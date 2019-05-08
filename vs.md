# scala-native vs kotlin-native

There is a companion project using kotlin-native. What's the difference between the two?

Scala's main attractions are:
* preference for scala semantics
* less feature churn
* faster compile
* intellisense

Kotlins main attractions are:
* cross-platform possibilies are greater
* auto binding interop

Favor the developer or favor the product :(

## scala-native
### pros
* seems more feature stable. everything I've needed has always been available.
* sbt compiles scala-native about 3x faster than konanc-native compiles kotlin-native
* sbt seems simpler to setup and use than konanc-native 
* works with Metals language server (https://scalameta.org/metals/)

### cons
* only runs on mac and linux. I run it on windows wls using X server, so no hardware acceleration.
* no bindings for sdl_mixer

## kotlin-natve
### pros
* available on many platforms - windows, linux, mac, android, wasm

### cons
* almost every release brings breaking changes. 
* many features were not ready at the start of the project such as :

    mathlibrary - cos, tan, random


## both
gdb is relaitively useless for debugging scala-native in VSCode.
scala-native runs in wsl, so there is no communication between ide and debugger

when kotlin-native crashes, so does gdb, so it's about even.

## performance 
I'm averaging the time spent in game.update() per frame.
Scala seems about 10X faster, however both are well below 50% of the frame.

Under stress (shooting lots of ships), scala slows down, but is still about 2x the speed
of kotlin. 
Due to the way that presents, I think this is due to gc.
Kotlin scales better, and has negligible slowdown. 
Tuning the kotlin algorythm could probably move the needle on that 2x to a 1x

## decision
So - Kotlin:

    scales better
    more platforms
    auto-bindings

