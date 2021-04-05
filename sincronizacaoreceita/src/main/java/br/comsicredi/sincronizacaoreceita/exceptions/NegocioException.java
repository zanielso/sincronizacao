package br.comsicredi.sincronizacaoreceita.exceptions;

public class NegocioException extends RuntimeException {

    public NegocioException(String mensagemErro){
        super(mensagemErro);
    }

}
