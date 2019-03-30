package br.com.sicredi.pautavotacao.exceptions;

public class PautaNaoEncontradaException extends BussinesException {
    public PautaNaoEncontradaException(String mensagem) {
        super(String.format("Pauta %s não existe.", mensagem));
    }
}