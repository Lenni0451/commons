# commons
A java library with many useful functions and classes.\
The idea is to put similar classes and utils into one subpackage category like `network` or `collections`.\
When there are enough utils in one category, they will be moved into a separate module.
I will keep the package structure, so you only have to add a new module to your project.

## Usage
To use `commons` in your project, you have the following options:
### Gradle
````groovy
repositories {
    mavenCentral()
}

dependencies {
    implementation "net.lenni0451.commons:all:x.x.x"
}
````
Check my [maven server](https://maven.lenni0451.net/#/releases/net/lenni0451/commons) for the latest version.\
Or the [snapshot server](https://maven.lenni0451.net/#/snapshots/net/lenni0451/commons) for the latest snapshot version.

### Maven
````xml
<dependency>
  <groupId>net.lenni0451.commons</groupId>
  <artifactId>all</artifactId>
  <version>x.x.x</version>
</dependency>
````
Check my [maven server](https://maven.lenni0451.net/#/releases/net/lenni0451/commons) for the latest version.\
Or the [snapshot server](https://maven.lenni0451.net/#/snapshots/net/lenni0451/commons) for the latest snapshot version.

### Jar File
To add `commons` as a jar library, you can download the jar file from my [Jenkins Server](https://build.lenni0451.net/job/commons/).
