package org.example.controllers;

import org.example.models.Professor;
import org.example.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {

    @Autowired
    private ProfessorRepository professorRepo;

    @GetMapping
    public Iterable<Professor> getAllProfessors()
    {
        return professorRepo.findAll();
    }
}
