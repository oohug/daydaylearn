package com.hug.agent;

import java.lang.instrument.Instrumentation;

public class MyAgent {
    public static void premain(String args, Instrumentation instrumentation) {
        System.out.println("Hi, I'm agent!");
        instrumentation.addTransformer(new MyTransformer());
    }
}