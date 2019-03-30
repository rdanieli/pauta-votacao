package br.com.sicredi.pautavotacao.exceptions;

public class DataFechamentoSessaoInferiorAtualException extends BussinesException {
    public DataFechamentoSessaoInferiorAtualException() {
        super("Data de fechamento da sessão não pode ser inferior a atual.");
    }
}