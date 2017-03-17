import scalanative.native._
import SDL._
import SDLExtra._
import SDL_image._
import SDL_image_extras._
import scala.collection.mutable._


class ShmupWarz (renderer: Ptr[Renderer], width:Int, height:Int)  { 
  var pressed                 = collection.mutable.Set.empty[Keycode]
  var particles:LinkedList[Point2d] = new LinkedList()
  var bullets:LinkedList[Point2d] = new LinkedList()
  var enemies1:LinkedList[Point2d] = new LinkedList()
  var enemies2:LinkedList[Point2d] = new LinkedList()
  var enemies3:LinkedList[Point2d] = new LinkedList()
  var explosions:LinkedList[Point2d] = new LinkedList()
  var bangs:LinkedList[Point2d] = new LinkedList()
  var entities = initEntities()
  var delta = 0.0

  object mouse {
    var x:Int = 0
    var y:Int = 0
  }
  var keycode: Int = 0
  
  def draw(fps:Int): Unit = {
      SDL_SetRenderDrawColor(renderer, 0.toUByte, 0.toUByte, 0.toUByte, 255.toUByte)
		  SDL_RenderClear(renderer)
      for (entity <- entities) 
        if (entity.active)
          drawSprite(entity)
      SDL_RenderPresent(renderer)
  }

  def drawSprite(e:Entity) = {
      if (e.category == CategoryBackground) {
        SDL_RenderCopy(renderer, e.sprite.texture, null, null) 
      } else {
        val w = if (e.scale.x != 0) (e.sprite.width * e.scale.x).toInt else e.sprite.width
        val h = if (e.scale.y != 0) (e.sprite.height * e.scale.y).toInt else e.sprite.height
        val x = (e.position.x - w / 2).toInt
        val y = (e.position.y - h / 2).toInt
        val rect = stackalloc[Rect].init(x, y, w, h)
        SDL_RenderCopy(renderer, e.sprite.texture, null, rect) 
      }

  }

  def update(delta:Double): Unit = {
      entities.map(sys.input(delta))
  }

  def input(delta: Double, e: Entity) {
      if (e.active && e.category == CategoryPlayer) {
          e.position.x = mouse.x.toDouble
          e.position.y = mouse.y.toDouble

      }
  }  


  def handleEvents():Unit = {
    val event = stackalloc[Event]
      while (SDL_PollEvent(event) != 0) {
        event.type_ match {

          case KEYDOWN =>
            pressed += event.cast[Ptr[KeyboardEvent]].keycode
            //println(s"keydown:  $p")

          case KEYUP =>
            pressed -= event.cast[Ptr[KeyboardEvent]].keycode
            //println(s"keyup:  $p")

          case MOUSEMOTION =>
            mouse.x = event.cast[Ptr[MouseMotionEvent]].x
            mouse.y = event.cast[Ptr[MouseMotionEvent]].y
            //println(s"mousemotion: $x,$y")

          case MOUSEBUTTONDOWN =>
            mouse.x = event.cast[Ptr[MouseMotionEvent]].x
            mouse.y = event.cast[Ptr[MouseMotionEvent]].y
            //println(s"mousedown: $x,$y")

          case MOUSEBUTTONUP =>
            mouse.x = event.cast[Ptr[MouseMotionEvent]].x
            mouse.y = event.cast[Ptr[MouseMotionEvent]].y
            //println(s"mouseup: $x,$y")

          case QUIT_EVENT =>
            System.exit(0)

          case _ =>
            ()
        }
      }
  }

  def initEntities():Array[Entity] = {
    return Array(
        Entities.createBackground(renderer),
        Entities.createEnemy1(renderer),
        Entities.createEnemy2(renderer),
        Entities.createEnemy3(renderer),
        Entities.createParticle(renderer),
        Entities.createBullet(renderer),
        Entities.createBang(renderer),
        Entities.createExplosion(renderer),
        Entities.createPlayer(renderer)
    )
  }

  lazy val sys = new Systems(this)


}