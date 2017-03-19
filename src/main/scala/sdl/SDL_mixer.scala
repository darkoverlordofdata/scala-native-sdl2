import scalanative.native._
import SDL._

@extern
@link("SDL2_mixer")
object SDL_mixer {

    type Chunk   = Ptr[CStruct0]

    def Mix_OpenAudio(frequency:CInt, format:CInt, channels:CInt, chunksize:CInt):CInt = extern
    def Mix_CloseAudio():Unit = extern

    def Mix_LoadWAV_RW(src:SDL_RWops, freesrc:CInt):Chunk = extern
    def Mix_PlayChannelTimed(channel:CInt, chunk:Chunk, loops:CInt, ticks:CInt):CInt = extern

}


object SDL_mixer_extras {
    import SDL_mixer._

    //#define Mix_LoadWAV(file)	Mix_LoadWAV_RW(SDL_RWFromFile(file, "rb"), 1) 
    def Mix_LoadWAV(file:CString):Chunk = Mix_LoadWAV_RW(SDL_RWFromFile(file, c"r"), 1)

    def Mix_PlayChannel(chunk:Chunk, loops:CInt, ticks:CInt):CInt = Mix_PlayChannelTimed(-1, chunk, loops, ticks)

    val AUDIO_U8        = 0x0008  /**< Unsigned 8-bit samples */
    val AUDIO_S8        = 0x8008  /**< Signed 8-bit samples */
    val AUDIO_U16LSB    = 0x0010  /**< Unsigned 16-bit samples */
    val AUDIO_S16LSB    = 0x8010  /**< Signed 16-bit samples */
    val AUDIO_U16MSB    = 0x1010  /**< As above, but big-endian byte order */
    val AUDIO_S16MSB    = 0x9010  /**< As above, but big-endian byte order */
    val AUDIO_U16       = AUDIO_U16LSB
    val AUDIO_S16       = AUDIO_S16LSB
    

    val MIX_CHANNELS = 8
    val MIX_DEFAULT_FREQUENCY = 22050
    val MIX_DEFAULT_FORMAT = AUDIO_S16LSB //AUDIO_S16SYS
    val MIX_DEFAULT_CHANNELS = 2
    val MIX_MAX_VOLUME = 128
    val MIX_CHANNEL_POST = -2
    val MIX_EFFECTSMAXSPEED = c"MIX_EFFECTSMAXSPEED"
}

