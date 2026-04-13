package com.github.fbongcam.formats.m3u;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class M3UParser {
    private final String TAG = "M3UParser";
    private M3U m3u;
    private M3U_EXTENDED m3uExtended;
    private boolean isExtended = false;
    private static final Pattern m3uTagPattern = Pattern.compile("^#[A-Z0-9]+:.*$");
    private static final Pattern durationPattern = Pattern.compile("^([0-9]*),");

    private final static class MAP_KEYS {
        public static final String TAG = "tag";
        public static final String DATA = "data";
    }

    public M3UParser() {}

    /**
     * Parse M3U file
     *
     * @param file the m3u file to parse
     */
    public void parse(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = reader.readLine();
        line = line.trim();

        // Determine if playlist is of extended format or not
        if (line.startsWith(M3U.META_TAG.EXTM3U.getTag()) || line.startsWith(M3U.META_TAG.EXTINF.getTag())) {
            m3uExtended = new M3U_EXTENDED();
            isExtended = true;
        }
        else {
            m3u = new M3U();
            isExtended = false;
        }

        while ((line = reader.readLine()) != null) {
            // Make sure line is not empty
            if (line.isEmpty()) {
                continue;
            }

            // Process common tags
            boolean result = processCommonTags(line, reader);

            if (!result) {
                // Check for custom M3U tags
                if (isExtended && line.startsWith("#")) {
                    String tag_;
                    String data;
                    Matcher matcher = m3uTagPattern.matcher(line);
                    if (matcher.matches()) {
                        String[] array = line.split(":", 2);
                        tag_ = array[0];
                        data = array[1];
                        // Clean the tag
                        tag_ = tag_.replace("#", "");
                        m3uExtended.setCustomComment(tag_, data);
                    }
                }
            }

            // Just get file paths if not extended m3u
            if (!isExtended && !line.startsWith("#")) {
                // If line isn't empty, and it doesn't start with "#"
                // then it's a file path (most certainly)
                m3u.addFilePath(line);
            }
        }

        reader.close();
        System.out.println(String.format("===%s===\nParsed: %s", TAG, m3u == null ? m3uExtended.toString() : m3u.toString()));
    }

    private boolean processCommonTags(String line, BufferedReader reader) throws IOException {
        for (M3U.META_TAG tag : M3U.META_TAG.values()) {
            if (line.startsWith(tag.getTag())) {
                HashMap<String, String> map = m3uTagToMap(line);
                String dataString = map.getOrDefault(MAP_KEYS.DATA, "");

                switch (tag) {
                    case PLAYLIST:
                        //============== PLAYLIST ==============
                        if (isExtended) {
                            m3uExtended.setPlaylistName(dataString);
                        } else {
                            m3u.setPlaylistName(dataString);
                        }
                        return true;
                    case EXTENC:
                        //============== EXTENC ==============
                        // TODO Process inputString for #EXTENC tag
                        return true;
                    case EXTINF:
                        //============== EXTINF ==============

                        // EXTINF data
                        // duration, artist - track

                        assert dataString != null;
                        if (!dataString.isEmpty()) {
                            Integer duration = extractDuration(dataString);
                            m3uExtended.addTrackDuration(duration);

                            // Check track title format
                            String[] extinf_ = dataString.split(durationPattern.pattern(), 2);
                            if (extinf_.length > 1) {
                                String extinf_artistTrack = extinf_[1];
                                if (extinf_artistTrack.matches("^(.+?)\\s-\\s(.+)$")) {
                                    // Artist - Track title
                                    String[] artistTrack = extinf_artistTrack.split("\\s-\\s");
                                    m3uExtended.addArtistName(artistTrack[0]);
                                    m3uExtended.addTrackTitle(artistTrack[1]);
                                } else {
                                    // Track title
                                    System.out.println(TAG + ": Artist missing for track " + extinf_artistTrack);
                                    m3uExtended.addArtistName(null);
                                    m3uExtended.addTrackTitle(extinf_artistTrack);
                                }
                            }
                        }

                        // Get file URL
                        line = reader.readLine();
                        m3uExtended.addFilePath(line.trim());
                        return true;
                    case EXTGRP:
                        //============== EXTGRP ==============
                        // TODO Process inputString for #EXTGRP tag
                        return true;
                    case EXTALB:
                        //============== EXTALB ==============
                        // TODO Process inputString for #EXTALB tag
                        return true;
                    case EXTART:
                        //============== EXTART ==============
                        // TODO Process inputString for #EXTART tag
                        return true;
                    case EXTGENRE:
                        //============== EXTGENRE ==============
                        // TODO Process inputString for #EXTGENRE tag
                        return true;
                    case EXTM3A:
                        //============== EXTM3A ==============
                        // TODO Process inputString for #EXTM3A tag
                        return true;
                    case EXTBYT:
                        //============== EXTBYT ==============
                        // TODO Process inputString for #EXTBYT tag
                        return true;
                    case EXTBIN:
                        //============== EXTBIN ==============
                        // TODO Process inputString for #EXTBIN tag
                        return true;
                    case EXTIMG:
                        //============== EXTIMG ==============
                        // TODO Process inputString for #EXTIMG tag
                        return true;
                }
            }
        }
        return false;
    }

    private HashMap<String, String> m3uTagToMap(String line) {
        HashMap<String, String> map = new HashMap<>();
        Matcher matcher = m3uTagPattern.matcher(line);
        if (matcher.matches()) {
            String[] array = line.split(":", 2);
            map.put(MAP_KEYS.TAG, array[0]);
            map.put(MAP_KEYS.DATA, array[1]);
        }
        return map;
    }

    private Integer extractDuration(String string) {
        Matcher matcher = durationPattern.matcher(string);
        if (matcher.find()) {
            String durationString = matcher.group(1);
            assert durationString != null;
            return Integer.parseInt(durationString);
        }
        return null;
    }

    /**
     * Get M3U object
     *
     * @return M3U or M3UExtended
     */
    public M3U getM3u() {
        if (isExtended) {
            return m3uExtended;
        }
        return m3u;
    }
}
