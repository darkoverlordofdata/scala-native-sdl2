import scalanative.native._
import SDL._
import SDLExtra._
import SDL_image._
import SDL_image_extras._
import scala.collection.mutable._



class ShmupWarz (renderer: Ptr[Renderer], width:Int, height:Int)  { 
  var pressed = collection.mutable.Set.empty[Keycode]
  var particles = new ListBuffer[Point2d]()
  var bullets = new ListBuffer[Point2d]()
  var enemies1 = new ListBuffer[Point2d]()
  var enemies2 = new ListBuffer[Point2d]()
  var enemies3 = new ListBuffer[Point2d]()
  var explosions = new ListBuffer[Point2d]()
  var bangs = new ListBuffer[Point2d]()
  var entities = initEntities()
  var delta = 0.0
  lazy val sys = new Systems(this)

  object mouse {
    var x = 0
    var y = 0
    var pressed = false
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

      entities = sys.create(delta, sys.collision(delta, entities))
        .map(sys.input(delta))
        .map(sys.spawn(delta))
        .map(sys.physics(delta))
        .map(sys.physics(delta))
        .map(sys.expire(delta))
        .map(sys.tween(delta))
        .map(sys.remove(delta))
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
            keycode = event.cast[Ptr[KeyboardEvent]].keycode
            pressed += keycode

          case KEYUP =>
            keycode = event.cast[Ptr[KeyboardEvent]].keycode
            pressed -= keycode

          case MOUSEMOTION =>
            mouse.x = event.cast[Ptr[MouseMotionEvent]].x
            mouse.y = event.cast[Ptr[MouseMotionEvent]].y

          case MOUSEBUTTONDOWN =>
            mouse.pressed = true
            mouse.x = event.cast[Ptr[MouseMotionEvent]].x
            mouse.y = event.cast[Ptr[MouseMotionEvent]].y

          case MOUSEBUTTONUP =>
            mouse.pressed = false

          case QUIT_EVENT =>
            System.exit(0)

          case _ =>
            ()
        }
      }
  }

  def initEntities():List[Entity] = {
    return List(
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



}