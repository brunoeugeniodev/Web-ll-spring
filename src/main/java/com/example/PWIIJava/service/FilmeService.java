package com.example.PWIIJava.service;

import java.util.List;

import com.example.PWIIJava.model.Filme;

public interface FilmeService {
    public List <Filme> getAllFilmes();
    void saveFilme(Filme filme);
    Filme getFilmeById(long id);
    void deleteFilmeById(long id);
}
