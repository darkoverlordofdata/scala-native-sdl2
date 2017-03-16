import scalanative.native._
import SDL._
import SDLExtra._
import SDL_image._
import SDL_image_extras._

class ShmupWarz (renderer: Ptr[Renderer], width:Int, height:Int)  { 
  var pressed                 = collection.mutable.Set.empty[Keycode]
  var rand                    = new java.util.Random

  val surface:Ptr[Surface] = IMG_Load(c"/home/bruce/scala/shmupwarz/assets/images/BackdropBlackLittleSparkBlack.png")
  if (surface == null) {
    println("unable to load surface")
  }
  val texture:Ptr[Texture] = SDL_CreateTextureFromSurface(renderer, surface)
  if (texture == null) {
    println("unable to load texture")
  }
  SDL_SetTextureBlendMode(texture, SDL_BLENDMODE_BLEND)



  def draw(): Unit = {
      SDL_SetRenderDrawColor(renderer, 0.toUByte, 0.toUByte, 0.toUByte, 255.toUByte)
		  SDL_RenderClear(renderer)
      SDL_RenderCopy(renderer, texture, null, null)
      SDL_RenderPresent(renderer)
  }


  def update(): Unit = {
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

  rand.setSeed(java.lang.System.nanoTime)

}