package com.company;

import java.io.*;
import java.util.*;

public class Main {

    public static void error(String err) {
        System.out.printf("[ERROR]: %s\n", err);
    }

    /**
     * Read file to string lines.
     * @param filepath {String}
     * @return contents {ArrayList<String>}
     */

    public static ArrayList<String> getFileContents(String filepath) {
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

    public static Set<String> buildBanWord(String banlistFile) {
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

    public static class ArgsParser {
        Boolean c = false;
        Boolean w = false;
        Boolean l = false;
        Boolean s = false;
        Boolean a = false;
        Boolean o = false;
        Boolean e = false;
        String filePath;
        String outPath;
        String banPath;

        /**
         * Parse arg into instance
         * @param args {String[]}
         */
        ArgsParser(String[] args) {
            for (int i = 0; i < args.length; i++) {
                System.out.println(args[i].charAt(0) == '-');
                if(args[i].charAt(0) == '-' && args[i].length() < 3) {
                    switch (args[i].charAt(1)) {
                        case 't':
                            this.c = true;
                            break;
                        case 'w':
                            this.w = true;
                            break;
                        case 'l':
                            this.l = true;
                            break;
                        case 's':
                            this.s = true;
                            break;
                        case 'a':
                            this.a = true;
                            break;
                        case 'o':
                            this.o = true;
                            i++;
                            if(i < args.length) {
                                this.outPath = args[i];
                            }
                            break;
                        case 'e':
                            this.e = true;
                            i++;
                            if(i < args.length) {
                                this.banPath = args[i];
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    this.filePath = args[i];
                }
            }
        }

    }

    public static void main(String[] args) {
	// write your code here
        ArgsParser ap = (new ArgsParser(args));
    }
}
