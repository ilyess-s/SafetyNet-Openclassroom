package com.example.SafetyNet.controller;

import com.example.SafetyNet.model.MedicalRecords;
import com.example.SafetyNet.service.MedicalRecordService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
public class MedicalRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAddMedicalRecord() throws Exception {
        MedicalRecords record = new MedicalRecords("John", "Boyd", "03/06/1984", Collections.emptyList(), Collections.emptyList());

        mockMvc.perform(post("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isOk())
                .andExpect(content().string("Medical record added"));
    }

    @Test
    void testUpdateMedicalRecord_NotFound() throws Exception {
        when(medicalRecordService.updateRecord(any(MedicalRecords.class))).thenReturn(false);

        MedicalRecords record = new MedicalRecords("Unknown", "User", "01/01/2000", null, null);

        mockMvc.perform(put("/medicalRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(record)))
                .andExpect(status().isNotFound());
    }
}