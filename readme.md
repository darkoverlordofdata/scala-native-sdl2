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
