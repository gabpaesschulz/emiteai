package com.emiteai.api.controller;

import com.emiteai.api.TestBootConfig;
import com.emiteai.api.model.dto.PessoaResponse;
import com.emiteai.api.service.PessoaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PessoaController.class)
@ContextConfiguration(classes = TestBootConfig.class)
class PessoaControllerTest {

    @Autowired private MockMvc mvc;

    @MockitoBean private PessoaService service;

    @Test
    void list_retorna200eJson() throws Exception {
        PessoaResponse resp = new PessoaResponse(
                1L,"Ana","1199","111","01001","100",
                null,"Sé","São Paulo","SP");

        when(service.list()).thenReturn(List.of(resp));

        mvc.perform(get("/pessoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Ana"));
    }
}
