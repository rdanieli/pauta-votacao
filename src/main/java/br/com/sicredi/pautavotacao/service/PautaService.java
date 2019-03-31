package br.com.sicredi.pautavotacao.service;

import br.com.sicredi.pautavotacao.domain.OpcaoVoto;
import br.com.sicredi.pautavotacao.domain.Pauta;
import br.com.sicredi.pautavotacao.domain.SessaoVotacao;
import br.com.sicredi.pautavotacao.domain.Voto;
import br.com.sicredi.pautavotacao.exceptions.*;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class PautaService {

    @ConfigProperty(name = "tempo.sessao.padrao.segundos")
    private Integer tempoSessaoPadrao;

    @Transactional
    public Pauta criarNovaPauta(Pauta pauta) {
        pauta.persist();
        return pauta;
    }

    public List<Pauta> getPautas() {
        return Pauta.listAll();
    }

    public Optional<Pauta> getPauta(String id) {
        return Optional.ofNullable(Pauta.findById(Long.valueOf(id)));
    }

    @Transactional
    public void iniciarSessaoVotacao(String idPauta, LocalDateTime dataFechamento) {
        Pauta pauta = getPauta(idPauta).orElseThrow(() -> new PautaNaoEncontradaException(idPauta));

        if(getSessaoVotacao(pauta).isPresent()){
            throw new JaExisteSessaoException(idPauta);
        }

        if (dataFechamento != null && LocalDateTime.now().isAfter(dataFechamento)) {
            throw new DataFechamentoSessaoInferiorAtualException();
        }

        criaSessaoVotacao(pauta, dataFechamento);
    }

    private void criaSessaoVotacao(Pauta pauta, LocalDateTime dataFechamento) {
        SessaoVotacao.builder()
                .dataAbertura(LocalDateTime.now())
                .dataFechamento(dataFechamento(dataFechamento))
                .pauta(pauta)
                .build()
                .persist();
    }

    private LocalDateTime dataFechamento(LocalDateTime dataFechamento) {
        return dataFechamento == null ? LocalDateTime.now().plusSeconds(tempoSessaoPadrao) : dataFechamento;
    }

    private Optional<SessaoVotacao> getSessaoVotacao(Pauta pauta) {
        return Optional.ofNullable(SessaoVotacao.find("pauta", pauta).firstResult());
    }

    @Transactional
    public void votar(String idPauta, Voto voto) {
        SessaoVotacao sessaoVotacao = getSessaoVotacao(getPauta(idPauta)
                .orElseThrow(() -> new PautaNaoEncontradaException(idPauta)))
                .orElseThrow(SessaoNaoEncontradaException::new);

        if (LocalDateTime.now().isAfter(sessaoVotacao.getDataFechamento())) {
            throw new SessaoFechadaException();
        }

        voto.setSessaoVotacao(sessaoVotacao);
        voto.setDataHora(LocalDateTime.now());

        if (voto.eleitorJaVotou()) {
            throw new EleitorJaVotouException();
        }

        voto.persist();
    }

    public Map<OpcaoVoto, Long> result(Pauta pauta) {
        return getSessaoVotacao(pauta).map(sv -> sv.getVotos()
                .stream()
                .collect(Collectors.groupingBy(Voto::getOpcaoVoto,
                        Collectors.counting()))).orElse(null);
    }
}
