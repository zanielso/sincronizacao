package br.comsicredi.sincronizacaoreceita.service;

import br.comsicredi.sincronizacaoreceita.exceptions.NegocioException;
import br.comsicredi.sincronizacaoreceita.service.ifaces.IArquivoSevice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class ArquivoService implements IArquivoSevice {

    public static final String TIPO_AQUIVO = ".CSV";
    public static final String SEPARADOR_CSV = ";";

    @Autowired
    private MessageSource messageSource;

    public InputStream ler(String diretorio, String validarTipo) throws FileNotFoundException {
        if(StringUtils.isNotBlank(validarTipo)){
            validarFormato(diretorio, TIPO_AQUIVO);
        }

        InputStream inputStream;
        inputStream = new FileInputStream(diretorio);
        return inputStream;

    }


    public void gravar(String deretorio, List<String> linhas) throws IOException {
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(deretorio),"UTF8"));
        for (var linha : linhas) {
            bw.write(linha);
            bw.newLine();
        }
        bw.flush();
        bw.close();
    }

    private void validarFormato(String arquivo, String formato){
        var isFormatoValido = arquivo.toUpperCase().endsWith(formato);
        if(!isFormatoValido){
            var mensagem = messageSource.getMessage("arquivo.deve.ser.csv", null, LocaleContextHolder.getLocale());
            throw new NegocioException(mensagem);
        }
    }
}
