# M3U Parser

A Java library for parsing M3U and M3U Extended playlist files.

## Requirements

- Java 8 or newer
- Maven (when building from source)

## Usage

```java
File file = new File("path/to/playlist.m3u");

M3UParser parser = new M3UParser();
parser.parse(file);

M3U playlist = parser.getM3u();
```

## Maven dependency (JitPack)

Add the JitPack repository to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

Then add the library dependency. Replace `0.3.1` with the Git tag you want to use:

```xml
<dependency>
    <groupId>com.github.fbongcam</groupId>
    <artifactId>M3U-Parser</artifactId>
    <version>0.3.1</version>
</dependency>
```

## Build locally

```bash
mvn clean verify
```
