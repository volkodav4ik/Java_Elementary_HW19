package com.volkodav4ik;

/*
2*) Скачать файл src.zip (программно), разархивировать его и вывести
имена всех файлов в которых встречается строка @FunctionalInterface
https://dl.dropboxusercontent.com/s/5y76skm8f5ged7j/src.zip
*/

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.*;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class HW19T2 {

    private static final String DOWNLOAD_LINK = "https://dl.dropboxusercontent.com/s/5y76skm8f5ged7j/src.zip";
    private static final String DOWNLOAD_PATH = "C:\\Users\\Артемий\\IdeaProjects\\Java_Elementary_HW19";
    private static final String SEARCHING_ANNOTATION = "@FunctionalInterface";
    private static final String OUTPUT_FOLDER = "C:\\Users\\Артемий\\IdeaProjects\\Java_Elementary_HW19\\folderForUnzipFiles";
    private static String downloadedFileFullPath = null;

    public static void main(String[] args) throws Exception {

        downloadedFileFullPath = DOWNLOAD_PATH + DOWNLOAD_LINK.substring(DOWNLOAD_LINK.lastIndexOf("/"));

        File file = new File(downloadedFileFullPath);
        if (!file.exists()) {
            downloadFileFromLink();
        }
        long withUnzipStart = System.currentTimeMillis();
        File folder = new File(OUTPUT_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
        }
        unzipArchieve();
        printFileNameWithAnnotation(folder);
        long withUnzipFinish = System.currentTimeMillis();
        System.out.println("======================================================");
        System.out.println("Time of getting files with unzipping = " + (withUnzipFinish - withUnzipStart) / 1000.0 + " sec");

        // получение того же списка без распаковки
        System.out.println("======================================================");
        long withoutUnzipStart = System.currentTimeMillis();
        printFileNameWithAnnotationWithoutUnzipping();
        long withoutUnzipFinish = System.currentTimeMillis();
        System.out.println("======================================================");
        System.out.println("Time of getting files without unzipping = " + (withoutUnzipFinish - withoutUnzipStart) / 1000.0 + " sec");

    }

    private static void printFileNameWithAnnotation(File folder) {
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                printFileNameWithAnnotation(new File(file.getAbsolutePath()));
            } else {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        if (line.contains(SEARCHING_ANNOTATION)) {
                            System.out.println(file.getName().substring(file.getName().lastIndexOf("/") + 1));
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Something went wrong. Please, contact to your Programmer)");
                }
            }
        }
    }

    private static void unzipArchieve() throws IOException {
        try {
            byte[] buffer = new byte[1024];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(downloadedFileFullPath));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(OUTPUT_FOLDER + File.separator + fileName);
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            System.out.println("Unzip done!");
        } catch (IOException e) {
            System.out.println("Something went wrong. Please, contact to your Programmer)");
        }
    }


    private static void printFileNameWithAnnotationWithoutUnzipping() throws IOException {
        ZipFile zipFile = new ZipFile(downloadedFileFullPath);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            InputStream stream = zipFile.getInputStream(entry);
            Scanner s = new Scanner(stream).useDelimiter("\\A");
            String result = s.hasNext() ? s.next() : "";
            if (result.contains(SEARCHING_ANNOTATION)) {
                System.out.println(entry.getName().substring(entry.getName().lastIndexOf("/") + 1));
            }
        }
    }

    public static void downloadFileFromLink() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(DOWNLOAD_LINK).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Failed to download file: " + response);
        }
        FileOutputStream fos = new FileOutputStream(downloadedFileFullPath);
        fos.write(response.body().bytes());
        fos.close();
    }

}
