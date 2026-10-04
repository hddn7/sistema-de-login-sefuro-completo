package br.com.loginseguro.service;

import java.util.Locale;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.loginseguro.form.CadastroForm;
import br.com.loginseguro.model.Usuario;
import br.com.loginseguro.repository.UsuarioRepository;

@Service
public class CadastroService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public CadastroService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean cadastrar(CadastroForm form) {
        String email = form.getEmail().trim().toLowerCase(Locale.ROOT);
        if (usuarioRepository.existsByEmail(email)) {
            return false;
        }

        Usuario usuario = new Usuario();
        usuario.setNome(form.getNome().trim());
        usuario.setEmail(email);
        usuario.setSenha(passwordEncoder.encode(form.getSenha()));
        usuario.setPerfil("ALUNO");
        usuarioRepository.save(usuario);
        return true;
    }
}
