package br.com.sicredi.pautavotacao.service;

import br.com.sicredi.pautavotacao.domain.OpcaoVoto;
import br.com.sicredi.pautavotacao.domain.Pauta;
import br.com.sicredi.pautavotacao.domain.Voto;
import br.com.sicredi.pautavotacao.exceptions.EleitorJaVotouException;
import br.com.sicredi.pautavotacao.exceptions.JaExisteSessaoException;
import br.com.sicredi.pautavotacao.exceptions.PautaNaoEncontradaException;
import br.com.sicredi.pautavotacao.exceptions.SessaoFechadaException;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

@QuarkusTest
public class PautaServiceTest {

    @Inject
    PautaService pautaService;

    @Test
    public void testaInclusaoDePauta() {
        Pauta pautaIncluda = pautaService.criarNovaPauta(Pauta.builder().titulo("Teste 123").descricao("Descricao da pauta").build());
        assertNotNull(pautaIncluda.id);
    }

    @Test
    public void testInicioSessaoVotacao() {
        Pauta pautaSalva = pautaService.criarNovaPauta(Pauta.builder().titulo("Teste 123").descricao("Descricao da pauta").build());

        pautaService.iniciarSessaoVotacao(pautaSalva.id.toString(), null);
    }

    @Test
    public void testInicioSessaoVotacaoPautaNaoExiste() {
        try {
            pautaService.iniciarSessaoVotacao("99999", null);

            fail();
        } catch (PautaNaoEncontradaException e) {
            Assertions.assertNotNull(e);
        }
    }

    @Test
    public void testInicioSessaoVotacaoSessaoJaExiste() {
        try {
            Pauta pautaSalva = pautaService.criarNovaPauta(Pauta.builder().titulo("Pauta Nova").descricao("Descricao da pauta").build());

            pautaService.iniciarSessaoVotacao(pautaSalva.id.toString(), null);

            pautaService.iniciarSessaoVotacao(pautaSalva.id.toString(), LocalDateTime.now());

            fail();
        } catch (JaExisteSessaoException e) {
            Assertions.assertNotNull(e);
        }
    }

    @Test
    public void testVotacaoEstaFuncionando() {
        Pauta pautaSalva = pautaService.criarNovaPauta(Pauta.builder().titulo("Pauta Nova 1").descricao("Descricao da pauta").build());
        pautaService.iniciarSessaoVotacao(pautaSalva.id.toString(), LocalDateTime.now().plusSeconds(120));
        pautaService.votar(pautaSalva.id.toString(), Voto.builder()
                .opcaoVoto(OpcaoVoto.SIM)
                .dataHora(LocalDateTime.now())
                .idEleitor(100L)
                .build());
    }

    @Test
    public void testVotacaoEvitarDuplos() {
        Voto votoDuplo = Voto.builder()
                .opcaoVoto(OpcaoVoto.SIM)
                .dataHora(LocalDateTime.now())
                .idEleitor(100L)
                .build();
        Pauta pautaSalva = pautaService.criarNovaPauta(Pauta.builder().titulo("Pauta Nova 1").descricao("Descricao da pauta").build());
        pautaService.iniciarSessaoVotacao(pautaSalva.id.toString(), LocalDateTime.now().plusSeconds(120));
        pautaService.votar(pautaSalva.id.toString(), votoDuplo);

        try {
            pautaService.votar(pautaSalva.id.toString(), votoDuplo);
            fail();
        } catch (EleitorJaVotouException e) {
            Assertions.assertNotNull(e);
        }
    }

    @Test
    public void testVotacaoEncerrada() {
        Voto votoDuplo = Voto.builder()
                .opcaoVoto(OpcaoVoto.SIM)
                .dataHora(LocalDateTime.now())
                .idEleitor(100L)
                .build();
        Pauta pautaSalva = pautaService.criarNovaPauta(Pauta.builder().titulo("Pauta Nova 1").descricao("Descricao da pauta").build());
        pautaService.iniciarSessaoVotacao(pautaSalva.id.toString(), LocalDateTime.now().plusSeconds(5));
        try {
            Thread.sleep(6000);

            pautaService.votar(pautaSalva.id.toString(), votoDuplo);

            fail();
        } catch (SessaoFechadaException e) {
            Assertions.assertNotNull(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
