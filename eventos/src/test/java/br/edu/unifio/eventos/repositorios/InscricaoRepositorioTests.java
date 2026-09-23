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

import br.edu.unifio.eventos.entidades.Evento;
import br.edu.unifio.eventos.entidades.Inscricao;
import br.edu.unifio.eventos.entidades.Participante;

@SpringBootTest
public class InscricaoRepositorioTests {

    @Autowired
    private EventoRepositorio eventoRepositorio;
    @Autowired
    private ParticipanteRepositorio participanteRepositorio;
    @Autowired
    private InscricaoRepositorio inscricaoRepositorio;

    @Test
    public void deveSalvarUmaInscricaoNova() {
        var inscricao = new Inscricao();
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setStatus("PENDENTE");

        Evento evento = eventoRepositorio.findById(1).orElseThrow();
        inscricao.setEvento(evento);

        Participante participante = participanteRepositorio.findById(3).orElseThrow();
        inscricao.setParticipante(participante);

        inscricaoRepositorio.save(inscricao);

        assertNotNull(inscricao.getId());
        assertTrue(inscricaoRepositorio.existsById(inscricao.getId()));
    }

    @Test
    public void deveBuscarUmaInscricaoPorId() {
        Inscricao inscricao = inscricaoRepositorio.findById(1).orElseThrow();

        assertNotNull(inscricao);
        assertEquals("CONFIRMADA", inscricao.getStatus());
        assertEquals(1, inscricao.getEvento().getId());
    }

    @Test
    public void deveListarTodasAsInscricoes() {
        long totalAntes = inscricaoRepositorio.count();

        Evento evento = eventoRepositorio.findById(2).orElseThrow();
        Participante participante1 = participanteRepositorio.findById(1).orElseThrow();
        Participante participante2 = participanteRepositorio.findById(2).orElseThrow();

        var inscricao1 = new Inscricao();
        inscricao1.setDataInscricao(LocalDateTime.now());
        inscricao1.setStatus("CONFIRMADA");
        inscricao1.setEvento(evento);
        inscricao1.setParticipante(participante1);
        inscricaoRepositorio.save(inscricao1);

        var inscricao2 = new Inscricao();
        inscricao2.setDataInscricao(LocalDateTime.now());
        inscricao2.setStatus("PENDENTE");
        inscricao2.setEvento(evento);
        inscricao2.setParticipante(participante2);
        inscricaoRepositorio.save(inscricao2);

        List<Inscricao> inscricoes = inscricaoRepositorio.findAll();

        assertEquals(totalAntes + 2, inscricoes.size());
        assertTrue(inscricoes.stream().anyMatch(i -> i.getId().equals(inscricao1.getId())));
        assertTrue(inscricoes.stream().anyMatch(i -> i.getId().equals(inscricao2.getId())));
    }

    @Test
    public void deveAtualizarUmaInscricao() {
        Evento evento = eventoRepositorio.findById(1).orElseThrow();
        Participante participante = participanteRepositorio.findById(1).orElseThrow();

        var inscricao = new Inscricao();
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setStatus("PENDENTE");
        inscricao.setEvento(evento);
        inscricao.setParticipante(participante);
        inscricaoRepositorio.save(inscricao);

        inscricao.setStatus("CONFIRMADA");
        inscricaoRepositorio.save(inscricao);

        Inscricao inscricaoAtualizada = inscricaoRepositorio.findById(inscricao.getId()).orElseThrow();
        assertEquals("CONFIRMADA", inscricaoAtualizada.getStatus());
    }

    @Test
    public void deveRemoverUmaInscricaoPorId() {
        Evento evento = eventoRepositorio.findById(1).orElseThrow();
        Participante participante = participanteRepositorio.findById(1).orElseThrow();

        var inscricao = new Inscricao();
        inscricao.setDataInscricao(LocalDateTime.now());
        inscricao.setStatus("CANCELADA");
        inscricao.setEvento(evento);
        inscricao.setParticipante(participante);
        inscricaoRepositorio.save(inscricao);

        assertTrue(inscricaoRepositorio.existsById(inscricao.getId()));

        inscricaoRepositorio.deleteById(inscricao.getId());

        assertFalse(inscricaoRepositorio.existsById(inscricao.getId()));
    }
}