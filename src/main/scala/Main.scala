import scalanative.native._
import SDL._
import SDLExtra._
import SDL_ttf._
import SDL_image._
import SDL_image_extras._

object Main {
  def main(args: Array[String]): Unit = {

    val title  = c"Snake"
    val width  = 800
    val height = 800

    SDL_Init(INIT_VIDEO)
    TTF_Init()

    // val imgFlags = IMG_InitFlags.IMG_INIT_PNG
    val imgFlags = IMG_INIT_PNG
    if (IMG_Init(imgFlags) != imgFlags) {
      println("Unable to init image")
    }

    val window: Ptr[Window] = SDL_CreateWindow(title, 0, 0, width, height, WINDOW_SHOWN)
    val renderer: Ptr[Renderer] = SDL_CreateRenderer(window, -1, VSYNC)

    
    val game = new ShmupWarz(renderer, width, height)

    while (true) {
      game.handleEvents()
      game.draw()
      game.update()
      SDL_Delay((1000 / 12).toUInt)
    }
    SDL_DestroyRenderer(renderer)
    SDL_DestroyWindow(window)
    SDL_Quit()
  }
}

