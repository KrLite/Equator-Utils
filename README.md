# Equator-Utils

**Equator Utils** is the sub-library of **[Equator.](https://github.com/KrLite/Equator)**
This library provides a set of utilities, not only can be used in rendering but also in other common environments.

## Implementation

The easiest way of implementing this library is to use **[JitPack.](https://jitpack.io/#KrLite/Equator-Utils)**

Add the followings to your `build.gradle:`

```groovy
repositories {
	maven { url 'https://jitpack.io' }
}

dependencies {
	implementation include("com.github.KrLite:Equator-Utils:xxx")
}
```

> The `xxx` above should be replaced with the latest [`tag name`](https://github.com/KrLite/Equator-Utils/tags) of Equator Utils.
>
> If you have already implemented **[Equator,](https://github.com/KrLite/Equator)** you can use `implementation "com.github.KrLite:Equator-Utils:xxx"` instead of including the lib.
