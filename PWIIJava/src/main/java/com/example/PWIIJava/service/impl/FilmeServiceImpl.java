package com.example.PWIIJava.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.PWIIJava.model.Filme;
import com.example.PWIIJava.repository.FilmeRepository;
import com.example.PWIIJava.service.FilmeService;

@Service
public class FilmeServiceImpl implements FilmeService{
    @Autowired
    private FilmeRepository filmeRepository;

    @Override
    public List <Filme> getAllFilmes(){
        return filmeRepository.findAll();
    }

    @Override
    public void saveFilme(Filme filme){
        this.filmeRepository.save(filme);
    }

    @Override
    public Filme getFilmeById(long id) {
        Optional < Filme > optional = filmeRepository.findById(id);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            throw new RuntimeException("Filme not found with id: " + id);
        }
    }

    @Override
    public void deleteFilmeById(long id) {
        this.filmeRepository.deleteById(id);
    }
}
