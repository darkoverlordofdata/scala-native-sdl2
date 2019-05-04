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

switched to new bindings https://github.com/regb/scalanative-graphics-bindings

install an XServer - I'm using X410, and have also tried Xming.

add to .bashrc
    
```        
export DISPLAY=127.0.0.1:0.0
```

on boot:
$ xhost +127.0.0.1

run app in wsl shell (ctrl-`)
```bash
$ target/scala-2.11/example-out
```