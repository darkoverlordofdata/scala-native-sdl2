import scalanative.native._
import SDL._

@extern
@link("SDL2_ttf")
object SDL_ttf {
  
  type Font   = Ptr[CStruct0]


  def TTF_Init(): Unit = extern

  def TTF_OpenFont(path:CString, size:CInt):Font = extern
  def TTF_RenderUTF8_Solid(font:Font, text:CString, color:CInt):Surface = extern
}

object SDL_ttf_extras {
}