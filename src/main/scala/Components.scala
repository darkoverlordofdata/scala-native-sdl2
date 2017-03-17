import scalanative.native._
import SDL._

object Input extends Enumeration {
    //type Input = Value
    val None, Left, Right, Jump, Restart, Quit = Value
}

sealed trait Actor
case object ActorDefault extends Actor
case object ActorBackground extends Actor
case object ActorText extends Actor
case object ActorLives extends Actor
case object ActorEnemy1 extends Actor
case object ActorEnemy2 extends Actor
case object ActorEnemy3 extends Actor
case object ActorPlayer extends Actor
case object ActorBullet extends Actor
case object ActorExplosion extends Actor
case object ActorBang extends Actor
case object ActorParticle extends Actor
case object ActorHud extends Actor


sealed trait Category 
case object CategoryBackground extends Category
case object CategoryBullet extends Category
case object CategoryEnemy extends Category
case object CategoryExplosion extends Category
case object CategoryParticle extends Category
case object CategoryPlayer extends Category


object Effect extends Enumeration {
    //type Effect = Value
    val Pew, Asplode, SmallAsplode = Value
}

object Enemies extends Enumeration {
    //type Enemies = Value
    val Enemy1, Enemy2, Enemy3 = Value
}

object Timers extends Enumeration {
    //type Timers = Value
    val Timer1 = Value(2)
    val Timer2 = Value(7)
    val Timer3 = Value(13)
}

abstract class Component

case class Color(r:UByte, g:UByte, b:UByte, a:UByte) extends Component
case class Health(current:Int, maximum:Int) extends Component
case class ScaleTween(min:Double, max:Double, speed:Double, repeat:Boolean, active:Boolean) extends Component
case class Sprite(texture:Ptr[Texture], width: Int, height: Int) extends Component

class Rectangle(var x:Int, var y:Int, var width:Int, var height:Int)
class Point2d(var x:Double, var y:Double) {}
class Vector2d(var x:Double, var y:Double) {}