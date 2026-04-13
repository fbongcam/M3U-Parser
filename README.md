## Usage
```java
File file = new File(path_to_file)

M3UParser m3uParser = new M3UParser();
m3uParser.parse(file);

M3U m3u = m3uParser.getM3u();
```

The M3U object contains all data.
