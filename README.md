# ViaNBT

ViaNBT is a library for dealing with [NBT](https://minecraft.wiki/w/NBT_format) and SNBT.

This project is derived from an earlier version of [OpenNBT](https://github.com/GeyserMC/OpenNBT/) and contains various fundamental improvements and changes to it, including:

* Most notably, move the tag name out the of tags themselves
* `SNBT` for string serialization[^1]
* Add primitive getter methods to number types
* Don't wrap values given in Tag#setValue / Tag constructors
* NumberTag and NumberArrayTag interfaces for easier number handling
* Don't use reflection when creating tag instances
* Directly use value in copy(), also replacing clone()
* Implement tag specific equals() methods
* Update to Java 8
* A bunch of other small improvements and fixes

[^1]: Includes code from [adventure](https://github.com/KyoriPowered/adventure) used for SNBT serialization. It works well enough,
but if you need more accurate SNBT parsing across different Minecraft versions,
you can use [mcstructs](https://github.com/Lenni0451/MCStructs),
or [Via's version of it using ViaNBT](https://github.com/ViaVersion/mcstructs-viafier).

## Dependency

**Maven:**

```xml
<repository>
    <id>viaversion-repo</id>
    <url>https://repo.viaversion.com</url>
</repository>
```

```xml
<dependency>
    <groupId>com.viaversion</groupId>
    <artifactId>nbt</artifactId>
    <version>5.0.0</version>
</dependency>
```

**Gradle:**

```kotlin
repositories {
    maven("https://repo.viaversion.com")
}

dependencies {
    implementation("com.viaversion:nbt:5.0.0")
}
```

## Building

Run `mvn install` in the source's directory via Maven.

## License

ViaNBT is licensed under the **[MIT license](http://www.opensource.org/licenses/mit-license.html)**.
