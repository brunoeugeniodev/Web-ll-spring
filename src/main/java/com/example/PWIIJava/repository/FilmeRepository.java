package com.example.PWIIJava.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.PWIIJava.model.Filme;

@Repository
public interface FilmeRepository extends JpaRepository<Filme, Long> {
    
}
