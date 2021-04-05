package br.comsicredi.sincronizacaoreceita.service.ifaces;

public interface IReceitaService {

    boolean atualizarConta(String agencia, String conta, double saldo, String status)
            throws RuntimeException, InterruptedException;

}
