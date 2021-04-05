package br.comsicredi.sincronizacaoreceita.service.ifaces;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IArquivoSevice {

    InputStream ler(String diretorio, String validarTipo) throws FileNotFoundException;

    void gravar(String deretorio, List<String> linhas) throws IOException;

}
