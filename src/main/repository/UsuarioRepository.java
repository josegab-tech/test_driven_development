package main.repository;

import java.util.ArrayList;
import java.util.List;

import main.models.Usuario;

public class UsuarioRepository {
    private int countId = 1;
    private List<Usuario> usuarios = new ArrayList<>();

    public Usuario salvar(Usuario u) throws IllegalArgumentException {

        if(u.getEmail() != null) u.setEmail(u.getEmail().trim());
        if(u.getNome() !=null) u.setNome(u.getNome().trim());

        if(u.getNome() != null && u.getNome().length() > 500){
            throw new IllegalArgumentException("O nome não pode exceder 500 caracteres.");
        }
        if(u.getEmail()!= null && u.getEmail().length() > 500){
            throw new IllegalArgumentException("O e-mail não pode exceder 500 caracteres.");
        }

        if(u.getSenha() == null || !validarComplexidadeDaSenha(u.getSenha())){
            throw new IllegalArgumentException("A senha não cumpre os requisitos de complexidade.");
        }

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
        //Trim no email para login flexivel
        if(email != null) email = email.trim();

        Usuario usuario = buscarPorEmail(email);
        if (usuario == null) {
            return "Credenciais inválidas";
        }
        if (usuario.getSenha().equals(senha)) {
            return "Sucesso";
        }
        return "Credenciais inválidas";
    }

    private boolean validarComplexidadeDaSenha(String senha){
        if(senha.length() < 8 || senha.contains(" ")){
            return false;
        }

        boolean temNumero = senha.matches(".*\\d.*");
        //Vericia se possui caracteres especiais ou fora da faixa Alfanumérica
        boolean temEspecialOuEmoji = senha.matches(".*[^a-zA-Z0-9].*");

        return temNumero && temEspecialOuEmoji;
    }
}
