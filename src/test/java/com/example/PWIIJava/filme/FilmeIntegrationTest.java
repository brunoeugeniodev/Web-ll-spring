package com.example.PWIIJava.filme;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.example.PWIIJava.model.Filme;
import com.example.PWIIJava.repository.FilmeRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test") // Usa application-test.properties
@Transactional // Limpa o banco após cada teste
public class FilmeIntegrationTest {
    

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmeRepository filmeRepository;

    @Test
    @WithMockUser(authorities = { "Admin" })
    void testSaveProductIntegration() throws Exception {

        Filme filmeA = new Filme();
        filmeA.setDescription("Descricao");
        filmeA.setName("Filme A");
        filmeA.setPrice(65.24f);
        filmeA.setStock(121);


        mockMvc.perform(post("/filme/save")
                .with(csrf())
                .flashAttr("filme", filmeA))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/filme"));

        // Verifica no banco se foi salvo
        assertTrue(filmeRepository.findAll()
                .stream()
                .anyMatch(p -> "Filme A".equals(p.getName())));
        
    }
}
