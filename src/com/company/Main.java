package com.company;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void error(String err) {
        System.out.printf("[ERROR]: %s\n", err);
    }

    /**
     * Read file to string lines.
     * @param filepath {String}
     * @return contents {ArrayList<String>}
     */

    public ArrayList<String> getFileContents(String filepath) {
        File f = new File(filepath);

        ArrayList<String> contents = new ArrayList<>();

        if(!f.exists() || !f.isFile()){
            return contents;
        }

        try {
            FileInputStream fileInputStream = new FileInputStream(f);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "utf-8");
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String lineContent = "";

            while ((lineContent = reader.readLine()) != null) {
                contents.add(lineContent);
            }

            fileInputStream.close();
            inputStreamReader.close();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contents;

    }

    public Set<String> buildBanWord(String banlistFile) {
        return null;
    }

    /**
     * Count words (split with " " and ",")
     * @param str {String}
     * @param banWords {Set<String>}
     * @return count {int}
     */
    public static int countWord(String str, Set<String> banWords) {
        int count = 0;
        String[] splitStr = str.split(" |,");

        for (String word : splitStr) {
            if (!banWords.contains(word)) {
                count++;
            }
        }

        return count;
    }

    public static void main(String[] args) {
	// write your code here
    }
}
