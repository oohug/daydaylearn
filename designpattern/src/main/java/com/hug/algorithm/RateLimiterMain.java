package com.hug.algorithm;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterMain {

    public static void main(String[] args) {

        final RateLimiter rateLimiter = RateLimiter.create(10);

        for (int i = 0; i < 10; i++) {

            new Thread(new Runnable() {

                @Override

                public void run() {

                    rateLimiter.acquire();

                    System.out.println("pass");

                }

            }).start();

        }

    }

}