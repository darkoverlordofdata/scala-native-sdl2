import scalanative.native._
import SDL._
import SDLExtra._
import SDL_ttf._
import SDL_image._
import SDL_image_extras._

object Main {
  def main(args: Array[String]): Unit = {

    val title  = c"ShmupWarz"
    val width  = 800
    val height = 800

    SDL_Init(INIT_VIDEO)
    TTF_Init()

    if (IMG_Init(IMG_INIT_PNG) != IMG_INIT_PNG) {
      println("Unable to init image")
    }

    val window: Window = SDL_CreateWindow(title, 0, 0, width, height, WINDOW_SHOWN)
    val renderer: Renderer = SDL_CreateRenderer(window, -1, VSYNC)
    val game = new ShmupWarz(renderer, width, height)
    val u = 1000000000.toDouble 
    var mark1 = System.nanoTime().toDouble / u
    var mark2 = 0.toDouble
    var delta = 0.toDouble
    var d = 0.toDouble
    var fps = 60
    var k = 0

    while (true) {
      mark2 = System.nanoTime().toDouble / u
      delta = mark2 - mark1
      mark1 = mark2
      k += 1
      d += delta
      if (d >= 1.0) {
        fps = k
        k = 0
        d = 0
      }
      game.handleEvents()
      game.draw(fps)
      game.update(delta)
    }
    SDL_DestroyRenderer(renderer)
    SDL_DestroyWindow(window)
    SDL_Quit()
  }
}

