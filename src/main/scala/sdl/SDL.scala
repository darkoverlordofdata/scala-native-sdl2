import scalanative.native._
import SDL._

/**
 * @see https://gist.github.com/densh/1885e8b03127fd52ff659505d8b3b76b
 */
@extern
@link("SDL2")
object SDL {
  
  type Window   = Ptr[CStruct0]
  type Renderer = Ptr[CStruct0]
  type Surface  = Ptr[CStruct0]
  type Texture  = Ptr[CStruct0]
  type SDL_RWops = Ptr[CStruct0]

  def SDL_GetError():CString = extern
  def SDL_Init(flags: UInt):CInt = extern
  def SDL_CreateWindow(title: CString,
                       x: CInt,
                       y: CInt,
                       w: Int,
                       h: Int,
                       flags: UInt): Window = extern
  def SDL_DestroyWindow(win: Window):Unit = extern
  def SDL_Delay(ms: UInt):Unit = extern
  def SDL_CreateRenderer(win: Window,
                         index: CInt,
                         flags: UInt): Renderer = extern

  def SDL_DestroyRenderer(render: Renderer):Unit = extern
  def SDL_Quit():Unit = extern

  def SDL_CreateTextureFromSurface(render: Renderer, surface: Surface):Texture = extern
  def SDL_SetTextureBlendMode(texture: Texture, blendMode: UInt):UInt = extern

  def SDL_RWFromFile(file:CString, mode:CString):SDL_RWops = extern
  //def SDL_LoadBMP_RW(file, 1)

  type _56 = Nat.Digit[Nat._5, Nat._6]
  type Event = CStruct2[UInt, CArray[Byte, _56]]

  def SDL_PollEvent(event: Ptr[Event]): CInt = extern

  type Rect = CStruct4[CInt, CInt, CInt, CInt]

  def SDL_RenderClear(renderer: Renderer): Unit = extern
  def SDL_SetRenderDrawColor(renderer: Renderer,
                             r: UByte,
                             g: UByte,
                             b: UByte,
                             a: UByte): Unit = extern
  def SDL_RenderFillRect(renderer: Renderer, rect: Ptr[Rect]): Unit = extern
  def SDL_RenderPresent(renderer: Renderer): Unit = extern
  def SDL_RenderCopy(renderer: Renderer, texture: Texture, srcrect: Ptr[Rect], dstrect: Ptr[Rect]):Unit = extern

  def SDL_SetTextureColorMod(texture: Texture, r: UByte, g: UByte, b: UByte):Unit = extern

  type KeyboardEvent = CStruct8[UInt, UInt, UInt, UByte, UByte, UByte, UByte, Keysym]
  type Keysym = CStruct4[Scancode, Keycode, UShort, UInt]
  type Scancode = Int
  type Keycode = Int

  type MouseMotionEvent = CStruct9[UInt, UInt, UInt, UInt, UInt, X, Y, Int, Int]
  type X = Int
  type Y = Int

}

object SDLExtra {

  val SDL_INIT_TIMER          = 0x00000001.toUInt
  val SDL_INIT_AUDIO          = 0x00000010.toUInt
  val SDL_INIT_VIDEO          = 0x00000020.toUInt  /**< SDL_INIT_VIDEO implies SDL_INIT_EVENTS */
  val SDL_INIT_JOYSTICK       = 0x00000200.toUInt  /**< SDL_INIT_JOYSTICK implies SDL_INIT_EVENTS */
  val SDL_INIT_HAPTIC         = 0x00001000.toUInt
  val SDL_INIT_GAMECONTROLLER = 0x00002000.toUInt  /**< SDL_INIT_GAMECONTROLLER implies SDL_INIT_JOYSTICK */
  val SDL_INIT_EVENTS         = 0x00004000.toUInt
  val SDL_INIT_NOPARACHUTE    = 0x00100000.toUInt  /**< compatibility; this flag is ignored. */
  val SDL_INIT_EVERYTHING     = SDL_INIT_TIMER | SDL_INIT_AUDIO | SDL_INIT_VIDEO | SDL_INIT_EVENTS | SDL_INIT_JOYSTICK | SDL_INIT_HAPTIC | SDL_INIT_GAMECONTROLLER 


  val WINDOW_SHOWN = 0x00000004.toUInt
  val VSYNC        = 0x00000004.toUInt

  val QUIT_EVENT = 0x100.toUInt

  val KEYDOWN  = 0x300.toUInt
  val KEYUP    = (0x300 + 1).toUInt

  val MOUSEMOTION     = 0x400.toUInt
  val MOUSEBUTTONDOWN = 0x401.toUInt
  val MOUSEBUTTONUP   = 0x402.toUInt

  val FINGERDOWN      = 0x700.toUInt
  val FINGERUP        = 0x701.toUInt
  val FINGERMOTION    = 0x702.toUInt
  
  val RIGHT_KEY = 1073741903
  val LEFT_KEY  = 1073741904
  val DOWN_KEY  = 1073741905
  val UP_KEY    = 1073741906

  val KEY_ESC   = 27
  val KEY_z     = 122

  val SDL_BLENDMODE_NONE = 0.toUInt
  val SDL_BLENDMODE_BLEND = 1.toUInt
  val SDL_BLENDMODE_ADD = 2.toUInt
  val SDL_BLENDMODE_MOD = 4.toUInt

  val SDL_WINDOWPOS_CENTERED = 0x2FFF0000

  implicit class EventOps(val self: Ptr[Event]) extends AnyVal {
    def type_ = !(self._1)
  }

  implicit class RectOps(val self: Ptr[Rect]) extends AnyVal {
    def init(x: Int, y: Int, w: Int, h: Int): Ptr[Rect] = {
      !(self._1) = x
      !(self._2) = y
      !(self._3) = w
      !(self._4) = h
      self
    }
  }

  implicit class KeyboardEventOps(val self: Ptr[KeyboardEvent]) extends AnyVal {
    def keycode: Keycode = !(self._8._2)
  }

  implicit class MouseMotionEventOps(val self: Ptr[MouseMotionEvent]) extends AnyVal {
    def x: X = !(self._6)
    def y: Y = !(self._7)
  }
  
}


