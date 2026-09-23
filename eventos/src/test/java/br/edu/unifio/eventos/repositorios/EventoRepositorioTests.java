package br.edu.unifio.eventos.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.unifio.eventos.entidades.Categoria;
import br.edu.unifio.eventos.entidades.Evento;
import br.edu.unifio.eventos.entidades.Local;
import br.edu.unifio.eventos.entidades.Palestrante;

@SpringBootTest
public class EventoRepositorioTests {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;
    @Autowired
    private LocalRepositorio localRepositorio;
    @Autowired
    private PalestranteRepositorio palestranteRepositorio;
    @Autowired
    private EventoRepositorio eventoRepositorio;

    @Test
    public void deveSalvarUmEventoNovo() {
        var evento = new Evento();
        evento.setNome("Workshop de Robótica");
        evento.setDescricao("Workshop prático sobre robótica e automação");
        evento.setDataInicio(LocalDateTime.of(2026, 11, 5, 9, 0));
        evento.setDataFim(LocalDateTime.of(2026, 11, 5, 18, 0));
        evento.setCapacidade(150);
        evento.setStatus("PLANEJADO");

        Categoria categoria = categoriaRepositorio.findById(1).orElseThrow();
        evento.setCategoria(categoria);

        Local local = localRepositorio.findById(2).orElseThrow();
        evento.setLocal(local);

        Palestrante palestrante = palestranteRepositorio.findById(1).orElseThrow();
        evento.setPalestrante(palestrante);

        eventoRepositorio.save(evento);

        assertNotNull(evento.getId());
        assertTrue(eventoRepositorio.existsById(evento.getId()));
    }

    @Test
    public void deveBuscarUmEventoPorId() {
        Evento evento = eventoRepositorio.findById(1).orElseThrow();

        assertNotNull(evento);
        assertEquals("Semana de Tecnologia 2026", evento.getNome());
        assertEquals("CONFIRMADO", evento.getStatus());
    }

    @Test
    public void deveListarTodosOsEventos() {
        long totalAntes = eventoRepositorio.count();

        Categoria categoria = categoriaRepositorio.findById(2).orElseThrow();
        Local local = localRepositorio.findById(3).orElseThrow();
        Palestrante palestrante = palestranteRepositorio.findById(2).orElseThrow();

        var evento1 = new Evento();
        evento1.setNome("Encontro de Startups");
        evento1.setDescricao("Networking e pitches de startups");
        evento1.setDataInicio(LocalDateTime.of(2026, 10, 20, 9, 0));
        evento1.setDataFim(LocalDateTime.of(2026, 10, 20, 18, 0));
        evento1.setCapacidade(300);
        evento1.setStatus("PLANEJADO");
        evento1.setCategoria(categoria);
        evento1.setLocal(local);
        evento1.setPalestrante(palestrante);
        eventoRepositorio.save(evento1);

        var evento2 = new Evento();
        evento2.setNome("Feira de Inovação");
        evento2.setDescricao("Exposição de projetos inovadores");
        evento2.setDataInicio(LocalDateTime.of(2026, 11, 15, 9, 0));
        evento2.setDataFim(LocalDateTime.of(2026, 11, 17, 18, 0));
        evento2.setCapacidade(500);
        evento2.setStatus("PLANEJADO");
        evento2.setCategoria(categoria);
        evento2.setLocal(local);
        evento2.setPalestrante(palestrante);
        eventoRepositorio.save(evento2);

        List<Evento> eventos = eventoRepositorio.findAll();

        assertEquals(totalAntes + 2, eventos.size());
        assertTrue(eventos.stream().anyMatch(e -> e.getNome().equals("Encontro de Startups")));
        assertTrue(eventos.stream().anyMatch(e -> e.getNome().equals("Feira de Inovação")));
    }

    @Test
    public void deveAtualizarUmEvento() {
        Categoria categoria = categoriaRepositorio.findById(1).orElseThrow();
        Local local = localRepositorio.findById(1).orElseThrow();
        Palestrante palestrante = palestranteRepositorio.findById(1).orElseThrow();

        var evento = new Evento();
        evento.setNome("Evento Teste");
        evento.setDescricao("Descrição Teste");
        evento.setDataInicio(LocalDateTime.of(2026, 12, 1, 9, 0));
        evento.setDataFim(LocalDateTime.of(2026, 12, 1, 18, 0));
        evento.setCapacidade(100);
        evento.setStatus("PLANEJADO");
        evento.setCategoria(categoria);
        evento.setLocal(local);
        evento.setPalestrante(palestrante);
        eventoRepositorio.save(evento);

        evento.setStatus("CONFIRMADO");
        eventoRepositorio.save(evento);

        Evento eventoAtualizado = eventoRepositorio.findById(evento.getId()).orElseThrow();
        assertEquals("CONFIRMADO", eventoAtualizado.getStatus());
    }

    @Test
    public void deveRemoverUmEventoPorId() {
        Categoria categoria = categoriaRepositorio.findById(1).orElseThrow();
        Local local = localRepositorio.findById(1).orElseThrow();
        Palestrante palestrante = palestranteRepositorio.findById(1).orElseThrow();

        var evento = new Evento();
        evento.setNome("Evento para Remover");
        evento.setDescricao("Descrição Teste");
        evento.setDataInicio(LocalDateTime.of(2026, 12, 10, 9, 0));
        evento.setDataFim(LocalDateTime.of(2026, 12, 10, 18, 0));
        evento.setCapacidade(50);
        evento.setStatus("PLANEJADO");
        evento.setCategoria(categoria);
        evento.setLocal(local);
        evento.setPalestrante(palestrante);
        eventoRepositorio.save(evento);

        assertTrue(eventoRepositorio.existsById(evento.getId()));

        eventoRepositorio.deleteById(evento.getId());

        assertFalse(eventoRepositorio.existsById(evento.getId()));
    }
}