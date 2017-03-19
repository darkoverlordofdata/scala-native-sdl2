import scalanative.native._
import SDL._
import SDLExtra._
import SDL_mixer._
import SDL_mixer_extras._

class Systems (val game: ShmupWarz) {
    var rand = new java.util.Random
    var enemyT1: Double = 2.0
    var enemyT2: Double = 7.0
    var enemyT3: Double = 13.0
    var FireRate : Double = 0.1
    var timeToFire: Double = 0.0
    val Tau = 6.28318

    val s1 = Mix_LoadWAV(c"/home/bruce/scala/shmupwarz/assets/sounds/pew.wav")
    val s2 = Mix_LoadWAV(c"/home/bruce/scala/shmupwarz/assets/sounds/asplode.wav")
    val s3 = Mix_LoadWAV(c"/home/bruce/scala/shmupwarz/assets/sounds/smallasplode.wav")

    rand.setSeed(java.lang.System.nanoTime)

    /** 
     * Handle player input
     */
    def input(delta:Double) (entity:Entity):Entity = (entity.active, entity.category) match {
        case (true, CategoryPlayer) => 
            val x = game.mouse.x.toDouble
            val y = game.mouse.y.toDouble
            if (game.pressed.contains(KEY_z) || game.mouse.pressed) { // z
                timeToFire -= delta
                if (timeToFire < 0.0) {
                    game.bullets = new Point2d(entity.position.x - 27, entity.position.y + 2) :: game.bullets
                    game.bullets = new Point2d(entity.position.x + 27, entity.position.y + 2) :: game.bullets
                    timeToFire = FireRate
                }
            }
            entity.copy(position=new Point2d(x, y))
        
        case _ => entity
    }
    
    def sound(delta:Double)(entity:Entity):Entity = (entity.active, entity.sound) match {
        case (true, Some(sound)) => {
            sound match {
                case EffectPew => Mix_PlayChannel(s1, 0, 0)
                case EffectAsplode => Mix_PlayChannel(s2, 0, 0)
                case EffectSmallAsplode => Mix_PlayChannel(s3, 0, 0)
                case _ => ()
            }
            entity
        }
        case _ => entity
    }

    /**
     * Motion
     */
    def physics(delta:Double) (entity:Entity):Entity = (entity.active, entity.velocity) match {
        case (true, Some(velocity)) => 
            val x = entity.position.x + velocity.x * delta
            val y = entity.position.y + velocity.y * delta
            val x1 = (x-entity.bounds.width/2).toInt
            val y1 = (y-entity.bounds.height/2).toInt

            entity.copy(
                position = new Point2d(x, y), 
                bounds = new Rectangle(x1, y1, entity.bounds.width, entity.bounds.height))
        
        case _ =>  entity
    }
    
    /**
     * Expire enities
     */
    def expire(delta:Double)(entity:Entity):Entity = (entity.active, entity.expires) match {
        case (true, Some(expires)) => 
            val exp = expires - delta
            entity.copy(
                expires = Some(exp), 
                active = if (exp > 0.0) true else false)
                
        case _ => entity
    }

    /**
     * Tween 
     */
    def tween(delta:Double)(entity:Entity):Entity = (entity.active, entity.scaleTween) match {
        case (true, Some(tween)) => {
            var x = entity.scale.x + (tween.speed * delta)
            var y = entity.scale.y + (tween.speed * delta)
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
            entity.copy(
                scale = new Vector2d(x, y), 
                scaleTween = Some(new ScaleTween(tween.min, tween.max, tween.speed, tween.repeat, active))
                )

        }
        case _ => entity
    }

    /**
     * remove offscreen entities
     */
    def remove(delta:Double)(entity:Entity):Entity = (entity.active, entity.category) match {
        case (true, CategoryEnemy) 
            if (entity.position.y > game.height) => {
                entity.copy(active = false)
        }
        case (true, CategoryBullet) 
            if (entity.position.y < 0) => {
                entity.copy(active = false)
        }
        case _ => entity
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
    def create(delta:Double)(entity:Entity):Entity = (entity.active, entity.actor) match {
        case (true, _)  => 
            val ix = game.deactivate.indexOf(entity.id)
            if (ix != -1) {
                game.deactivate.remove(ix)
                entity.copy(active = false)
            } else entity
            
        case (false, ActorBullet) => 
            game.bullets match {
                case Nil => entity
                case bullet :: rest =>
                    game.bullets = rest
                    Entities.bullet(entity, bullet.x, bullet.y)
            } 

        case (false, ActorEnemy1) => 
            game.enemies1 match {
                case Nil => entity
                case enemy :: rest =>
                    game.enemies1 = rest
                    Entities.enemy1(entity, game.width, rand)
            }

        case (false, ActorEnemy2) => 
            game.enemies2 match {
                case Nil => entity
                case enemy :: rest =>
                    game.enemies2 = rest
                    Entities.enemy2(entity, game.width, rand)
            }

        case (false, ActorEnemy3) => 
            game.enemies3 match {
                case Nil => entity
                case enemy :: rest =>
                    game.enemies3 = rest
                    Entities.enemy3(entity, game.width, rand)
            }

        case (false, ActorExplosion) => 
            game.explosions match {
                case Nil => entity
                case explosion :: rest =>
                    game.explosions = rest
                    Entities.explosion(entity, explosion.x, explosion.y)
            }

        case (false, ActorBang) => 
            game.bangs match {
                case Nil => entity
                case bang :: rest =>
                    game.bangs = rest
                    Entities.bang(entity, bang.x, bang.y)
            }

        case (false, ActorParticle) => 
            game.particles match {
                case Nil => entity
                case particle :: rest =>
                    game.particles = rest
                    Entities.particle(entity, particle.x, particle.y, rand)
            }

        case _ => entity
        
    }

    /**
     * Handle collisions
     */
    def collision(delta:Double)(entity:Entity):Entity = {
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

        def collide(entity:Entity):Entity = {
            for (bullet <- game.entities) 
                if (bullet.active && bullet.category == CategoryBullet) 
                    return if (intersects(entity, bullet)) handleCollision(entity, bullet) else entity
            entity
        }
        (entity.active, entity.category) match {
            case (true, CategoryEnemy) => collide(entity)
            case _ => entity
        }
    }

}

