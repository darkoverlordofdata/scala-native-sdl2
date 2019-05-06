import scalanative.native._
import sdl2.SDL._
import sdl2.Extras._
import sdl2.image.SDL_image._
import sdl2.ttf.SDL_ttf._
import scala.collection.mutable._

class ShmupWarz (val renderer: Ptr[SDL_Renderer], val width:Int, val height:Int)  { 

    var pressed = collection.mutable.Set.empty[SDL_Keycode]
    var deactivate = new ListBuffer[Int]()
    var running = false

    var bullets:List[Point2d] = List()
    var enemies1:List[Point2d] = List()
    var enemies2:List[Point2d] = List()
    var enemies3:List[Point2d] = List()
    var explosions:List[Point2d] = List()
    var bangs:List[Point2d] = List()
    var particles:List[Point2d] = List()
    val FONT = c"assets/fonts/OpenDyslexic-Bold.otf"
    var delta = 0.0
    lazy val sys = new Systems(this)
    lazy val font = TTF_OpenFont(FONT, 28)
    var entities = Entities.createLevel(renderer)

    object mouse {
        var x = 0
        var y = 0
        var pressed = false
    }
    var keycode: Int = 0

    /** message queues */  
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

  def start():Unit = {
      running = true
  }
  /**
   * Draw frame
   */
  def draw(fps:Int): Unit = {
      SDL_SetRenderDrawColor(renderer, 0.toUByte, 0.toUByte, 0.toUByte, 255.toUByte)
		  SDL_RenderClear(renderer)
      entities.filter(_.active).map(drawEntity)
    //   drawFps(fps)
      SDL_RenderPresent(renderer)
  }

  def drawEntity(e:Entity):Unit = {
      def setTint():Unit = {
          e.tint match {
              case Some(tint) => SDL_SetTextureColorMod(e.sprite.texture, tint.r, tint.g, tint.b)
              case _ => ()
          }
      }

      if (e.category == CategoryBackground) {
          setTint()
          SDL_RenderCopy(renderer, e.sprite.texture, null, null) 
      } else {
          val w = (e.sprite.width * e.scale.x).toInt
          val h = (e.sprite.height * e.scale.y).toInt
          val x = (e.position.x - w / 2).toInt
          val y = (e.position.y - h / 2).toInt
          setTint()
          val rect = stackalloc[SDL_Rect].init(x, y, w, h)
          SDL_RenderCopy(renderer, e.sprite.texture, null, rect) 
      }
  }

  def drawFps(fps:Int):Unit = {
      val msg: CString = c"fps"
      val tint: UInt = 0xffffff00.toUInt
      val text = TTF_RenderUTF8_Solid(font, msg, tint)
      val texture = SDL_CreateTextureFromSurface(renderer, text)
    //   SDL_SetTextureBlendMode(texture, SDL_BLENDMODE_BLEND)
      val rect = stackalloc[SDL_Rect].init(5, 5, 56, 28)
      SDL_RenderCopy(renderer, texture, null, rect)
  }

  /**
   * Update frame
   */
  def update(delta:Double): Unit = {
      sys.spawn(delta)
      entities = entities
          .map(sys.collision(delta))
          .map(sys.create(delta))
          .map(sys.input(delta))
          .map(sys.sound(delta))
          .map(sys.physics(delta))
          .map(sys.expire(delta))
          .map(sys.tween(delta))
          .map(sys.remove(delta))
  }

    /**
     * Handle Events
     */
    def handleEvents():Unit = {
        val event = stackalloc[SDL_Event]
        while (SDL_PollEvent(event) != 0) {
            event.type_ match {

                case SDL_KEYDOWN =>
                    keycode = event.key.keysym.sym
                    if (keycode == SDLK_ESCAPE) running = false
                    pressed += keycode

                case SDL_KEYUP =>
                    keycode = event.key.keysym.sym
                    pressed -= keycode

                case SDL_MOUSEMOTION =>
                    mouse.x = event.motion.x
                    mouse.y = event.motion.y

                case SDL_MOUSEBUTTONDOWN =>
                    mouse.pressed = true
                    mouse.x = event.motion.x
                    mouse.y = event.motion.y

                case SDL_MOUSEBUTTONUP =>
                    mouse.pressed = false

                case SDL_QUIT =>
                    System.exit(0)

                case _ =>
                    ()
            }
        }
    }
}