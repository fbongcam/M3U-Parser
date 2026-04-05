package com.fb.formats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class M3UParser {
    private M3U m3u;
    private M3U_EXTENDED m3uExtended;
    private boolean isExtended = false;

    public M3UParser() {}

    /**
     * Parse M3U file
     * @param file the m3u file to parse
     */
    public void parse(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        boolean headerRead = false;
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            // Determine if playlist is of extended format or not
            if (!headerRead) {
                if (line.startsWith(M3U.META_TAG.EXTM3U.getTag()) || line.startsWith(M3U.META_TAG.EXTINF.getTag())) {
                    m3uExtended = new M3U_EXTENDED();
                    isExtended = true;
                } else {
                    m3u = new M3U();
                }
                headerRead = true;
            }

            // Make sure line is not empty
            if (line.isEmpty()) {
                continue;
            }

            if (isExtended) {
                for (M3U.META_TAG tag : M3U.META_TAG.values()) {
                    if (line.startsWith(tag.getTag())) {
                        switch (tag) {
                            case PLAYLIST:
                                if (isExtended) {
                                    m3uExtended.setPlaylistName(cleanTagFromLine(M3U.META_TAG.PLAYLIST, line));
                                }
                                else {
                                    m3u.setPlaylistName(cleanTagFromLine(M3U.META_TAG.PLAYLIST, line));
                                }
                                break;
                            case EXTENC:
                                // Process inputString for #EXTENC tag
                                break;
                            case EXTINF:
                                String extInfo = cleanTagFromLine(M3U.META_TAG.EXTINF, line);
                                String[] info = extInfo.split(",");
                                // info[0] = duration
                                m3uExtended.addTrackDuration(Integer.parseInt(info[0]));
                                // Combine string again in case track title contains "," (comma)
                                String track = "";
                                for (int i=1; i < info.length; i++) {
                                    track += info[i];
                                }
                            /* Check track title format
                                Artist - Track title
                                Track title
                             */
                                if (track.matches("^([^-]+)\\s[-]\\s([^-]+)$")) {
                                    // Artist - Track title
                                    String[] artistTrack = track.split("-");
                                    m3uExtended.addArtistName(artistTrack[0]);
                                    m3uExtended.addTrackTitle(artistTrack[1]);
                                }
                                else {
                                    m3uExtended.addArtistName(null);
                                    m3uExtended.addTrackTitle(track);
                                }
                                // Read track file URL
                                line = reader.readLine();
                                m3uExtended.addFilePath(line);
                                break;
                            case EXTGRP:
                                // TODO Process inputString for #EXTGRP tag
                                break;
                            case EXTALB:
                                // TODO Process inputString for #EXTALB tag
                                break;
                            case EXTART:
                                // TODO Process inputString for #EXTART tag
                                break;
                            case EXTGENRE:
                                // TODO Process inputString for #EXTGENRE tag
                                break;
                            case EXTM3A:
                                // TODO Process inputString for #EXTM3A tag
                                break;
                            case EXTBYT:
                                // TODO Process inputString for #EXTBYT tag
                                break;
                            case EXTBIN:
                                // TODO Process inputString for #EXTBIN tag
                                break;
                            case EXTIMG:
                                // TODO Process inputString for #EXTIMG tag
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
            else {
                m3u.addFilePath(line);
            }
        }
        reader.close();
    }

    public M3U getM3U() {
        if (isExtended) {
            return m3uExtended;
        }
        return m3u;
    }

    private String cleanTagFromLine(M3U.META_TAG tag, String line) {
        String tagString = tag.getTag() + ":";
        return line.replace(tagString, "");
    }

}
