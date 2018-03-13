package com.company;

import java.io.*;
import java.util.ArrayList;

public class Main {

    public static void error(String err) {
        System.out.printf("[ERROR]: %s\n", err);
    }

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

    public static void main(String[] args) {
	// write your code here
    }
}
