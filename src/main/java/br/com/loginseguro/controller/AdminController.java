package br.com.loginseguro.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.loginseguro.model.Usuario;
import br.com.loginseguro.model.UsuarioResumo;
import br.com.loginseguro.repository.UsuarioRepository;

@Controller
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final FindByIndexNameSessionRepository<? extends Session> sessoes;

    public AdminController(
            UsuarioRepository usuarioRepository,
            FindByIndexNameSessionRepository<? extends Session> sessoes) {
        this.usuarioRepository = usuarioRepository;
        this.sessoes = sessoes;
    }

    @GetMapping("/admin")
    public String listarUsuarios(Model model) {
        List<Usuario> listaDoBanco = usuarioRepository.findAll();
        List<UsuarioResumo> usuarios = new ArrayList<>();
        for (Usuario usuario : listaDoBanco) {
            UsuarioResumo resumo = new UsuarioResumo(usuario.getId(), usuario.getNome(),
                usuario.getEmail(), usuario.getPerfil());
            usuarios.add(resumo);
        }
        model.addAttribute("usuarios", usuarios);
        return "admin";
    }

    @PostMapping("/admin/usuarios/{id}/perfil")
    public String alterarPerfil(
            @PathVariable String id,
            @RequestParam String perfil,
            Authentication autenticacao,
            RedirectAttributes atributos) {
        boolean perfilValido = perfil.equals("ADMIN")
            || perfil.equals("PROFESSOR")
            || perfil.equals("ALUNO");
        if (!perfilValido) {
            atributos.addFlashAttribute("erro", "O perfil escolhido não é válido.");
            return "redirect:/admin";
        }

        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            atributos.addFlashAttribute("erro", "Usuário não encontrado.");
            return "redirect:/admin";
        }
        if (usuario.getEmail().equals(autenticacao.getName())) {
            atributos.addFlashAttribute("erro", "Não é possível alterar o próprio perfil.");
            return "redirect:/admin";
        }
        if (usuario.getPerfil().equals("ADMIN") && !perfil.equals("ADMIN")
                && usuarioRepository.countByPerfil("ADMIN") <= 1) {
            atributos.addFlashAttribute("erro", "O sistema precisa manter pelo menos um administrador.");
            return "redirect:/admin";
        }

        usuario.setPerfil(perfil);
        usuarioRepository.save(usuario);
        encerrarSessoes(usuario.getEmail());
        atributos.addFlashAttribute("sucesso", "Perfil atualizado.");
        return "redirect:/admin";
    }

    @PostMapping("/admin/usuarios/{id}/excluir")
    public String excluirUsuario(
            @PathVariable String id,
            Authentication autenticacao,
            RedirectAttributes atributos) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null) {
            atributos.addFlashAttribute("erro", "Usuário não encontrado.");
            return "redirect:/admin";
        }
        if (usuario.getEmail().equals(autenticacao.getName())) {
            atributos.addFlashAttribute("erro", "Não é possível excluir a própria conta.");
            return "redirect:/admin";
        }
        if (usuario.getPerfil().equals("ADMIN") && usuarioRepository.countByPerfil("ADMIN") <= 1) {
            atributos.addFlashAttribute("erro", "O sistema precisa manter pelo menos um administrador.");
            return "redirect:/admin";
        }

        encerrarSessoes(usuario.getEmail());
        usuarioRepository.delete(usuario);
        atributos.addFlashAttribute("sucesso", "Usuário excluído.");
        return "redirect:/admin";
    }

    private void encerrarSessoes(String email) {
        for (Session sessao : sessoes.findByPrincipalName(email).values()) {
            sessoes.deleteById(sessao.getId());
        }
    }
}