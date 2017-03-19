import scalanative.native._
import SDL._

/**
 * Components
 */

abstract class Component
final case class Color(r:UByte, g:UByte, b:UByte, a:UByte) extends Component
final case class Health(current:Int, maximum:Int) extends Component
final case class ScaleTween(min:Double, max:Double, speed:Double, repeat:Boolean, active:Boolean) extends Component
final case class Sprite(texture:Texture, width: Int, height: Int) extends Component
final case class Rectangle(var x:Int, var y:Int, var width:Int, var height:Int) extends Component
final case class Point2d(var x:Double, var y:Double) extends Component
final case class Vector2d(var x:Double, var y:Double) extends Component

sealed trait Actor
final case object ActorDefault extends Actor
final case object ActorBackground extends Actor
final case object ActorText extends Actor
final case object ActorLives extends Actor
final case object ActorEnemy1 extends Actor
final case object ActorEnemy2 extends Actor
final case object ActorEnemy3 extends Actor
final case object ActorPlayer extends Actor
final case object ActorBullet extends Actor
final case object ActorExplosion extends Actor
final case object ActorBang extends Actor
final case object ActorParticle extends Actor
final case object ActorHud extends Actor


sealed trait Category 
final case object CategoryBackground extends Category
final case object CategoryBullet extends Category
final case object CategoryEnemy extends Category
final case object CategoryExplosion extends Category
final case object CategoryParticle extends Category
final case object CategoryPlayer extends Category


sealed trait Effect 
final case object EffectPew extends Effect
final case object EffectAsplode extends Effect
final case object EffectSmallAsplode extends Effect


sealed trait Enemies 
final case object Enemy1 extends Enemies
final case object Enemy2 extends Enemies
final case object Enemy3 extends Enemies


