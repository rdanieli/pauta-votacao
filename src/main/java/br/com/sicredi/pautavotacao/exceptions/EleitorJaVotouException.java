package br.com.sicredi.pautavotacao.exceptions;

public class EleitorJaVotouException extends BussinesException {
    public EleitorJaVotouException() {
        super("Eleitor já efetuou o seu voto.");
    }
}