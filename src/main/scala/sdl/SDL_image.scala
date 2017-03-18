import scalanative.native._
import SDL._

@extern
@link("SDL2_image")
object SDL_image {


  def IMG_Init(flags: UInt):UInt = extern
  def IMG_Load(path: CString):Surface = extern


}

object SDL_image_extras {

  val IMG_INIT_JPG = 1.toUInt
  val IMG_INIT_PNG = 2.toUInt
  val IMG_INIT_TIF = 4.toUInt
  
}
