import scalanative.native._

class Systems (val game: ShmupWarz) {
    var rand = new java.util.Random
    var enemyT1: Double = 2.0
    var enemyT2: Double = 7.0
    var enemyT3: Double = 13.0
    var FireRate : Double = 0.1
    var timeToFire: Double = 0.0
    val Tau = 6.28318

    rand.setSeed(java.lang.System.nanoTime)

    /** 
     * Handle player input
     */
    def input(delta:Double) (e:Entity):Entity = (e.active, e.category) match {
        case (true, CategoryPlayer) => 
            val x = game.mouse.x.toDouble
            val y = game.mouse.y.toDouble
            if (game.pressed.contains(122) || game.mouse.pressed) { // z
                timeToFire -= delta
                if (timeToFire < 0.0) {
                    game.bullets = new Point2d(e.position.x - 27, e.position.y + 2) :: game.bullets
                    game.bullets = new Point2d(e.position.x + 27, e.position.y + 2) :: game.bullets
                    timeToFire = FireRate
                }
            }
            e.copy(position=new Point2d(x, y))
        
        case _ => e
    }
    

    /**
     * Motion
     */
    def physics(delta:Double) (e:Entity):Entity = (e.active, e.velocity) match {
        case (true, Some(velocity)) => 
            val x = e.position.x + velocity.x * delta
            val y = e.position.y + velocity.y * delta
            val x1 = x.toInt
            val y1 = (y-e.bounds.height/2).toInt

            e.copy(
                position = new Point2d(x, y), 
                bounds = new Rectangle(x1, y1, e.bounds.width, e.bounds.height))
        
        case _ =>  e
    }
    
    /**
     * Expire enities
     */
    def expire(delta:Double)(e:Entity):Entity = (e.active, e.expires) match {
        case (true, Some(expires)) => 
            val exp = expires - delta
            e.copy(
                expires = Some(exp), 
                active = if (exp > 0.0) true else false)
                
        case _ => e
    }

    /**
     * Tween 
     */
    def tween(delta:Double)(e:Entity):Entity = (e.active, e.scaleTween, e.scale) match {
        case (true, Some(tween), Some(scale)) => {
            var x = scale.x + (tween.speed * delta)
            var y = scale.y + (tween.speed * delta)
            var active = tween.active

            if (x > tween.max) {
                x = tween.max
                y = tween.max
                active = false
            } else if (x < tween.min) {
                x = tween.min
                y = tween.min
                active = false
            }
            e.copy(
                scale = Some(new Vector2d(x, y)), 
                scaleTween = Some(new ScaleTween(tween.min, tween.max, tween.speed, tween.repeat, active))
                )

        }
        case _ => e
    }

    /**
     * remove offscreen entities
     */
    def remove(delta:Double)(e:Entity):Entity = (e.active, e.category) match {
        case (true, CategoryEnemy) 
            if (e.position.y > game.height) => {
                e.copy(active = false)
        }
        case (true, CategoryBullet) 
            if (e.position.y < 0) => {
                e.copy(active = false)
        }
        case _ => e
    }

    /**
     * Spawn enemies
     */
    def spawn(delta:Double):Unit = {

        def spawnEnemy(t:Double, enemy:Int):Double = {
            val d1 = t-delta
            if (d1 < 0.0) {
                game.addEnemy(enemy)
                enemy match {
                    case 1 => 2.0
                    case 2 => 7.0
                    case 3 => 13.0
                    case _ => 0.0
                }
            } else d1
        }
        enemyT1 = spawnEnemy(enemyT1, 1)
        enemyT2 = spawnEnemy(enemyT2, 2)
        enemyT3 = spawnEnemy(enemyT3, 3)
    }

    /**
     * create entities from que
     */
    def create(delta:Double)(e:Entity):Entity = (e.active, e.actor) match {
        case (true, _)  => 
            val ix = game.deactivate.indexOf(e.id)
            if (ix != -1) {
                game.deactivate.remove(ix)
                e.copy(active = false)
            } else e
            
        case (false, ActorBullet) => 
            game.bullets match {
                case Nil => e
                case bullet :: rest =>
                    game.bullets = rest
                    Entities.bullet(e, bullet.x, bullet.y)
            } 

        case (false, ActorEnemy1) => 
            game.enemies1 match {
                case Nil => e
                case enemy :: rest =>
                    game.enemies1 = rest
                    Entities.enemy1(e, game.width, rand)
            }

        case (false, ActorEnemy2) => 
            game.enemies2 match {
                case Nil => e
                case enemy :: rest =>
                    game.enemies2 = rest
                    Entities.enemy2(e, game.width, rand)
            }

        case (false, ActorEnemy3) => 
            game.enemies3 match {
                case Nil => e
                case enemy :: rest =>
                    game.enemies3 = rest
                    Entities.enemy3(e, game.width, rand)
            }

        case (false, ActorExplosion) => 
            game.explosions match {
                case Nil => e
                case explosion :: rest =>
                    game.explosions = rest
                    Entities.explosion(e, explosion.x, explosion.y)
            }

        case (false, ActorBang) => 
            game.bangs match {
                case Nil => e
                case bang :: rest =>
                    game.bangs = rest
                    Entities.bang(e, bang.x, bang.y)
            }

        case (false, ActorParticle) => 
            game.particles match {
                case Nil => e
                case particle :: rest =>
                    game.particles = rest
                    Entities.particle(e, particle.x, particle.y, rand)
            }

        case _ => e
        
    }

    /**
     * Handle collisions
     */
    def collision(delta:Double)(e:Entity):Entity = {
        def intersects(a:Entity, b:Entity):Boolean = {
            val r1 = a.bounds
            val r2 = b.bounds
            return ((r1.x < r2.x + r2.width) && 
                    (r1.x + r1.width > r2.x) && 
                    (r1.y < r2.y + r2.height) && 
                    (r1.y + r1.height > r2.y)) 
        }

        def handleCollision(a: Entity, b: Entity):Entity = {
            game.addBang(b.position.x, b.position.y)
            game.removeEntity(b.id)
            for (i <- 0 to 3) game.addParticle(b.position.x, b.position.y)
            a.health match {
                case Some(health) => {
                    val h = health.current -2
                    if (h < 0) {
                        game.addExplosion(b.position.x, b.position.y)
                        return a.copy(active=false)
                    } else {
                        return a.copy(health=Some(new Health(h, health.maximum)))
                    }   
                }
                case _ => a
            }
        }

        def collide(e:Entity):Entity = {
            for (bullet <- game.entities) 
                if (bullet.active && bullet.category == CategoryBullet) 
                    return if (intersects(e, bullet)) handleCollision(e, bullet) else e
            e
        }
        (e.active, e.category) match {
            case (true, CategoryEnemy) => collide(e)
            case _ => e
        }
    }
}