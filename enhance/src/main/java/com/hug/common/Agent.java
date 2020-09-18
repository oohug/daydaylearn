package com.hug.common;

import java.lang.instrument.Instrumentation;

public class Agent {

    public static void premain(String agentArgs, Instrumentation instrumentation) {

        instrumentation.addTransformer(new ClassFileTransformerDemo());

        System.out.println("test...");
    }

}