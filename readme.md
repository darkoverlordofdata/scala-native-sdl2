# scala-native

scala native + SDL2

port of fsharp https://github.com/darkoverlordofdata/map-reduce-ecs

immutable ecs:

a frame is the current state of the entity database

each frame, the database is transformed by a map reduce style pipline into a new state.



### Performance
similar to fsharp:


avg per frame
nativescala 0.001558
fsharp      0.001651
ooc         0.002586
nim         0.003331	
vala	    0.003586	
scala-jvm   0.008185	

#### Windows 10 / WLS

asus i7 about 8x faster than inspiron i3
nativescala 0.001558 => 0.000185

switched to new bindings https://github.com/regb/scalanative-graphics-bindings
nativescala 0.001558 => 0.000688


error: XDG_RUNTIME_DIR not set in the environment.
SDL could not initialize! SDL Error: ${SDL_GetError()}

install an XServer (X410, Xming, ...)
DISPLAY=:0.0 ; export DISPLAY
xhost +