package tests;

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

        Usuario novoUsuario = new Usuario("Matheus", "matheusgedesinho@gmail.com", "122.322.323-15", "senha123",
                LocalDate.parse("2026-06-05"));

        Usuario usuarioSalvo = repository.salvar(novoUsuario);

        assertNotNull(usuarioSalvo, "O objeto salvo não deveria ser nulo.");
        assertNotNull(usuarioSalvo.getId(), "O id deveria ter sido gerado.");
    }

    @Test
    void criarEmailDuplicado() {
        Usuario novoUsuario = new Usuario("Matheus", "matheusgedesinho@gmail.com", "122.322.323-15",
                LocalDate.parse("2026-06-05"));

        Usuario novoUsuario2 = new Usuario("Mathias", "matheusgedesinho@gmail.com", "122.322.323-15",
                LocalDate.parse("2026-06-05"));

        repository.salvar(novoUsuario);

        assertThrows(IllegalArgumentException.class, () -> {
            repository.salvar(novoUsuario2);
        }, "Nao deveria ser possivel salvar um usuario com o mesmo email");

    }

    @Test
    void criarCpfDuplicado() {
        Usuario novoUsuario = new Usuario("Matheus", "matheusgedesinho@gmail.com", "122.322.323-15",
                LocalDate.parse("2026-06-05"));

        Usuario novoUsuario2 = new Usuario("Mathias", "matheusgedesinho@gmail.com", "122.322.323-15",
                LocalDate.parse("2026-06-05"));

        repository.salvar(novoUsuario);

        assertThrows(IllegalArgumentException.class, () -> {
            repository.salvar(novoUsuario2);
        }, "Nao deveria ser possivel salvar um usuario com o mesmo cpf");

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

    @Test
    void validarRegrasDeComplexidadeDaSenha() {

        // Senha sem números e sem caractere especial
        Usuario uFraco = new Usuario("Jose", "j1@email.com", "111.111.111-11", "justletters",
                LocalDate.parse("2026-06-05"));
        assertThrows(IllegalArgumentException.class, () -> repository.salvar(uFraco),
                "Deveria barrar senha sem números/especiais.");

        // Senha com menos de 8 caracteres
        Usuario uCurto = new Usuario("Jose", "j2@email.com", "222.222.222-22", "123!", LocalDate.parse("2026-06-05"));
        assertThrows(IllegalArgumentException.class, () -> repository.salvar(uCurto), "Deveria barrar senha curta.");

        Usuario uComEspaco = new Usuario("Jose", "j3@email.com", "333.333.333-33", "Senha 123!",
                LocalDate.parse("2026-06-05"));
        assertThrows(IllegalArgumentException.class, () -> repository.salvar(uComEspaco),
                "Deveria barrar senha com espaços.");

        // Senha contendo espaços coma as regras
        Usuario uValido = new Usuario("Jose", "j4@email.com", "444.444.444-44", "Senha123!",
                LocalDate.parse("2026-06-05"));
        assertNotNull(repository.salvar(uValido),
                "DEveria salvar com sucesso uma senha complexa.");
    }

    @Test
    void fazerTrimNosEspacosDeEmailEUsuario() {
        Usuario usuarioComEspacos = new Usuario("  Jose Trim  ", "  trim@email.com  ", "555.555.555-55", "Senha123!",
                LocalDate.parse("2026-06-05"));
        Usuario salvo = repository.salvar(usuarioComEspacos);

        // Garantir o trim ao salvar
        assertEquals("Jose Trim", salvo.getNome());
        assertEquals("trim@email.com", salvo.getEmail());

        // Garantir que o login funcione mesmo se espaços acidentais
        String resultado = repository.fazerLogin("   trim@email.com   ", "Senha123!");
        assertEquals("Sucesso", resultado, "O login deve ignorar espaços extras nas pontas do e-mail.");
    }

    @Test
    void aceitarEmojisNaSenha() {
        Usuario usuarioComEmoji = new Usuario("Jose", "emoji@email.com", "777.777.777-77", "senha🚀🔥123",
                LocalDate.parse("2026-06-05"));
        Usuario salvo = repository.salvar(usuarioComEmoji);

        assertEquals("senha🚀🔥123", salvo.getSenha());

        String resultado = repository.fazerLogin("emoji@email.com", "senha🚀🔥123");
        assertEquals("Sucesso", resultado, "O sistema auteentificou com sucesso os emojis.");

    }

    @Test
    void barrarTextoQueExcedaLimiteMaximo() {
        //String gigante
    String textLongo = "A".repeat(501);

    Usuario nomeLongo = new Usuario(textLongo, "longo@email.com", "888.888.888-88", "Senha123!", LocalDate.parse("2026-06-05"));
    assertThrows(IllegalArgumentException.class, () -> repository.salvar(nomeLongo), "Deveria barrar nome maior que 500 caracteres.");


    Usuario emailLongo = new Usuario("jose", textLongo, "888.888.888-88", "Senha123!", LocalDate.parse("2026-06-05"));
    assertThrows(IllegalArgumentException.class, () -> repository.salvar(emailLongo), "Deveria barrar email maior que 500 caracteres.");
    }

}
