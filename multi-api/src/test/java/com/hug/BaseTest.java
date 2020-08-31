package com.hug;

import static org.junit.Assert.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.UnsupportedEncodingException;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MultiApiRun.class)
@ComponentScan(basePackages = {"com.hug"}, excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE)})
//@ActiveProfiles(value = "dev")
@AutoConfigureMockMvc
@Slf4j
abstract public class BaseTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        System.out.println("-----------init----------------");
    }

    @After
    public void cleanUp() {
        System.out.println("-----------结束----------------");
    }


    public String post(String url, String req) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(req))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("100")))
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");
        return response.getContentAsString();
    }

    public String postWithHeader(String url, String req, HttpHeaders httpHeaders) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .headers(httpHeaders)
                .content(req))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(CoreMatchers.containsString("100")))
                .andReturn()
                .getResponse();
        response.setCharacterEncoding("UTF-8");
        return response.getContentAsString();
    }

    public String get(String url) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        return response.getContentAsString();
    }

    public String getWithHeader(String url, HttpHeaders httpHeaders) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .headers(httpHeaders)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        return response.getContentAsString();
    }
}
