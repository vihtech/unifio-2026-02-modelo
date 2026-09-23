package br.edu.unifio.eventos.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.unifio.eventos.entidades.Local;

@SpringBootTest
public class LocalRepositorioTests {

    @Autowired
    private LocalRepositorio localRepositorio;

    @Test
    public void deveSalvarUmLocalNovo() {
        var local = new Local();
        local.setNome("Arena Multiuso");
        local.setEndereco("Av. Central, 100 - Ourinhos/SP");
        local.setCapacidade(1000);

        localRepositorio.save(local);

        assertNotNull(local.getId());
        assertTrue(localRepositorio.existsById(local.getId()));
    }

    @Test
    public void deveBuscarUmLocalPorId() {
        Local local = localRepositorio.findById(2).orElseThrow();

        assertNotNull(local);
        assertEquals("Auditório Unifio", local.getNome());
        assertEquals(200, local.getCapacidade());
    }

    @Test
    public void deveListarTodosOsLocais() {
        long totalAntes = localRepositorio.count();

        var local1 = new Local();
        local1.setNome("Espaço Cultural");
        local1.setEndereco("Rua das Artes, 50 - Ourinhos/SP");
        local1.setCapacidade(120);
        localRepositorio.save(local1);

        var local2 = new Local();
        local2.setNome("Ginásio Municipal");
        local2.setEndereco("Rua dos Esportes, 20 - Ourinhos/SP");
        local2.setCapacidade(600);
        localRepositorio.save(local2);

        List<Local> locais = localRepositorio.findAll();

        assertEquals(totalAntes + 2, locais.size());
        assertTrue(locais.stream().anyMatch(l -> l.getNome().equals("Espaço Cultural")));
        assertTrue(locais.stream().anyMatch(l -> l.getNome().equals("Ginásio Municipal")));
    }

    @Test
    public void deveAtualizarUmLocal() {
        var local = new Local();
        local.setNome("Local Teste");
        local.setEndereco("Endereço Teste");
        local.setCapacidade(50);
        localRepositorio.save(local);

        local.setCapacidade(80);
        localRepositorio.save(local);

        Local localAtualizado = localRepositorio.findById(local.getId()).orElseThrow();
        assertEquals(80, localAtualizado.getCapacidade());
    }

    @Test
    public void deveRemoverUmLocalPorId() {
        var local = new Local();
        local.setNome("Local para Remover");
        local.setEndereco("Endereço Teste");
        local.setCapacidade(30);
        localRepositorio.save(local);

        assertTrue(localRepositorio.existsById(local.getId()));

        localRepositorio.deleteById(local.getId());

        assertFalse(localRepositorio.existsById(local.getId()));
    }
}