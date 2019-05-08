# scala-native

scala native + SDL2

port of fsharp https://github.com/darkoverlordofdata/map-reduce-ecs

immutable ecs:

a frame is the current state of the entity database

each frame, the database is transformed by a map reduce style pipline into a new state.

### Performance

I track the average amount of time per frame to perform update and draw

If that goes over 100% the animation will stutter. Sooner, actually. So anything under 50% is good.


        unstressed                  stressed

c++     ??
s/n     0.0002323s  -  1.39%        0.0011268s     -  7.9%
k/n     0.0022246s  - 13.35%        0.0021262s     - 13.28%
f#      ??



#### VSCode Plugins
* Scala (Metals) - https://github.com/scalameta/metals-vscode
* Scala Syntax (official) - https://github.com/scala/vscode-scala-syntax


switched to new bindings https://github.com/regb/scalanative-graphics-bindings

#### Windows 10 / WLS

install an XServer such as X410 or Xming.

add to .xinitrc:
```
export DISPLAY=127.0.0.1:0.0
xhost +
```

run app in wsl shell (ctrl-`)
```bash
$ target/scala-2.11/example-out
```



1.6390931606292725
2.1621570587158203
2.2136030197143555
2.2756989002227783
2.244440793991089
2.2226333618164063
