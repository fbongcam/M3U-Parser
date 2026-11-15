# M3U Parser

A Java library for parsing M3U and M3U Extended playlist files.

## Requirements

- Java 8 or newer
- Maven or Gradle (when building from source)

## Usage

```java
File file = new File("path/to/playlist.m3u");

M3UParser parser = new M3UParser();
parser.parse(file);

M3U playlist = parser.getM3u();
```

## Installation

The library is available through [JitPack](https://jitpack.io/). Replace `0.3.3` in the examples below with the Git tag you want to use.

### Maven

Add the JitPack repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then add the dependency:

```xml
<dependency>
    <groupId>com.github.fbongcam</groupId>
    <artifactId>M3U-Parser</artifactId>
    <version>0.3.3</version>
</dependency>
```

### Gradle (Groovy DSL)

Add the JitPack repository in your `build.gradle`:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

Then add the dependency:

```groovy
dependencies {
    implementation 'com.github.fbongcam:M3U-Parser:0.3.3'
}
```

### Gradle (Kotlin DSL)

If your project uses `build.gradle.kts`, add:

```kotlin
repositories {
    maven(url = "https://jitpack.io")
}
```

Then add the dependency:

```kotlin
dependencies {
    implementation("com.github.fbongcam:M3U-Parser:0.3.3")
}
```

## Build locally

With Maven:

```bash
mvn clean verify
```

With Gradle:

```bash
gradle clean build
```

The project supports both build systems. JitPack detects `build.gradle` and uses Gradle to build tagged releases.
