package com.github.crafttogether.chatbridge.utilities;

import com.github.crafttogether.chatbridge.ChatBridge;
import org.json.JSONArray;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Members {
    private final static File folder = ChatBridge.getPlugin().getDataFolder();

    public static JSONArray get() {
        try {
            final File members = new File(folder.getPath() + File.separator + "DMIMembers.json");

            // Config file doesn't exist
            if (!members.exists()) {
                final String content = "[]"; // Create members raw text
                Files.write(Paths.get(members.getPath()), content.getBytes()); // Write content as file
            }

            final StringBuilder output = new StringBuilder();
            final Scanner scanner = new Scanner(new FileReader(members)); // Create scanner from file
            while (scanner.hasNext()) output.append(scanner.next()); // Add all lines to output

            return new JSONArray(output.toString()); // Create JSONObject from output
        } catch (Exception e) {
            return null;
        }
    }

    public static void update(JSONArray json) {
        try {
            final Writer toilet = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(folder.getPath() + File.separator + "DMIMembers.json"), StandardCharsets.UTF_8));
            toilet.write(json.toString());
            toilet.flush();
            toilet.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
