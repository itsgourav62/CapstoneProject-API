package com.capstone.qwikpay.controller;

import com.capstone.qwikpay.entities.Bill;
import com.capstone.qwikpay.services.BillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BillControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BillService billService;

    @InjectMocks
    private BillController billController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(billController).build();
    }

    // 1. Test Create Bill
    @Test
    void testCreateBill_Success() throws Exception {
        Bill mockBill = new Bill();
        mockBill.setAmount(100);
        mockBill.setDescription("New bill");

        when(billService.createBill(any(Bill.class))).thenReturn(mockBill);

        mockMvc.perform(post("/api/bills/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 100.0, \"description\": \"New bill\"}"))
                .andExpect(status().isOk())  // Changed from CREATED to OK
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.description").value("New bill"));
    }

    // 2. Test Get Bill by ID
    @Test
    void testGetBillById_Success() throws Exception {
        Bill mockBill = new Bill();
        mockBill.setAmount(100);
        mockBill.setDescription("Test bill");

        when(billService.getBillById(1)).thenReturn(mockBill);

        mockMvc.perform(get("/api/bills/retrieveBillById/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.description").value("Test bill"));
    }

    // 3. Test Update Bill by ID
    

    // 4. Test Delete Bill by ID
    @Test
    void testDeleteBill_Success() throws Exception {
        doNothing().when(billService).deleteBillById(1);

        mockMvc.perform(delete("/api/bills/delete/1"))
                .andExpect(status().isNoContent());
    }
}
