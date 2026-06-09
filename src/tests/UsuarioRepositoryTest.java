package tests;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;

import main.models.Usuario;
import main.repository.UsuarioRepository;

import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;

public class UsuarioRepositoryTest {
    UsuarioRepository repository;

    @BeforeEach
    void setUp() {
        repository = new UsuarioRepository();
    }

    @Test
    void testAdicionarUsuario() {

        Usuario novoUsuario = new Usuario("Matheus", "matheusgedesinho@gmail.com", "122.322.323-15","senha123",
                LocalDate.parse("2026-06-05"));

        Usuario usuarioSalvo = repository.salvar(novoUsuario);

        assertNotNull(usuarioSalvo, "O objeto não poderia estar vazio.");

        assertNotNull(usuarioSalvo.getId(), "O id não foi inserido corretamente.");

    }

    @Test
    void fazerLoginComSucesso() {
        Usuario novoUsuario = new Usuario("Agabo", "agabo@email.com", "111.222.333-44", "senhaSegura",
                LocalDate.parse("1990-01-01"));
        repository.salvar(novoUsuario);

        String resultado = repository.fazerLogin("agabo@email.com", "senhaSegura");

        assertEquals("Sucesso", resultado, "O login deveria ser realizado com sucesso.");
    }

    @Test
    void negarLoginComEmailNaoCadastrado() {
        String resultado = repository.fazerLogin("naoexiste@email.com", "senha123");

        assertEquals("Credenciais inválidas", resultado, "Deveria retornar erro para e-mail não cadastrado.");
    }

    @Test
    void negarLoginComSenhaIncorreta() {
        Usuario novoUsuario = new Usuario("Agabo", "agabo@email.com", "111.222.333-44", "senhaCorreta",
                LocalDate.parse("1990-01-01"));
        repository.salvar(novoUsuario);

        String resultado = repository.fazerLogin("agabo@email.com", "senhaErrada");

        assertEquals("Credenciais inválidas", resultado, "Deveria negar o acesso para senha incorreta.");
    }

}
