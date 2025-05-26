package com.example.PWIIJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.PWIIJava.model.Filme;
import com.example.PWIIJava.service.FilmeService;

import jakarta.validation.Valid;

@Controller
public class FilmeController {
    @Autowired
    private FilmeService filmeService;

    @GetMapping("/filme")
    public String index(Model model) {
        model.addAttribute("filmesList", filmeService.getAllFilmes());
        return "filme/index";
    }

    @GetMapping("/filme/create")
    public String create(Model model) {
        model.addAttribute("filme", new Filme());
        return "filme/create";
    }

    @PostMapping("/filme/save")
    public String postMethodName(@ModelAttribute @Valid Filme filme, BindingResult result) {
        if (result.hasErrors()) {
            return "filme/create";
        }
        filmeService.saveFilme(filme);
        return "redirect:/filme";
    }

    @GetMapping("/filme/delete/{id}")
    public String delete(@PathVariable Long id) {
        this.filmeService.deleteFilmeById(id);
        return "redirect:/filme";
    }

    @GetMapping("/filme/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Filme filme = filmeService.getFilmeById(id);
        model.addAttribute("filme", filme);
        return "filme/edit";
    }
}
