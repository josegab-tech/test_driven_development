package main.repository;

import java.util.ArrayList;
import java.util.List;

import main.models.Usuario;

public class UsuarioRepository {
    private int countId = 1;
    private static List<Usuario> usuarios = new ArrayList<>();

    public Usuario salvar(Usuario u){
        u.setId(countId); // adiciona id automaticamente
        countId++; // incrementa o contador
        usuarios.add(u); // adiciona na lista 
        return usuarios.getLast(); // retorna o que foi adicionado
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
