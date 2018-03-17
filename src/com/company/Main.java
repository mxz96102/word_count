package com.company;

import java.io.*;
import java.util.*;

public class Main {

    private static void error(String err) {
        System.out.printf("[ERROR]: %s\n", err);
    }

    /**
     * Read file to string lines.
     * @param filepath {String}
     * @return contents {ArrayList<String>}
     */

    private static ArrayList<String> getFileContents(String filepath) {
        File f = new File(filepath);

        ArrayList<String> contents = new ArrayList<>();

        if(!f.exists() || !f.isFile()){
            error(f.getName() + "file not found");
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
        } catch (IOException e) {
            e.printStackTrace();
        }

        return contents;
    }

    /**
     * read file to word array
     * @param filePath {String}
     * @return words {String[]}
     * @throws IOException
     */
    private static String[] fileToWord(String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        StringBuilder buffer = new StringBuilder();
        String line;
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine();
        while (line != null) {
            buffer.append(line);
            buffer.append(" ");
            line = reader.readLine();
        }
        reader.close();
        is.close();

        return buffer.toString().split("[\n \t]");
    }

    /**
     * build file from file
     * @param banlistFile {String}
     * @return banwordset {Set<String>}
     */
    private static Set<String> buildBanWord(String banlistFile) {
        Set<String> s = new HashSet<>();

        s.add("");

        if(banlistFile == null) {
            return s;
        }

        try {
            String[] words = fileToWord(banlistFile);

            s.addAll(Arrays.asList(words));
        } catch (IOException e) {
            error("ban word list read error");
            e.printStackTrace();
        }

        return s;
    }

    /**
     * Count words (split with " " and ",")
     * @param str {String}
     * @param banWords {Set<String>}
     * @return count {int}
     */
    private static int countWord(String str, Set<String> banWords) {
        int count = 0;
        String[] splitStr = str.split("[ ,]");

        for (String word : splitStr) {
            if (!banWords.contains(word)) {
                count++;
            }
        }

        return count;
    }

    private static class ArgsParser {
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
                        case 'c':
                            this.c = true;
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

    /**
     * count line.
     * @param contents {String[]}
     * @return [codeline, nullline, docline] {int[]}
     */
    private static int[] countline(String[] contents) {
        int[] linecount = {0, 0, 0};

        for (String line : contents) {
            if(line.trim().length() <= 1) {
                linecount[1]++;
            } else {
                if(!line.trim().contains("//")) {
                    linecount[0]++;
                } else if(line.replaceAll(" ", "").indexOf("//") > 1) {
                    linecount[0]++;
                } else if(line.contains("//")) {
                    linecount[2]++;
                }
            }
        }

        return linecount;
    }

    /**
     * count char from contents array
     * @param contents {String[]}
     * @return sum {int}
     */
    private static int sumChar(String[] contents) {
        int sum = 0;

        for (String line : contents) {
            sum += line.length();
        }

        return sum;
    }

    /**
     * confirm file exists or create it
     * @param filepath {String}
     */
    private static void confirmFile(String filepath) {
        File f = new File(filepath);

        try {
            if(!f.exists()) {
                f.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * count file content
     * @param filepath {String}
     * @param ap {ArgsParser}
     */
    private static void countFile(String filepath, ArgsParser ap, PrintWriter out) {
        ArrayList<String> lineList = getFileContents(filepath);
        String[] contents = lineList.toArray(new String[lineList.size()]);

        if(ap.c) {
            out.printf("%s, 字符数: %d\r\n", filepath, sumChar(contents));
        }

        if(ap.w) {
            Set<String> banset = buildBanWord(ap.banPath);
            int wordCount = 0;

            for(String line : contents) {
                System.out.println(line);
                wordCount += countWord(line, banset);
            }

            out.printf("%s, 单词数: %d\r\n", filepath, wordCount);
        }

        if(ap.l) {
            out.printf("%s, 行数: %d\r\n", filepath, contents.length);
        }

        if(ap.a) {
            int[] lineCout = countline(contents);

            out.printf("%s, 代码行/空行/注释行: %d/%d/%d\r\n", filepath, lineCout[0], lineCout[1], lineCout[2]);
        }
    }

    /**
     * find files by path name
     * @param filepath {String}
     * @return filepathlist {String}
     */
    private static String[] findfiles(String filepath, boolean s) {
        File fp = new File(filepath);
        int idx = filepath.lastIndexOf('/');
        String dirpath, filename;
        ArrayList<String> sal = new ArrayList<>();

        if(fp.isDirectory()) {
            for(File f : fp.listFiles()) {
                if(f.isFile()) {
                    sal.add(f.getPath());
                } else if(f.isDirectory()) {
                    sal.addAll(Arrays.asList(findfiles(f.getPath(), s)));
                }
            }
        } else {
            if(idx > 0) {
                dirpath = "./" + filepath.substring(0, idx+1);
                filename = filepath.substring(idx+1, filepath.length());
            } else {
                dirpath = "./";
                filename = filepath;
            }

            if(filename.contains("*")) {
                filename = filename.replaceAll("\\*", "\\\\w*");
            }

            File[] fs = (new File(dirpath)).listFiles();

            assert fs != null;
            for (File f : fs) {
                System.out.println(f.getName().matches(filename));
                if(f.getName().matches(filename) && f.isFile()) {
                    sal.add(dirpath + f.getName());
                    System.out.println(dirpath + f.getName());
                }
            }
        }

        return sal.toArray(new String[sal.size()]);
    }

    public static void main(String[] args) {
        if(args.length == 0) {
            error("please input some argument in command line");
            return;
        }

        ArgsParser ap = (new ArgsParser(args));
        String outPath = "result.txt";

        if(ap.filePath == null) {
            error("lack of file name");
            return;
        }

        if(ap.o) {
            outPath = ap.outPath;
        }

        confirmFile(outPath);

        PrintWriter out = null;
        try {
            out = new PrintWriter(outPath);

            String[] fileList = findfiles(ap.filePath, ap.s);

            for(String f: fileList) {
                System.out.println(f);
                countFile(f, ap, out);
            }

            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
