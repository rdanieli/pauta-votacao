package br.com.sicredi.pautavotacao.exceptions;

public class BussinesException extends RuntimeException {
    public BussinesException(String mensagem) {
        super(mensagem, null, true, false);
    }
}
