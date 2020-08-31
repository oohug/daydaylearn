package com.hug.controller;

import com.hug.BaseTest;
import org.junit.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.Assert.*;

public class MultiApiControllerTest extends BaseTest {


    @Test
    public void hello() throws Exception {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("api-version", "1.1");
        String response = getWithHeader("http://localhost:8080/api/hello", httpHeaders);
        System.out.println(response);
    }

    @Test
    public void hello2() {

    }

    @Test
    public void hello5() {
    }

    @Test
    public void postJsonV1() {
    }

    @Test
    public void postJsonV2() {
    }

    @Test
    public void postJsonV3() {
    }
}