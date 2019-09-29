package com.optimaize.test.task.controllers;

import com.optimaize.test.task.ShtukaApplication;
import com.optimaize.test.task.responses.Responses;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = ShtukaApplication.class
)
@AutoConfigureMockMvc
public class ShtukaControllerTest {
    protected static final String RESPONSE_CODE_JPATH = "$.code";
    protected static final String RESPONSE_MESSAGE_JPATH = "$.message";
    protected static final String RESPONSE_RESULT_JPATH = "$.result";
    @Autowired
    MockMvc mockMvc;

    @Test
    public void testExceptions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/shtuka/save").param("number", "qqq"))
                .andExpect(jsonPath(RESPONSE_CODE_JPATH).value(String.valueOf(Responses.NOT_NUMBER.getCode())))
                .andExpect(jsonPath(RESPONSE_MESSAGE_JPATH).value(String.valueOf(Responses.NOT_NUMBER.getMessage())));

        //check that no result if no any values
        mockMvc.perform(MockMvcRequestBuilders.get("/shtuka/min"))
                .andExpect(jsonPath(RESPONSE_CODE_JPATH).value(String.valueOf(Responses.NO_ANY_INPUT_VALUES.getCode())))
                .andExpect(jsonPath(RESPONSE_MESSAGE_JPATH).value(String.valueOf(Responses.NO_ANY_INPUT_VALUES.getMessage())));
        mockMvc.perform(MockMvcRequestBuilders.get("/shtuka/max"))
                .andExpect(jsonPath(RESPONSE_CODE_JPATH).value(String.valueOf(Responses.NO_ANY_INPUT_VALUES.getCode())))
                .andExpect(jsonPath(RESPONSE_MESSAGE_JPATH).value(String.valueOf(Responses.NO_ANY_INPUT_VALUES.getMessage())));
        mockMvc.perform(MockMvcRequestBuilders.get("/shtuka/avg"))
                .andExpect(jsonPath(RESPONSE_CODE_JPATH).value(String.valueOf(Responses.NO_ANY_INPUT_VALUES.getCode())))
                .andExpect(jsonPath(RESPONSE_MESSAGE_JPATH).value(String.valueOf(Responses.NO_ANY_INPUT_VALUES.getMessage())));
    }

    @Test
    public void testSuccessResults() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/shtuka/save").param("number", "3"))
                .andExpect(jsonPath(RESPONSE_CODE_JPATH).value(String.valueOf(Responses.OK.getCode())))
                .andExpect(jsonPath(RESPONSE_MESSAGE_JPATH).value(String.valueOf(Responses.OK.getMessage())));
        mockMvc.perform(MockMvcRequestBuilders.post("/shtuka/save").param("number", "4"))
                .andExpect(jsonPath(RESPONSE_CODE_JPATH).value(String.valueOf(Responses.OK.getCode())))
                .andExpect(jsonPath(RESPONSE_MESSAGE_JPATH).value(String.valueOf(Responses.OK.getMessage())));

        //check that no result if no any values
        mockMvc.perform(MockMvcRequestBuilders.get("/shtuka/min"))
                .andExpect(jsonPath(RESPONSE_CODE_JPATH).value(String.valueOf(Responses.OK.getCode())))
                .andExpect(jsonPath(RESPONSE_MESSAGE_JPATH).value(String.valueOf(Responses.OK.getMessage())))
                .andExpect(jsonPath(RESPONSE_RESULT_JPATH).value(3));
        mockMvc.perform(MockMvcRequestBuilders.get("/shtuka/max"))
                .andExpect(jsonPath(RESPONSE_CODE_JPATH).value(String.valueOf(Responses.OK.getCode())))
                .andExpect(jsonPath(RESPONSE_MESSAGE_JPATH).value(String.valueOf(Responses.OK.getMessage())))
                .andExpect(jsonPath(RESPONSE_RESULT_JPATH).value(4));
        mockMvc.perform(MockMvcRequestBuilders.get("/shtuka/avg"))
                .andExpect(jsonPath(RESPONSE_CODE_JPATH).value(String.valueOf(Responses.OK.getCode())))
                .andExpect(jsonPath(RESPONSE_MESSAGE_JPATH).value(String.valueOf(Responses.OK.getMessage())))
                .andExpect(jsonPath(RESPONSE_RESULT_JPATH).value(3.5));
    }
}
