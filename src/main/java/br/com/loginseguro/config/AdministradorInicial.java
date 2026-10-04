package br.com.loginseguro.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import br.com.loginseguro.model.Usuario;
import br.com.loginseguro.repository.UsuarioRepository;

@Component
public class AdministradorInicial implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}")
    private String email;

    @Value("${app.admin.password}")
    private String senha;

    public AdministradorInicial(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (email.isBlank() || senha.isBlank()) {
            return;
        }
        if (senha.length() < 8 || senha.length() > 72) {
            throw new IllegalArgumentException("A senha inicial do administrador deve ter entre 8 e 72 caracteres.");
        }

        String emailNormalizado = email.trim().toLowerCase(Locale.ROOT);
        if (usuarioRepository.existsByEmail(emailNormalizado)) {
            return;
        }

        Usuario administrador = new Usuario();
        administrador.setNome("Administrador");
        administrador.setEmail(emailNormalizado);
        administrador.setSenha(passwordEncoder.encode(senha));
        administrador.setPerfil("ADMIN");
        usuarioRepository.save(administrador);
    }
}
