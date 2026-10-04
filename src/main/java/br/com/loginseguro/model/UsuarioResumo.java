package br.com.loginseguro.model;

public class UsuarioResumo {

    private final String id;
    private final String nome;
    private final String email;
    private final String perfil;

    public UsuarioResumo(String id, String nome, String email, String perfil) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.perfil = perfil;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getPerfil() {
        return perfil;
    }
}