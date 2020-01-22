package com.roman.ripp;

import com.google.gson.Gson;
import com.roman.ripp.collab.Credentials;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class Utility {
    public static Credentials ReadCredentials(String filePath) {
        var credentialsFile = new File(filePath);
        if (credentialsFile.exists()) {
            var credentialsFilePath = credentialsFile.getAbsolutePath();
            try {
                var contents = Files.readString(Paths.get(credentialsFilePath));
                return new Gson().fromJson(contents, Credentials.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean StringContainsIgnoreCase(String source, String query)
    {
        return Pattern.compile(Pattern.quote(query), Pattern.CASE_INSENSITIVE)
                .matcher(source)
                .find();
    }
}
