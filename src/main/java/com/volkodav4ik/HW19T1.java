package com.volkodav4ik;

import java.io.File;

public class HW19T1 {

    private static final String MAIN_PATH = "C:\\Users\\Артемий\\IdeaProjects\\Java_Elementary_HW18\\src\\main\\java\\com\\volkodav4ik";

    public static void main(String[] args) {
        File folder = new File(MAIN_PATH);
        getNameAllFileInDir(folder);
    }

    private static void getNameAllFileInDir(File folder) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                getNameAllFileInDir(new File(file.getAbsolutePath()));
            } else {
                System.out.println(file.getName());
            }
        }
    }
}
