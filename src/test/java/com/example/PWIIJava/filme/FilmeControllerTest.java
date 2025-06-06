package com.example.PWIIJava.filme;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.PWIIJava.config.TestConfig;
import com.example.PWIIJava.controller.FilmeController;
import com.example.PWIIJava.model.Filme;
import com.example.PWIIJava.service.FilmeService;

@WebMvcTest(FilmeController.class)
@Import(TestConfig.class)
public class FilmeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilmeService filmeService;

    @AfterEach
    void resetMocks() {
        reset(filmeService);
    }

    private List<Filme> testCreateFilmeList(){
        Filme filmeB = new Filme();
        filmeB.setId(1L);
        filmeB.setDescription("Descricao");
        filmeB.setName("Filme B");
        filmeB.setPrice(65.24f);
        filmeB.setStock(121);

        return List.of(filmeB);
    }

    @Test
    @DisplayName("GET /filme - Listar filmes na tela index sem usuário autenticado")
    void testIndexNotAuthenticatedUser() throws Exception {
         mockMvc.perform(get("/filme"))
            .andExpect(status().isUnauthorized()); // Correção aqui
    }

    @Test
    @WithMockUser
    @DisplayName("GET /filme - Listar filmes na tela index com usuário logado")
    void testIndexAuthenticatedUser() throws Exception {
        when(filmeService.getAllFilmes()).thenReturn(testCreateFilmeList());

        mockMvc.perform(get("/filme"))
               .andExpect(status().isOk())
               .andExpect(view().name("filme/index"))
               .andExpect(model().attributeExists("filmesList"))
               .andExpect(content().string(containsString("Listagem de Filme")))
               .andExpect(content().string(containsString("Filme B")));
    }

    @Test
    @WithMockUser(username = "aluno@iftm.edu.br", authorities = { "Admin" })
    @DisplayName("GET /filme/create - Exibe formulário de criação")
    void testCreateFormAuthorizedUser() throws Exception {
        mockMvc.perform(get("/filme/create"))
                .andExpect(status().isOk())
                .andExpect(view().name("filme/create"))
                .andExpect(model().attributeExists("filme"))
                .andExpect(content().string(containsString("Cadastrar Filme")));
    }

    @Test
    @WithMockUser(username = "aluno2@iftm.edu.br", authorities = { "Manager" })
    @DisplayName("GET /filme - Verificar o link de cadastrar para um usuario não admin logado")
    void testCreateFormNotAuthorizedUser() throws Exception {
        when(filmeService.getAllFilmes()).thenReturn(testCreateFilmeList());
       // Obter o HTML da página renderizada pelo controlador
       mockMvc.perform(get("/filme/create"))
            .andExpect(status().isOk())
            .andExpect(view().name("filme/create"))
            .andExpect(model().attributeExists("filme"))
            .andExpect(content().string(not(containsString("<a class=\"dropdown-item\" href=\"/filme/create\">Cadastrar</a>"))));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /filme/save - Falha na validação e retorna para o formulário")
    void testSaveFilmeValidationError() throws Exception {
        Filme filme = new Filme(); // Produto sem nome, o que causará erro de validação

        mockMvc.perform(post("/filme/save")
                        .with(csrf())
                        .flashAttr("filme", filme))
                .andExpect(status().isOk())
                .andExpect(view().name("filme/create"))
                .andExpect(model().attributeHasErrors("filme"));

        verify(filmeService, never()).saveFilme(any(Filme.class));
    }

    @Test
    @WithMockUser(username = "aluno@iftm.edu.br", authorities = { "Admin" })
    @DisplayName("POST /filme/save - Filme válido é salvo com sucesso")
    void testSaveValidFilme() throws Exception {
        Filme filme = new Filme();
        filme.setName("Novo Filme");
        filme.setDescription("Descrição");
        filme.setPrice(100f);
        filme.setStock(10);

        mockMvc.perform(post("/filme/save")
                        .with(csrf())
                        .flashAttr("filme", filme))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/filme"));

        verify(filmeService).saveFilme(any(Filme.class));
    }

}
