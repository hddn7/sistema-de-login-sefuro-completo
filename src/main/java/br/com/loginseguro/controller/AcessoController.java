package br.com.loginseguro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.loginseguro.form.CadastroForm;
import br.com.loginseguro.service.CadastroService;
import jakarta.validation.Valid;

@Controller
public class AcessoController {

    private final CadastroService cadastroService;

    public AcessoController(CadastroService cadastroService) {
        this.cadastroService = cadastroService;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/cadastro")
    public String formularioCadastro(Model model) {
        model.addAttribute("form", new CadastroForm());
        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrar(
            @Valid @ModelAttribute("form") CadastroForm form,
            BindingResult resultado,
            RedirectAttributes atributos) {
        if (resultado.hasErrors()) {
            return "cadastro";
        }
        if (!cadastroService.cadastrar(form)) {
            resultado.rejectValue("email", "email.existente", "Este e-mail já está cadastrado.");
            return "cadastro";
        }
        atributos.addFlashAttribute("sucesso", "Cadastro concluído. Entre com seu e-mail e senha.");
        return "redirect:/login";
    }
}
