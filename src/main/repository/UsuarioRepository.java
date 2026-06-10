package main.repository;

import java.util.ArrayList;
import java.util.List;

import main.models.Usuario;

public class UsuarioRepository {
    private int countId = 1;
    private List<Usuario> usuarios = new ArrayList<>();

    public Usuario salvar(Usuario u) throws IllegalArgumentException {

        if (verificarEmail(u.getEmail())) {
            throw new IllegalArgumentException();
        }
        if (verificarCpf(u.getCpf())) {
            throw new IllegalArgumentException();
        }

        u.setId(countId); // adiciona id automaticamente
        countId++; // incrementa o contador

        usuarios.add(u); // adiciona na lista

        return usuarios.getLast(); // retorna o que foi adicionado
    }

    private boolean verificarEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true; // email ja foi utilizado
            }
        }
        return false;
    }

    private boolean verificarCpf(String cpf) {
        for (Usuario u : usuarios) {
            if (u.getCpf().equalsIgnoreCase(cpf)) {
                return true; // cpf ja foi utilizado
            }
        }
        return false;
    }

    public Usuario buscarPorEmail(String email) {
        for (Usuario u : usuarios) {
            if (u.getEmail().equals(email)) {
                return u;
            }
        }
        return null;
    }

    public String fazerLogin(String email, String senha) {
        Usuario usuario = buscarPorEmail(email);
        if (usuario == null) {
            return "Credenciais inválidas";
        }
        if (usuario.getSenha().equals(senha)) {
            return "Sucesso";
        }
        return "Credenciais inválidas";
    }
}
