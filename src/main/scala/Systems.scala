import scalanative.native._
import SDL._
import SDLExtra._
import SDL_image._
import SDL_image_extras._


class Systems (val game: ShmupWarz) {
    var rand = new java.util.Random
    var enemyT1: Double = 2.0
    var enemyT2: Double = 7.0
    var enemyT3: Double = 13.0
    var FireRate : Double = 0.1
    var timeToFire: Double = 0.0

    rand.setSeed(java.lang.System.nanoTime)

    /** 
     * Handle player input
     */
    def input (delta:Double) (e:Entity):Entity = (e.active, e.category) match {
        case (true, CategoryPlayer) => 
            val x = game.mouse.x.toDouble
            val y = game.mouse.y.toDouble
            if (game.pressed.contains(122)) { // z
                timeToFire -= delta
                if (timeToFire < 0.0) {
                    game.bullets = new Point2d(e.position.x - 27, e.position.y + 2) :: game.bullets
                    game.bullets = new Point2d(e.position.x + 27, e.position.y + 2) :: game.bullets
                    timeToFire = FireRate
                }
            }
            e.copy(position=new Point2d(x, y))
        
        case _ => 
            e
    }
    

    /**
     * Motion
     */
    def physics(delta:Double)(e:Entity):Entity = (e.active, e.velocity) match {
        case (true, Some(velocity)) => 
            val x = e.position.x * velocity.x * delta
            val y = e.position.y * velocity.y * delta
            e.copy(position=new Point2d(x, y))
        
        case _ => 
            e
    }
    
    /**
     * Expire enities
     */
    def expire(delta:Double)(e:Entity):Entity = (e.active, e.expires) match {
        case (true, Some(expires)) => 
            val exp = expires - delta
            e.copy(
                expires = Some(exp), 
                active = if (exp > 0.0) true else false
                )
        
        case _ => 
            e
    }

    /**
     * Tween 
     */
    def tween(delta:Double)(e:Entity):Entity = (e.active, e.scaleTween) match {
        case (true, Some(tween)) => {
            var x = e.scale.x + (tween.speed * delta)
            var y = e.scale.y + (tween.speed * delta)
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
                scale = new Vector2d(x, y), 
                scaleTween = Some(new ScaleTween(tween.min, tween.max, tween.speed, tween.repeat, active))
                )

            e
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
     * Handle collisions
     */
    def collision(delta:Double, entities:List[Entity]):List[Entity] = {
        entities
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
                case bullet :: rest =>
                    game.bullets = rest
                    e.copy(
                        active = true,
                        expires = Some(1.0), 
                        position = new Point2d(bullet.x, bullet.y))
                case Nil =>
                    e
            } 

        case (false, ActorEnemy1) => 
            game.enemies1 match {
                case enemy :: rest =>
                    game.enemies1 = rest
                    e.copy(
                        active = true,
                        position = new Point2d(rand.nextInt(game.width-35).toDouble, 92.0/2.0),
                        health = Some(new Health(10, 10)))
                case Nil =>
                    e
            }

        case (false, ActorEnemy2) => 
            game.enemies2 match {
                case enemy :: rest =>
                    game.enemies2 = rest
                    e.copy(
                        active = true,
                        position = new Point2d(rand.nextInt(game.width-86).toDouble, 172.0/2.0),
                        health = Some(new Health(20, 20)))
                case Nil =>
                    e
            }

        case (false, ActorEnemy3) => 
            game.enemies3 match {
                case enemy :: rest =>
                    game.enemies3 = rest
                    e.copy(
                        active = true,
                        position = new Point2d(rand.nextInt(game.width-160).toDouble, 320.0/2.0),
                        health = Some(new Health(60, 60)))
                case Nil =>
                    e
            }

        case (false, ActorExplosion) => 
            game.explosions match {
                case explosion :: rest =>
                    game.explosions = rest
                    e.copy(
                        active = true,
                        position = new Point2d(explosion.x, explosion.y),
                        scale = new Vector2d(0.5, 0.5),
                        expires = Some(0.2))
                case Nil =>
                    e
            }

        case (false, ActorBang) => 
            game.bangs match {
                case bang :: rest =>
                    game.bangs = rest
                    e.copy(
                        active = true,
                        position = new Point2d(bang.x, bang.y),
                        scale = new Vector2d(0.2, 0.2),
                        expires = Some(0.2))
                case Nil =>
                    e
            }

        case _ => e
        
    }


}