package br.com.loginseguro.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaginaController {

    @GetMapping("/painel")
    public String painel(Authentication autenticacao) {
        for (GrantedAuthority perfil : autenticacao.getAuthorities()) {
            if (perfil.getAuthority().equals("ROLE_ADMIN")) {
                return "redirect:/admin";
            }
            if (perfil.getAuthority().equals("ROLE_PROFESSOR")) {
                return "redirect:/professor";
            }
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
