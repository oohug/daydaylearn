package com.hug.common.keygen;

public final class KeyGeneratorFactory {
    public static KeyGenerator newInstance(String keyGeneratorClassName) {
        try {
            return (KeyGenerator)Class.forName(keyGeneratorClassName).newInstance();
        } catch (ReflectiveOperationException var2) {
            var2.printStackTrace();
//            throw var2;
            throw new IllegalArgumentException(String.format("Class %s should have public privilege and no argument constructor", keyGeneratorClassName));
        }
    }

    private KeyGeneratorFactory() {
    }
}