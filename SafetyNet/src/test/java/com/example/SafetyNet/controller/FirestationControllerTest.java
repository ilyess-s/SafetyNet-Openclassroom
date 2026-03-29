package com.example.SafetyNet.controller;

import com.example.SafetyNet.model.Firestations;
import com.example.SafetyNet.service.FirestationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FirestationController.class)
public class FirestationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FirestationService firestationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetPersonsByStation() throws Exception {
        when(firestationService.getPersonsByStation(3)).thenReturn(new HashMap<>());

        mockMvc.perform(get("/firestation").param("stationNumber", "3"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddFirestation() throws Exception {
        Firestations station = new Firestations("1509 Culver St", 3);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(station)))
                .andExpect(status().isOk())
                .andExpect(content().string("Firestation added"));
    }

    @Test
    void testDeleteFirestation_NotFound() throws Exception {
        when(firestationService.deleteFirestation("Unknown Address")).thenReturn(false);

        mockMvc.perform(delete("/firestation").param("address", "Unknown Address"))
                .andExpect(status().isNotFound());
    }
}