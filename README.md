# Equator-Utils

Equator Utils is the sub-library of [Equator.](https://github.com/KrLite/Equator)
This library provides a set of utilities, not only can be used in rendering but also in other common environments.

## Implementation

The easiest way of implementing this library is to use [jitpack.io.](https://jitpack.io/#KrLite/Equator-Utils)

Add the followings to your `build.gradle:`

```groovy
epositories {
	maven { url 'https://jitpack.io' }
}

dependencies {
	implementation include("com.github.KrLite:Equator-Utils:v1.0.4")
}
```

> If you have already implemented [Equator,](https://github.com/KrLite/Equator) it's fine to not use `include` for a slimmer package. In this case, you can use `implementation "com.github.KrLite:Equator-Utils:v1.0.4"` in your gradle.
