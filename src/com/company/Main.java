package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Set;
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

    }

    public int countWord(String str, Set<String> banWords) {
        Matcher wordMatcher = Pattern.compile("\\b([a-z]\\w+[a-zA-Z]){1}\\b").matcher(str);
        int count = 0;

        if(banWords.size() == 0) {
            while(wordMatcher.find()) {
                count++;
            }
        } else {
            while(wordMatcher.find()) {
                if(!banWords.contains(wordMatcher.group(0))) count++;
            }
        }

        return count;

    }

    public static void main(String[] args) {
	// write your code here
    }
}
