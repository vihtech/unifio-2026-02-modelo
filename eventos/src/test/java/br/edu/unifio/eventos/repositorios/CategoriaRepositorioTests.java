package br.edu.unifio.eventos.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.unifio.eventos.entidades.Categoria;

@SpringBootTest
public class CategoriaRepositorioTests {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    @Test
    public void deveSalvarUmaCategoriaNova() {
        var categoria = new Categoria();
        categoria.setNome("Gastronomia");
        categoria.setDescricao("Eventos sobre culinária e gastronomia");

        categoriaRepositorio.save(categoria);

        assertNotNull(categoria.getId());
        assertTrue(categoriaRepositorio.existsById(categoria.getId()));
    }

    @Test
    public void deveBuscarUmaCategoriaPorId() {
        Categoria categoria = categoriaRepositorio.findById(1).orElseThrow();

        assertNotNull(categoria);
        assertEquals("Tecnologia", categoria.getNome());
        assertEquals("Eventos sobre tecnologia e inovação", categoria.getDescricao());
    }

    @Test
    public void deveListarTodasAsCategorias() {
        long totalAntes = categoriaRepositorio.count();

        var categoria1 = new Categoria();
        categoria1.setNome("Música");
        categoria1.setDescricao("Shows e festivais de música");
        categoriaRepositorio.save(categoria1);

        var categoria2 = new Categoria();
        categoria2.setNome("Esportes");
        categoria2.setDescricao("Eventos esportivos e competições");
        categoriaRepositorio.save(categoria2);

        List<Categoria> categorias = categoriaRepositorio.findAll();

        assertEquals(totalAntes + 2, categorias.size());
        assertTrue(categorias.stream().anyMatch(c -> c.getNome().equals("Música")));
        assertTrue(categorias.stream().anyMatch(c -> c.getNome().equals("Esportes")));
    }

    @Test
    public void deveAtualizarUmaCategoria() {
        var categoria = new Categoria();
        categoria.setNome("Categoria Teste");
        categoria.setDescricao("Descrição Teste");
        categoriaRepositorio.save(categoria);

        categoria.setNome("Categoria Teste Atualizada");
        categoriaRepositorio.save(categoria);

        Categoria categoriaAtualizada = categoriaRepositorio.findById(categoria.getId()).orElseThrow();
        assertEquals("Categoria Teste Atualizada", categoriaAtualizada.getNome());
    }

    @Test
    public void deveRemoverUmaCategoriaPorId() {
        var categoria = new Categoria();
        categoria.setNome("Categoria para Remover");
        categoria.setDescricao("Será removida");
        categoriaRepositorio.save(categoria);

        assertTrue(categoriaRepositorio.existsById(categoria.getId()));

        categoriaRepositorio.deleteById(categoria.getId());

        assertFalse(categoriaRepositorio.existsById(categoria.getId()));
    }
}