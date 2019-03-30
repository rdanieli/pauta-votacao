package br.com.sicredi.pautavotacao.exceptions;

public class JaExisteSessaoException extends BussinesException {
    public JaExisteSessaoException(String mensagem) {
        super(String.format("Já existe sessão para esta pauta %s.", mensagem));
    }
}