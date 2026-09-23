package br.edu.unifio.eventos.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.unifio.eventos.entidades.Palestrante;

@SpringBootTest
public class PalestranteRepositorioTests {

    @Autowired
    private PalestranteRepositorio palestranteRepositorio;

    @Test
    public void deveSalvarUmPalestranteNovo() {
        var palestrante = new Palestrante();
        palestrante.setNome("Roberto Nogueira");
        palestrante.setMiniBio("Especialista em finanças e investimentos");
        palestrante.setEmail("roberto.nogueira@email.com");

        palestranteRepositorio.save(palestrante);

        assertNotNull(palestrante.getId());
        assertTrue(palestranteRepositorio.existsById(palestrante.getId()));
    }

    @Test
    public void deveBuscarUmPalestrantePorId() {
        Palestrante palestrante = palestranteRepositorio.findById(1).orElseThrow();

        assertNotNull(palestrante);
        assertEquals("Ana Souza", palestrante.getNome());
        assertEquals("ana.souza@email.com", palestrante.getEmail());
    }

    @Test
    public void deveListarTodosOsPalestrantes() {
        long totalAntes = palestranteRepositorio.count();

        var palestrante1 = new Palestrante();
        palestrante1.setNome("Camila Duarte");
        palestrante1.setMiniBio("Especialista em marketing digital");
        palestrante1.setEmail("camila.duarte@email.com");
        palestranteRepositorio.save(palestrante1);

        var palestrante2 = new Palestrante();
        palestrante2.setNome("Marcos Vieira");
        palestrante2.setMiniBio("Consultor em gestão de projetos");
        palestrante2.setEmail("marcos.vieira@email.com");
        palestranteRepositorio.save(palestrante2);

        List<Palestrante> palestrantes = palestranteRepositorio.findAll();

        assertEquals(totalAntes + 2, palestrantes.size());
        assertTrue(palestrantes.stream().anyMatch(p -> p.getNome().equals("Camila Duarte")));
        assertTrue(palestrantes.stream().anyMatch(p -> p.getNome().equals("Marcos Vieira")));
    }

    @Test
    public void deveAtualizarUmPalestrante() {
        var palestrante = new Palestrante();
        palestrante.setNome("Palestrante Teste");
        palestrante.setMiniBio("Bio Teste");
        palestrante.setEmail("teste@email.com");
        palestranteRepositorio.save(palestrante);

        palestrante.setEmail("teste.atualizado@email.com");
        palestranteRepositorio.save(palestrante);

        Palestrante palestranteAtualizado = palestranteRepositorio.findById(palestrante.getId()).orElseThrow();
        assertEquals("teste.atualizado@email.com", palestranteAtualizado.getEmail());
    }

    @Test
    public void deveRemoverUmPalestrantePorId() {
        var palestrante = new Palestrante();
        palestrante.setNome("Palestrante para Remover");
        palestrante.setMiniBio("Bio Teste");
        palestrante.setEmail("remover@email.com");
        palestranteRepositorio.save(palestrante);

        assertTrue(palestranteRepositorio.existsById(palestrante.getId()));

        palestranteRepositorio.deleteById(palestrante.getId());

        assertFalse(palestranteRepositorio.existsById(palestrante.getId()));
    }
}