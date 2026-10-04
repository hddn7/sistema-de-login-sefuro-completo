package br.com.loginseguro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.loginseguro.form.CadastroForm;
import br.com.loginseguro.model.Usuario;
import br.com.loginseguro.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class CadastroServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CadastroService cadastroService;

    @Test
    void salvaUsuarioComSenhaProtegidaEPerfilDeAluno() {
        CadastroForm form = new CadastroForm();
        form.setNome(" Ana ");
        form.setEmail(" ANA@EXEMPLO.COM ");
        form.setSenha("senha-segura");

        when(usuarioRepository.existsByEmail("ana@exemplo.com")).thenReturn(false);
        when(passwordEncoder.encode("senha-segura")).thenReturn("senha-codificada");

        assertTrue(cadastroService.cadastrar(form));

        ArgumentCaptor<Usuario> capturador = ArgumentCaptor.forClass(Usuario.class);
        verify(usuarioRepository).save(capturador.capture());
        assertEquals("Ana", capturador.getValue().getNome());
        assertEquals("ana@exemplo.com", capturador.getValue().getEmail());
        assertEquals("senha-codificada", capturador.getValue().getSenha());
        assertEquals("ALUNO", capturador.getValue().getPerfil());
    }

    @Test
    void naoCadastraEmailQueJaExiste() {
        CadastroForm form = new CadastroForm();
        form.setNome("Ana");
        form.setEmail("ana@exemplo.com");
        form.setSenha("senha-segura");

        when(usuarioRepository.existsByEmail("ana@exemplo.com")).thenReturn(true);

        assertFalse(cadastroService.cadastrar(form));
    }
}