package br.edu.unifio.eventos.repositorios;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.edu.unifio.eventos.entidades.Participante;

@SpringBootTest
public class ParticipanteRepositorioTests {

    @Autowired
    private ParticipanteRepositorio participanteRepositorio;

    @Test
    public void deveSalvarUmParticipanteNovo() {
        var participante = new Participante();
        participante.setNome("Isabelly Silva");
        participante.setEmail("isabelly.silva@gmail.com");
        participante.setTelefone("(14) 99845-3088");

        participanteRepositorio.save(participante);

        assertNotNull(participante.getId());
        assertTrue(participanteRepositorio.existsById(participante.getId()));
    }

    @Test
    public void deveBuscarUmParticipantePorId() {
        Participante participante = participanteRepositorio.findById(1).orElseThrow();

        assertNotNull(participante);
        assertEquals("Pedro Henrique", participante.getNome());
        assertEquals("pedro.henrique@email.com", participante.getEmail());
    }

    @Test
    public void deveListarTodosOsParticipantes() {
        long totalAntes = participanteRepositorio.count();

        var participante1 = new Participante();
        participante1.setNome("Camila Ferreira");
        participante1.setEmail("camila.ferreira@email.com");
        participante1.setTelefone("(14) 99999-0007");
        participanteRepositorio.save(participante1);

        var participante2 = new Participante();
        participante2.setNome("Thiago Almeida");
        participante2.setEmail("thiago.almeida@email.com");
        participante2.setTelefone("(14) 99999-0008");
        participanteRepositorio.save(participante2);

        List<Participante> participantes = participanteRepositorio.findAll();

        assertEquals(totalAntes + 2, participantes.size());
        assertTrue(participantes.stream().anyMatch(p -> p.getNome().equals("Camila Ferreira")));
        assertTrue(participantes.stream().anyMatch(p -> p.getNome().equals("Thiago Almeida")));
    }

    @Test
    public void deveAtualizarUmParticipante() {
        var participante = new Participante();
        participante.setNome("Participante Teste");
        participante.setEmail("teste@email.com");
        participante.setTelefone("(14) 90000-0000");
        participanteRepositorio.save(participante);

        participante.setTelefone("(14) 91111-1111");
        participanteRepositorio.save(participante);

        Participante participanteAtualizado = participanteRepositorio.findById(participante.getId()).orElseThrow();
        assertEquals("(14) 91111-1111", participanteAtualizado.getTelefone());
    }

    @Test
    public void deveRemoverUmParticipantePorId() {
        var participante = new Participante();
        participante.setNome("Participante para Remover");
        participante.setEmail("remover@email.com");
        participante.setTelefone("(14) 92222-2222");
        participanteRepositorio.save(participante);

        assertTrue(participanteRepositorio.existsById(participante.getId()));

        participanteRepositorio.deleteById(participante.getId());

        assertFalse(participanteRepositorio.existsById(participante.getId()));
    }
}