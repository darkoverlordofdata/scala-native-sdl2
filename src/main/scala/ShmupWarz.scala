import scalanative.native._
import SDL._
import SDLExtra._
import SDL_image._
import SDL_image_extras._
import SDL_ttf._
import scala.collection.mutable._

class ShmupWarz (val renderer: Renderer, val width:Int, val height:Int)  { 

  var pressed = collection.mutable.Set.empty[Keycode]
  var deactivate = new ListBuffer[Int]()

  var bullets:List[Point2d] = List()
  var enemies1:List[Point2d] = List()
  var enemies2:List[Point2d] = List()
  var enemies3:List[Point2d] = List()
  var explosions:List[Point2d] = List()
  var bangs:List[Point2d] = List()
  var particles:List[Point2d] = List()
  val FONT = c"/home/bruce/scala/shmupwarz/assets/fonts/OpenDyslexic-Bold.otf"

  var entities = initEntities()

  var delta = 0.0
  lazy val sys = new Systems(this)
  lazy val font = TTF_OpenFont(FONT, 28)

  object mouse {
    var x = 0
    var y = 0
    var pressed = false
  }
  var keycode: Int = 0
  
  def draw(fps:Int): Unit = {
      SDL_SetRenderDrawColor(renderer, 0.toUByte, 0.toUByte, 0.toUByte, 255.toUByte)
		  SDL_RenderClear(renderer)
      entities.filter(_.active).map(drawSprite)
      drawFps(fps)
      SDL_RenderPresent(renderer)
  }

  def drawSprite(e:Entity):Unit = {
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

  def drawFps(fps:Int):Unit = {
      val text = TTF_RenderUTF8_Solid(font, toCString(s"$fps"), 0xffffff00)
      val texture = SDL_CreateTextureFromSurface(renderer, text)
      SDL_SetTextureBlendMode(texture, SDL_BLENDMODE_BLEND)
      val rect = stackalloc[Rect].init(5, 5, 56, 28)
      SDL_RenderCopy(renderer, texture, null, rect)
  }

  def update(delta:Double): Unit = {

      sys.spawn(delta)
      entities = sys.collision(delta, entities)
        .map(sys.create(delta))
        .map(sys.input(delta))
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

  def removeEntity(id:Int):Unit = deactivate += id
  def addBullet(x:Double, y:Double):Unit = bullets = new Point2d(x, y) :: bullets
  def addExplosion(x:Double, y:Double):Unit = explosions = new Point2d(x, y) :: explosions
  def addBang(x:Double, y:Double):Unit = bangs = new Point2d(x, y) :: bangs
  def addParticle(x:Double, y:Double):Unit = particles = new Point2d(x, y) :: particles
   
  def addEnemy(enemy:Int):Unit = enemy match {
    case 1 => enemies1 = new Point2d(0,0) :: enemies1
    case 2 => enemies2 = new Point2d(0,0) :: enemies2
    case 3 => enemies3 = new Point2d(0,0) :: enemies3
    case _ => ()
  }

  def initEntities():List[Entity] = {
    return List(
        Entities.createBackground(renderer),
        Entities.createEnemy1(renderer),
        Entities.createEnemy1(renderer),
        Entities.createEnemy1(renderer),
        Entities.createEnemy1(renderer),
        Entities.createEnemy2(renderer),
        Entities.createEnemy2(renderer),
        Entities.createEnemy2(renderer),
        Entities.createEnemy2(renderer),
        Entities.createEnemy3(renderer),
        Entities.createEnemy3(renderer),
        Entities.createEnemy3(renderer),
        Entities.createEnemy3(renderer),
        // Entities.createParticle(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBullet(renderer),
        Entities.createBang(renderer),
        Entities.createBang(renderer),
        Entities.createBang(renderer),
        Entities.createBang(renderer),
        Entities.createBang(renderer),
        Entities.createBang(renderer),
        Entities.createExplosion(renderer),
        Entities.createExplosion(renderer),
        Entities.createExplosion(renderer),
        Entities.createExplosion(renderer),
        Entities.createExplosion(renderer),
        Entities.createExplosion(renderer),
        Entities.createPlayer(renderer)
    )
  }
}