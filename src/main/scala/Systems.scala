import scalanative.native._
import SDL._
import SDLExtra._
import SDL_image._
import SDL_image_extras._


class Systems (val game: ShmupWarz) {
    var enemyT1: Double = Timers.Timer1.asInstanceOf[Double]
    var enemyT2: Double = Timers.Timer2.asInstanceOf[Double]
    var enemyT3: Double = Timers.Timer3.asInstanceOf[Double]
    var FireRate : Double = 0.1
    var timeToFire: Double = 0.0


    
}