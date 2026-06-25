package com.github.fbongcam.formats.m3u;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * M3U playlist format model.
 * <p>
 * Copyright (c) 2024 fbongcam
 * All rights reserved.
 */
public class M3U {
    public static final String TAG = "M3U";
    public static final String FILE_EXTENSION = "m3u";

    private File file;
    private String name;
    private String ID;
    private String displayTitle = "";
    private ArrayList<String> filePaths = new ArrayList<String>();
    private ArrayList<String> artistNames = new ArrayList<String>();
    private ArrayList<String> trackTitles = new ArrayList<String>();
    private ArrayList<Integer> trackDurations = new ArrayList<Integer>();
    private LinkedHashMap<String, String> customTags = new LinkedHashMap<String, String>();

    /**
     * Creates an empty M3U playlist.
     */
    public M3U() {
        this.ID = UUID.randomUUID().toString();
    }

    public M3U(ArrayList<String> filePaths) {
        this();
        this.filePaths = filePaths;
    }

    public M3U(String[] filePaths) {
        this();
        this.filePaths = new ArrayList<>(Arrays.asList(filePaths));
    }

    /**
     * Creates an M3U playlist with a name and file paths.
     *
     * @param displayTitle title of the playlist
     * @param filePaths    list of file paths for the playlist
     */
    public M3U(String displayTitle, ArrayList<String> filePaths) {
        this();
        this.displayTitle = displayTitle;
        this.filePaths = filePaths;
    }

    /**
     * Creates an M3U playlist with a name and file paths.
     *
     * @param displayTitle title of the playlist
     * @param filePaths    array of file paths for the playlist
     */
    public M3U(String displayTitle, String[] filePaths) {
        this();
        this.displayTitle = displayTitle;
        this.filePaths = new ArrayList<>(Arrays.asList(filePaths));
    }

    /**
     * Creates an M3U playlist with metadata and file paths.
     *
     * @param displayTitle   title of the playlist
     * @param artistNames    list of artist names
     * @param trackTitles    list of track titles
     * @param trackDurations list of track durations in seconds
     * @param filePaths      list of file paths
     */
    public M3U(
            String displayTitle,
            ArrayList<String> artistNames,
            ArrayList<String> trackTitles,
            ArrayList<Integer> trackDurations,
            ArrayList<String> filePaths) {
        this();
        this.displayTitle = displayTitle;
        this.filePaths = filePaths;
        this.artistNames = artistNames;
        this.trackTitles = trackTitles;
        this.trackDurations = trackDurations;
    }

    /**
     * Creates an M3U playlist with metadata and file paths.
     *
     * @param displayTitle   title of the playlist
     * @param artistNames    array of artist names
     * @param trackTitles    array of track titles
     * @param trackDurations array of track durations in seconds
     * @param filePaths      array of file paths
     */
    public M3U(
            String displayTitle,
            String[] artistNames,
            String[] trackTitles,
            Integer[] trackDurations,
            String[] filePaths) {
        this();
        this.displayTitle = displayTitle;
        this.filePaths = new ArrayList<>(Arrays.asList(filePaths));
        this.artistNames = new ArrayList<>(Arrays.asList(artistNames));
        this.trackTitles = new ArrayList<>(Arrays.asList(trackTitles));
        this.trackDurations = new ArrayList<>(Arrays.asList(trackDurations));
    }

    /**
     * Generates the M3U playlist content.
     *
     * @return serialized M3U content, or null if required data is missing
     */
    public String getM3UasString() {

        if (isAnyVariableEmpty()) {
            // If file paths are available continue with only file paths
            if (!filePaths.isEmpty()) {
                // Construct basic m3u with file paths only
                String data = "";
                for (int i = 0; i < filePaths.size(); i++) {
                    data += filePaths.get(i) + "\n\n";
                }
                return data;
            } else {
                System.out.println(TAG + ": Data is missing");
                return null;
            }
        }

        // Check if items are missing any data
        if (areTrackListsSameLength()) {
            System.out.println(TAG + ": Data is missing");
            return null;
        }

        // Start with M3U Extended header
        String data = M3UMetaTag.EXTM3U.getTag();

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
        data += M3UMetaTag.PLAYLIST.getTag() + ":" + displayTitle;

        // New empty line
        data += "\n\n";

        for (int i = 0; i < filePaths.size(); i++) {
            // Extended Info indicator
            data += M3UMetaTag.EXTINF.getTag() + ":";

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
            data += filePaths.get(i);

            // New item (empty line)
            data += "\n\n";
        }

        return data;
    }

    public void addFilePath(String path) {
        this.filePaths.add(path);
    }

    private boolean areTrackListsSameLength() {
        int expectedSize = filePaths.size();
        return Stream.of(artistNames, trackTitles, trackDurations)
                .mapToInt(Collection::size)
                .allMatch(size -> size == expectedSize);
    }

    private boolean isAnyVariableEmpty() {
        return Stream.of(
                displayTitle,
                filePaths,
                artistNames,
                trackTitles,
                trackDurations,
                customTags
        // Easily add new variables here in the future
        ).anyMatch(this::isEmpty);
    }

    private boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return ((String) obj).isBlank();
        }
        if (obj instanceof Collection<?>) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map<?, ?>) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        return false;
    }

    /*
     * -----------------------
     * SETTERS
     * -----------------------
     */

    public void setFile(File file) {
        this.file = file;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFilePaths(ArrayList<String> filePaths) {
        this.filePaths = filePaths;
    }

    public void setFilePaths(String[] filePaths) {
        this.filePaths = new ArrayList<>(Arrays.asList(filePaths));
    }

    public void setDisplayTitle(String title) {
        this.displayTitle = title;
    }

    /**
     * Sets the list of artist names for this playlist.
     *
     * @param artistNames artist names for each track
     */
    public void setArtistNames(ArrayList<String> artistNames) {
        this.artistNames = artistNames;
    }

    /**
     * Sets the list of track titles for this playlist.
     *
     * @param trackTitles titles for each track
     */
    public void setTrackTitles(ArrayList<String> trackTitles) {
        this.trackTitles = trackTitles;
    }

    /**
     * Sets the list of track durations for this playlist.
     *
     * @param trackDurations durations in seconds for each track
     */
    public void setTrackDurations(ArrayList<Integer> trackDurations) {
        this.trackDurations = trackDurations;
    }

    /**
     * Adds a single track duration to the playlist.
     *
     * @param duration duration in seconds
     */
    public void addTrackDuration(Integer duration) {
        trackDurations.add(duration);
    }

    /**
     * Adds a single artist name to the playlist.
     *
     * @param artistName artist name for the next track
     */
    public void addArtistName(String artistName) {
        artistNames.add(artistName);
    }

    /**
     * Adds a single track title to the playlist.
     *
     * @param trackTitle track title for the next track
     */
    public void addTrackTitle(String trackTitle) {
        trackTitles.add(trackTitle);
    }

    /**
     * Adds or updates a custom tag in the playlist.
     *
     * @param tag  custom tag name
     * @param data custom tag value
     */
    public void setCustomTag(String tag, String data) {
        this.customTags.put(tag, data);
    }

    /*
     * -----------------------
     * GETTERS
     * -----------------------
     */

    /**
     * Returns all file paths in the playlist.
     *
     * @return list of file paths
     */
    public ArrayList<String> getFilePaths() {
        return filePaths;
    }

    /**
     * Returns playlist display title
     *
     * @return playlist title
     */
    public String getDisplayTitle() {
        return displayTitle;
    }

    /**
     * Returns all track durations in the playlist.
     *
     * @return list of track durations
     */
    public ArrayList<Integer> getTrackDurations() {
        return trackDurations;
    }

    /**
     * Returns all artist names in the playlist.
     *
     * @return list of artist names
     */
    public ArrayList<String> getArtistNames() {
        return artistNames;
    }

    /**
     * Returns all track titles in the playlist.
     *
     * @return list of track titles
     */
    public ArrayList<String> getTrackTitles() {
        return trackTitles;
    }

    /**
     * Returns all custom tags stored in the playlist.
     *
     * @return map of custom tag names and values
     */
    public Map<String, String> getCustomTags() {
        return this.customTags;
    }

    public File getFile() {
        return this.file;
    }

    public String getName() {
        return this.name;
    }

    public String getID() {
        return this.ID;
    }

    @Override
    public String toString() {
        return "M3U [file=" + file + ", name=" + name + ", ID=" + ID + ", displayTitle=" + displayTitle + ", filePaths="
                + filePaths + ", artistNames=" + artistNames + ", trackTitles=" + trackTitles + ", trackDurations="
                + trackDurations + ", customTags=" + customTags + "]";
    }

}
