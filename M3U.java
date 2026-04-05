package com.fb.formats;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Base class for M3U playlist format
 *
 * Copyright (c) 2024 fbongcam
 * All rights reserved.
 */

public class M3U {
    public static final String TAG = "M3U";
    public static final String TYPE = "M3U";
    public enum META_TAG {
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

        META_TAG(String tag) {
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
    public static final String FILE_EXTENSION = "m3u";
    protected String playlistName;
    protected ArrayList<String> filePaths;

    public M3U() {
        this.filePaths = new ArrayList<>();
    }
    
    /**
     * 
     * @param playlistName Name of the playlist
     */
    public M3U(String playlistName) {
        this.filePaths = new ArrayList<String>();
        this.playlistName = playlistName;
    }
    
    /**
     * 
     * @param playlistName Name of the playlist
     * @param filePaths Paths to each track
     */
    public M3U(String playlistName, ArrayList<String> filePaths) {
        this.playlistName = playlistName;
        this.filePaths = filePaths;
    }

    /**
     * 
     * @param playlistName Name of the playlist
     * @param filePaths Paths to each track
     */
    public M3U(String playlistName, String[] filePaths) {
        this.playlistName = playlistName;
        this.filePaths = new ArrayList<>(Arrays.asList(filePaths));
    }

    public void setFilePaths(ArrayList<String> filePaths) {
        this.filePaths = filePaths;
    }

    public void setFilePaths(String[] filePaths) {
        this.filePaths = new ArrayList<>(Arrays.asList(filePaths));
    }

    public void addFilePath(String path) {
        this.filePaths.add(path);
    }
    
    public String getM3U() {
        String data = "";
        for (int i = 0; i < filePaths.size(); i++)
        {
            data += filePaths.get(i) + "\n\n";
        }
        return data;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }
    
    public String getPlaylistName() {
        return playlistName;
    }

    public ArrayList<String> getFilePaths() {
        return filePaths;
    }
}
