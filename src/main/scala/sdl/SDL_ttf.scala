import scalanative.native._

@extern
@link("SDL2_ttf")
object SDL_ttf {
  def TTF_Init(): Unit = extern
}

object SDL_ttf_extras {
}