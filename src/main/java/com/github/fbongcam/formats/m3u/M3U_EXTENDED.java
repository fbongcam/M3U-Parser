package com.github.fbongcam.formats.m3u;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Extended format of M3U, extended from base M3U class.
 * <p>
 * Copyright (c) 2024 fbongcam
 * All rights reserved.
 */

public class M3U_EXTENDED extends M3U {
    public static final String TYPE = "M3U_EXTENDED";
    private ArrayList<String> artistNames = new ArrayList<String>();
    private ArrayList<String> trackTitles = new ArrayList<String>();
    private ArrayList<Integer> trackDurations = new ArrayList<Integer>();
    private LinkedHashMap<String, String> customTags = new LinkedHashMap<String, String>();

    public M3U_EXTENDED() {
    }

    public M3U_EXTENDED(String playlistName) {
        super(playlistName);
    }

    public M3U_EXTENDED(String playlistName, ArrayList<String> filePaths) {
        super(playlistName, filePaths);
    }

    public M3U_EXTENDED(String playlistName, String[] filePaths) {
        super(playlistName, filePaths);
    }

    /**
     * Constructor
     *
     * @param playlistName   Name of the playlist
     * @param artistNames    Name of the artist
     * @param trackTitles    Title of the track
     * @param trackDurations Total track duration (in seconds)
     */
    public M3U_EXTENDED(
            String playlistName,
            ArrayList<String> artistNames,
            ArrayList<String> trackTitles,
            ArrayList<Integer> trackDurations,
            ArrayList<String> filePaths) {
        super(playlistName, filePaths);
        this.artistNames = artistNames;
        this.trackTitles = trackTitles;
        this.trackDurations = trackDurations;
    }

    /**
     * Constructor
     *
     * @param playlistName   Name of the playlist
     * @param artistNames    Name of the artists
     * @param trackTitles    Title of the tracks
     * @param trackDurations Track durations (in seconds)
     * @param filePaths      Paths to files
     */
    public M3U_EXTENDED(
            String playlistName,
            String[] artistNames,
            String[] trackTitles,
            Integer[] trackDurations,
            String[] filePaths) {
        super(playlistName, filePaths);
        this.artistNames = new ArrayList<>(Arrays.asList(artistNames));
        this.trackTitles = new ArrayList<>(Arrays.asList(trackTitles));
        this.trackDurations = new ArrayList<>(Arrays.asList(trackDurations));
    }

    @Override
    public String getM3U() {
        // Check for non-empty data
        if (artistNames.isEmpty() ||
                trackTitles.isEmpty() ||
                trackDurations.isEmpty() ||
                super.filePaths.isEmpty()) {
            System.out.println(TAG + ": Data is missing");
            return null;
        }

        // Check for any missing data
        if (artistNames.size() != super.filePaths.size()) {
            System.out.println(TAG + ": Artist Name data is missing");
        }
        if (trackTitles.size() != super.filePaths.size()) {
            System.out.println(TAG + ": Track Title data is missing");
        }
        if (trackDurations.size() != super.filePaths.size()) {
            System.out.println(TAG + ": Track Duration data is missing");
        }

        // Start with M3U Extended header
        String data = META_TAG.EXTM3U.getTag();

        // Add custom tags
        if (!customTags.isEmpty()) {
            // Add custom tags
            for (Map.Entry<String, String> entry : customTags.entrySet()) {
                data += String.format("\n#%s:%s", entry.getKey().toUpperCase(), entry.getValue());
            }
        }

        // New empty line
        data += "\n\n";

        // Add Playlist Name
        data += META_TAG.PLAYLIST.getTag() + ":" + super.playlistName;

        // New empty line
        data += "\n\n";

        for (int i = 0; i < super.filePaths.size(); i++) {
            // Extended Info indicator
            data += META_TAG.EXTINF.getTag() + ":";

            // Add Track Duration
            if (trackDurations.get(i) == 0 || trackDurations.get(i) == null) {
                data += "1,";
            } else {
                data += trackDurations.get(i) + ",";
            }

            // Add Artist Name
            try {
                if (artistNames.get(i) == null) {
                    data += "Artist Unknown";
                } else {
                    data += artistNames.get(i);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            data += " - ";

            // Add Track Title
            try {
                if (trackTitles.get(i) == null) {
                    data += "Track Unknown";
                } else {
                    data += trackTitles.get(i);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            // New line
            data += "\n";

            // Add file path
            data += super.filePaths.get(i);

            // New item (empty line)
            data += "\n\n";
        }

        return data;
    }

    /*
     * -----------------------
     *  SETTERS
     * -----------------------
     */

    public void setArtistNames(ArrayList<String> artistNames) {
        this.artistNames = artistNames;
    }

    public void setTrackTitles(ArrayList<String> trackTitles) {
        this.trackTitles = trackTitles;
    }

    public void setTrackDurations(ArrayList<Integer> trackDurations) {
        this.trackDurations = trackDurations;
    }

    /**
     * Adds track duration
     *
     * @param duration Duration has to be specified in seconds
     */
    public void addTrackDuration(Integer duration) {
        trackDurations.add(duration);
    }

    public void addArtistName(String artistName) {
        artistNames.add(artistName);
    }

    public void addTrackTitle(String trackTitle) {
        trackTitles.add(trackTitle);
    }

    public void setCustomComment(String tag, String data) {
        this.customTags.put(tag, data);
    }

    /*
     * -----------------------
     *  GETTERS
     * -----------------------
     */

    public String[] getTrackInfo(int index) {
        return new String[]{
                trackDurations.get(index).toString(),
                artistNames.get(index),
                trackTitles.get(index)
        };
    }

    public Integer getTrackDuration(int index) {
        return trackDurations.get(index);
    }

    public String getArtistName(int index) {
        return artistNames.get(index);
    }

    public ArrayList<String> getArtistNames() {
        return artistNames;
    }

    public String getTrackTitle(int index) {
        return trackTitles.get(index);
    }

    public ArrayList<String> getTrackTitles() {
        return trackTitles;
    }

    public Map<String, String> getCustomTags() {
        return this.customTags;
    }

    @Override
    public String toString() {
        return "M3U_EXTENDED [artistNames=" + artistNames + ", trackTitles=" + trackTitles + ", trackDurations="
                + trackDurations + ", customTags=" + customTags + ", playlistName=" + playlistName + ", filePaths="
                + filePaths + "]";
    }

}
