package com.hug.ali.test1;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.IOException;

public class BufferedPrint {

    public static void main(String[] args) {

        try (BufferedReader br = new BufferedReader(new FileReader(FileDescriptor.in));) {
            while (true) {
                String text = br.readLine() + "\r";
                System.out.println(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}