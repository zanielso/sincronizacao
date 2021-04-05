package br.comsicredi.sincronizacaoreceita.utils;

public class Utilitario {

    public static String tratarValorMonetario(String valor) {
        return valor.replace(",",".");
    }
}
