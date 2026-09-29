package com.mukminov.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@Slf4j
@UtilityClass
public class GitHubUrlParser {

    public static String[] extractOwnerAndRepo(String url) {
        try {
            String path = URI.create(url).getPath();

            String[] parts = path.split("/");
            if (parts.length >= 3) {
                return new String[]{parts[1], parts[2].replace(".git", "")};
            }
        } catch (Exception e) {
            log.error("Error parsing GitHub URL: {}", url, e);
        }
        return null;
    }
}