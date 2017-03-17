import scalanative.native._
import SDL._
import SDLExtra._
import SDL_image._
import SDL_image_extras._
import scala.collection.mutable._


class ShmupWarz (renderer: Ptr[Renderer], width:Int, height:Int)  { 
  var pressed                 = collection.mutable.Set.empty[Keycode]
  var rand                    = new java.util.Random
  var particles:LinkedList[Point2d] = new LinkedList()
  var bullets:LinkedList[Point2d] = new LinkedList()
  var enemies1:LinkedList[Point2d] = new LinkedList()
  var enemies2:LinkedList[Point2d] = new LinkedList()
  var enemies3:LinkedList[Point2d] = new LinkedList()
  var explosions:LinkedList[Point2d] = new LinkedList()
  var bangs:LinkedList[Point2d] = new LinkedList()
  val rect = stackalloc[Rect].init(0, 0, 0, 0)


  rand.setSeed(java.lang.System.nanoTime)

  // val surface:Ptr[Surface] = IMG_Load(c"/home/bruce/scala/shmupwarz/assets/images/BackdropBlackLittleSparkBlack.png")
  // if (surface == null) {
  //   println("unable to load surface")
  // }
  // val background:Ptr[Texture] = SDL_CreateTextureFromSurface(renderer, surface)
  // if (background == null) {
  //   println("unable to load texture")
  // }
  // SDL_SetTextureBlendMode(background, SDL_BLENDMODE_BLEND)


  var entities = initEntities()


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
  }


  def handleEvents():Unit = {
    val event = stackalloc[Event]
      while (SDL_PollEvent(event) != 0) {
        event.type_ match {
          case QUIT_EVENT =>
            // return
            System.exit(0)
          case KEY_DOWN =>
            pressed += event.cast[Ptr[KeyboardEvent]].keycode
          case KEY_UP =>
            pressed -= event.cast[Ptr[KeyboardEvent]].keycode
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

}