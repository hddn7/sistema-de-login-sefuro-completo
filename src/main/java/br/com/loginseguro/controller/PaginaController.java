package br.com.loginseguro.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @GetMapping("/painel")
    public String painel(Authentication autenticacao) {
        boolean administrador = autenticacao.getAuthorities().stream()
                .anyMatch(perfil -> perfil.getAuthority().equals("ROLE_ADMIN"));
        if (administrador) {
            return "redirect:/admin";
        }

        boolean professor = autenticacao.getAuthorities().stream()
                .anyMatch(perfil -> perfil.getAuthority().equals("ROLE_PROFESSOR"));
        if (professor) {
            return "redirect:/professor";
        }
        return "redirect:/aluno";
    }

    @GetMapping("/professor")
    public String professor() {
        return "professor";
    }

    @GetMapping("/aluno")
    public String aluno() {
        return "aluno";
    }
}
