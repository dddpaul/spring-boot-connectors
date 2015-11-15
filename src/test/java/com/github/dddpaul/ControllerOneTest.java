package com.github.dddpaul;

import com.github.dddpaul.Application.ServiceOneConfiguration.ControllerOne;
import com.github.dddpaul.Application.SharedService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
public class ControllerOneTest extends Mockito {

    @Mock
    SharedService service;

    @InjectMocks
    ControllerOne controller;

    MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void test1() throws Exception {
        when(service.getMessage("Paul")).thenReturn("Hello, Paul, I'm shared service");
        mockMvc.perform(get("/one?name=Paul"))
                .andExpect(content().string("ControllerOne says \"Hello, Paul, I'm shared service\""));
    }

    @Test
    public void test2() throws Exception {
        when(service.getMessage("Павел")).thenReturn("Hello, Павел, I'm shared service");
        mockMvc.perform(get("/one?name=Павел"))
                .andExpect(content().string("ControllerOne says \"Hello, Павел, I'm shared service\""));
    }
}
