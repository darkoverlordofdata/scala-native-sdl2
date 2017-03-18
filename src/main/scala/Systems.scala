import scalanative.native._
import SDL._
import SDLExtra._
import SDL_image._
import SDL_image_extras._


class Systems (val game: ShmupWarz) {
    var rand = new java.util.Random
    // var enemyT1: Double = Timers.Timer1.asInstanceOf[Double]
    // var enemyT2: Double = Timers.Timer2.asInstanceOf[Double]
    // var enemyT3: Double = Timers.Timer3.asInstanceOf[Double]
    var FireRate : Double = 0.1
    var timeToFire: Double = 0.0

    rand.setSeed(java.lang.System.nanoTime)

    /** 
     * Handle player input
     */
    def input (delta:Double) (e:Entity):Entity = (e.active, e.category) match {
        case (true, CategoryPlayer) => {
            val x = game.mouse.x.toDouble
            val y = game.mouse.y.toDouble
            if (game.pressed.contains(122)) { // z
                timeToFire -= delta
                if (timeToFire < 0.0) {
                    game.bullets += (new Point2d(e.position.x - 27, e.position.y + 2))
                    game.bullets += (new Point2d(e.position.x + 27, e.position.y + 2))
                    timeToFire = FireRate
                }
            }
            e.copy(position=new Point2d(x, y))
        }
        case _ => e
    }
    

    /**
     * Spawn enemies
     */
    def spawn(delta:Double)(e:Entity):Entity = {
        e
    }

    /**
     * Motion
     */
    def physics(delta:Double)(e:Entity):Entity = (e.active, e.velocity) match {
        case (true, Some(velocity)) => {
            val x = e.position.x * velocity.x * delta
            val y = e.position.y * velocity.y * delta
            e.copy(position=new Point2d(x, y))
        }
        case _ => e
    }
    

    /**
     * Expire enities
     */
    def expire(delta:Double)(e:Entity):Entity = {
        e
    }


    /**
     * Tween 
     */
    def tween(delta:Double)(e:Entity):Entity = {
        e
    }

    /**
     * remove offscreen entities
     */
    def remove(delta:Double)(e:Entity):Entity = {
        e
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
    def create(delta:Double, entities:List[Entity]):List[Entity] = {
        entities
    }


}