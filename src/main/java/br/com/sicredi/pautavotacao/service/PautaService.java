package br.com.sicredi.pautavotacao.service;

import br.com.sicredi.pautavotacao.domain.Pauta;
import br.com.sicredi.pautavotacao.domain.SessaoVotacao;
import br.com.sicredi.pautavotacao.domain.Voto;
import br.com.sicredi.pautavotacao.exceptions.BussinesException;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

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

    public Pauta getPauta(String id) {
        return Pauta.findById(Long.valueOf(id));
    }

    @Transactional
    public void iniciarSessaoVotacao(String idPauta, LocalDateTime dataFechamento) {
        Pauta pauta = Pauta.findById(Long.valueOf(idPauta));

        if(pauta == null) {
            throw new BussinesException(String.format("Pauta %s informada não existe.", idPauta));
        }

        if(pauta.getSessaoVotacao() != null) {
            throw new BussinesException(String.format("Já existe sessão para esta pauta %s.", pauta));
        }

        if(dataFechamento != null && LocalDateTime.now().isAfter(dataFechamento)) {
            throw new BussinesException("Data de fechamento da sessão não pode ser inferior a atual atual.");
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

    @Transactional
    public void votar(String idPauta, Voto voto) {
        Pauta pauta = Pauta.findById(Long.valueOf(idPauta));

        if(pauta == null) {
            throw new BussinesException(String.format("Pauta %s informada não existe.", idPauta));
        }

        if(pauta.getSessaoVotacao() == null) {
            throw new BussinesException(String.format("Não existe sessão aberta para esta pauta %s.", pauta));
        }

        if(LocalDateTime.now().isAfter(pauta.getSessaoVotacao().getDataFechamento())) {
            throw new BussinesException("Sessão encontra-se fechada, não é possível realizar mais votações na sessão.");
        }

        if(jaVotou(pauta, voto)){
            throw new BussinesException("Eleitor já votou.");
        }

        efetivarVoto(pauta, voto);
    }

    private boolean jaVotou(Pauta pauta, Voto voto) {
        return Voto.find("sessaoVotacao = ?1 and idEleitor = ?2", pauta.getSessaoVotacao(), voto.getIdEleitor()).count() > 0;
    }

    private void efetivarVoto(Pauta pauta, Voto voto) {
        Voto.builder()
            .sessaoVotacao(pauta.getSessaoVotacao())
            .dataHora(LocalDateTime.now())
            .opcaoVoto(voto.getOpcaoVoto())
            .idEleitor(voto.getIdEleitor())
            .build()
            .persist();
    }
}
