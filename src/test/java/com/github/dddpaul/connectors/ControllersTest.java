package com.github.dddpaul.connectors;

import com.github.dddpaul.connectors.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class ControllersTest extends Mockito {

    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testControllerOne() throws Exception {
        mockMvc.perform(get("/one?name=Paul"))
                .andExpect(content().string("ControllerOne says \"Hello, Paul, I'm shared service\""));
        mockMvc.perform(get("/one?name=Павел"))
                .andExpect(content().string("ControllerOne says \"Hello, Павел, I'm shared service\""));
    }

    @Test
    public void testControllerTwo() throws Exception {
        mockMvc.perform(get("/two?name=Paul"))
                .andExpect(content().string("ControllerTwo says \"Hello, Paul, I'm shared service\""));
        mockMvc.perform(get("/two?name=Павел"))
                .andExpect(content().string("ControllerTwo says \"Hello, Павел, I'm shared service\""));
    }
}
