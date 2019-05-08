import scalanative.native._
import sdl2.SDL._
import sdl2.Extras._
import sdl2.image.SDL_image._
import sdl2.image.Extras._
import sdl2.ttf.SDL_ttf._

object Main {
    def main(args: Array[String]): Unit = {

        if (SDL_Init(SDL_INIT_EVERYTHING) < 0) {
            println( "SDL could not initialize! SDL Error: ${SDL_GetError()}\n")
        }
        TTF_Init()
        if (IMG_Init(IMG_INIT_PNG) != IMG_INIT_PNG) {
            println("Unable to init image")
        }
        // if (Mix_OpenAudio(MIX_DEFAULT_FREQUENCY, MIX_DEFAULT_FORMAT, 2, 2048) == -1) {
        //   println("Unable to init mixer")
        // }

        val title  = c"ShmupWarz"
        // val width  = (320*1.5).toInt
        // val height = (480*1.5).toInt
        val width  = (640*1.5).toInt
        val height = (512*1.5).toInt
        val window: Ptr[SDL_Window] = SDL_CreateWindow(title, SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, width, height, SDL_WINDOW_SHOWN)
        val renderer: Ptr[SDL_Renderer] = SDL_CreateRenderer(window, -1, SDL_RENDERER_PRESENTVSYNC)
        val game = new ShmupWarz(renderer, width, height)
        val u = 1000000000.toDouble 
        var mark1 = System.nanoTime().toDouble / u
        var mark2 = 0.toDouble
        var delta = 0.toDouble
        var d = 0.toDouble
        var fps = 60
        var k = 0
        var t = 0.0
        var k2 = 0

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
            val m1 = System.nanoTime().toDouble / u
            game.update(delta)
            val m2 = System.nanoTime().toDouble / u
            k2 = k2 +1
            t = t + (m2 - m1)
            if (k2 >= 1000) {
                // println(s"${t/1000.0}")
                println(s"${t}")
                k2 = 0
                t = 0.0
            }
            game.draw(fps)
        }
        SDL_DestroyRenderer(renderer)
        SDL_DestroyWindow(window)
        SDL_Quit()
    }
}

