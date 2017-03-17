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

    def input(delta:Double)(e:Entity):Unit = {
        if (e.active && e.category == CategoryPlayer) {
            e.position.x = game.mouse.x.toDouble
            e.position.y = game.mouse.y.toDouble

        //     if (game inputs[Input JUMP]) {
        //         timeToFire -= delta
        //         if (timeToFire < 0.0) {
        //             game bullets add((e position x - 27, e position y + 2) as Point2d)
        //             game bullets add((e position x + 27, e position y + 2) as Point2d)
        //             timeToFire = FireRate
        //         }
        //     }
        // }
            
        }
    }

    
}