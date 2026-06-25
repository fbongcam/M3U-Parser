package com.github.fbongcam.formats.m3u;

enum M3UMetaTag {
    EXTM3U("#EXTM3U"),
    EXTENC("#EXTENC"),
    EXTINF("#EXTINF"),
    PLAYLIST("#PLAYLIST"),
    EXTGRP("#EXTGRP"),
    EXTALB("#EXTALB"),
    EXTART("#EXTART"),
    EXTGENRE("#EXTGENRE"),
    EXTM3A("#EXTM3A"),
    EXTBYT("#EXTBYT"),
    EXTBIN("#EXTBIN"),
    EXTIMG("#EXTIMG");

    private final String tag;

    M3UMetaTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return tag;
    }
}
