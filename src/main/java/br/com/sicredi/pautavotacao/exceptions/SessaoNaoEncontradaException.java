package br.com.sicredi.pautavotacao.exceptions;

public class SessaoNaoEncontradaException extends BussinesException {
    public SessaoNaoEncontradaException() {
        super("Sessão de votação não encontrada.");
    }
}