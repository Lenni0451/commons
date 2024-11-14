# commons
A java library with many useful functions and classes.\
The idea is to put similar classes and utils into one subpackage category like `network` or `collections`.\
When there are enough utils in one category, they will be moved into a separate module. If a part of a module is moved into a separate module, the previous module will depend on the new module to not break existing code.

## Usage
To use `commons` using [Gradle](#gradle)/[Maven](#maven) you can add it as a dependency to your project.\
The latest version can be found in the [releases](https://github.com/Lenni0451/commons/releases).\
My libraries are hosted on [Maven Central](https://mvnrepository.com/artifact/net.lenni0451.commons) or my own [maven server](https://maven.lenni0451.net/).

### Gradle
```groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "net.lenni0451.commons:core:x.x.x"
}
```

### Maven
```xml
<dependency>
  <groupId>net.lenni0451.commons</groupId>
  <artifactId>core</artifactId>
  <version>x.x.x</version>
</dependency>
```

## Modules
The name of the module is the name of the maven artifact and the subpackage.\
All modules which depend on external libraries require the library to be added manually to your project.
This is done to allow you to choose the version of the library you want to use (only works if the version is ABI compatible).
| Module        | Description                                                                     | Dependencies     |
| ------------- | ------------------------------------------------------------------------------- | ---------------- |
| `animation`   | Classes and utils for creating simple animations using easing functions.        | -                |
| `asm`         | General purpose utilities for working with ASM.                                 | ObjectWeb ASM    |
| `brigadier`   | Extras for making brigadier nicer to work with.                                 | Mojang brigadier |
| `collections` | Additional collections and utilities for working with all kinds of collections. | -                |
| `core`        | The core module with many useful classes and utilities.                         | `collections`    |
| `debugging`   | Basics for debugging and profiling.                                             | -                |
| `functional`  | Functional interfaces for common use cases.                                     | -                |
| `httpclient`  | A http client which wraps around other client apis.                             | -                |
| `netty`       | Handlers and client/server implementations using netty.                         | Netty            |
| `swing`       | Utilities making creating UIs with swing easier.                                | -                |

Special modules:
| Module     | Description                                      |
| ---------- | ------------------------------------------------ |
| `all`      | A module which includes all other modules.       |
| ~~`main`~~ | __Deprecated__ Only for backwards compatibility. |
