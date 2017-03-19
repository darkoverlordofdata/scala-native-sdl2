import scalanative.native._
import SDL._
import SDLExtra._
import SDL_ttf._
import SDL_image._
import SDL_image_extras._
import SDL_mixer._
import SDL_mixer_extras._

object Main {
  def main(args: Array[String]): Unit = {

    if (SDL_Init(SDL_INIT_EVERYTHING) < 0) {
      println( "SDL could not initialize! SDL Error: ${SDL_GetError()}\n")
    }
    TTF_Init()
    if (IMG_Init(IMG_INIT_PNG) != IMG_INIT_PNG) {
      println("Unable to init image")
    }
    if (Mix_OpenAudio(MIX_DEFAULT_FREQUENCY, MIX_DEFAULT_FORMAT, 2, 2048) == -1) {
      println("Unable to init mixer")
    }

    val title  = c"ShmupWarz"
    val width  = (320*1.5).toInt
    val height = (480*1.5).toInt
    val window: Window = SDL_CreateWindow(title, SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, width, height, WINDOW_SHOWN)
    val renderer: Renderer = SDL_CreateRenderer(window, -1, VSYNC)
    val game = new ShmupWarz(renderer, width, height)
    val u = 1000000000.toDouble 
    var mark1 = System.nanoTime().toDouble / u
    var mark2 = 0.toDouble
    var delta = 0.toDouble
    var d = 0.toDouble
    var fps = 60
    var k = 0

    game.start()
    while (game.running) {
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

