alias r := release
alias dw := debug-watch

release:
    ./gradlew releaseDesktop
    open ~/Desktop

debug-watch ip="192.168.0.105:5555":
    adb connect {{ip}}
