package com.hug.ali.test1;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.IOException;

/**
 * IO基础考察：题目一
 */
public class CharsPrint {

    public static void main(String[] args) {

        System.out.println("请在控制台输入字符串:");
        try (BufferedReader br = new BufferedReader(new FileReader(FileDescriptor.in));) {
            while (true) {
                String text = br.readLine();
                System.out.println(text);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}