package br.comsicredi.sincronizacaoreceita.service;

import br.comsicredi.sincronizacaoreceita.dto.Sincronizacao;
import br.comsicredi.sincronizacaoreceita.enumerado.SituacaoSincronizacao;
import br.comsicredi.sincronizacaoreceita.service.ifaces.IArquivoSevice;
import br.comsicredi.sincronizacaoreceita.service.ifaces.IReceitaService;
import br.comsicredi.sincronizacaoreceita.service.ifaces.ISincronizacaoService;
import br.comsicredi.sincronizacaoreceita.utils.Utilitario;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class SincronizacaoService implements ISincronizacaoService {

    @Autowired
    private IArquivoSevice arquivoSevice;

    @Autowired
    private IReceitaService receitaService;

    @Autowired
    private MessageSource messageSource;

    public void sincronizarDados(String diretorioArquivo) {
        var mensagem = "";
        InputStream arquivo;
        List<Sincronizacao> sincronizacaos = new ArrayList<>();
        try {
            arquivo = arquivoSevice.ler(diretorioArquivo, ArquivoService.TIPO_AQUIVO);
            sincronizacaos = converterAquivoParaObjeto(arquivo);

            sincronizacaos.parallelStream().forEach(sinc -> {
                try {
                    var sincronizado = receitaService.atualizarConta(sinc.getAgencia(), sinc.getConta(), sinc.getSaldo().doubleValue(), sinc.getStatus());
                    sinc.setSituacao(sincronizado ? SituacaoSincronizacao.SINCRONIZADO : SituacaoSincronizacao.NAO_SINCRONIZADO);
                } catch (InterruptedException e) {
                    sinc.setSituacao(SituacaoSincronizacao.ERRO_NO_SERVICO);
                    var mensagemErro = messageSource.getMessage("sincronizacao.nao.finalizada", null, LocaleContextHolder.getLocale());
                    log.error(mensagemErro);
                }
            });

            List<String> linhas = montarCSV(sincronizacaos);
            arquivoSevice.gravar(diretorioArquivo,linhas);
        } catch (FileNotFoundException e) {
            mensagem = messageSource.getMessage("arquivo.nao.encontrado", null, LocaleContextHolder.getLocale());
            log.error(mensagem);
        } catch (IOException e) {
            mensagem = messageSource.getMessage("erro.arquivo.sincronizado", null, LocaleContextHolder.getLocale());
            log.error(mensagem);
        }

        mensagem = messageSource.getMessage("sucesso.sincronizacao", null, LocaleContextHolder.getLocale());
        log.info(mensagem);
    }

    private List<Sincronizacao> converterAquivoParaObjeto(InputStream arquivo){
        List<Sincronizacao> sincronizacoes = new ArrayList<>();
        String[] linha;

        try {
            CSVReader csvReader = new CSVReader(new InputStreamReader(arquivo));
            csvReader.skip(1);
            while ((linha = csvReader.readNext()) != null){
                linha = linha[0].replace("\"","").split(ArquivoService.SEPARADOR_CSV);
                sincronizacoes.add(new Sincronizacao(linha[0],linha[1],new BigDecimal(Utilitario.tratarValorMonetario(linha[2])), linha[3],null));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }

        return  sincronizacoes;
    }

    private List<String> montarCSV(List<Sincronizacao> sincronizacaos) throws IOException {
        List<String> linhas = new ArrayList<>();
        linhas.add("\"AGENCIA\";\"CONTA\";\"VALOR\";\"TIPOS\";\"STATUS\"");
        StringBuilder linha;
        for (var sinc :sincronizacaos) {
            linha = new StringBuilder();
            linha.append(sinc.getAgencia());
            linha.append(ArquivoService.SEPARADOR_CSV);
            linha.append(sinc.getConta());
            linha.append(ArquivoService.SEPARADOR_CSV);
            linha.append(sinc.getSaldo().toString());
            linha.append(ArquivoService.SEPARADOR_CSV);
            linha.append(sinc.getStatus());
            linha.append(ArquivoService.SEPARADOR_CSV);
            linha.append(sinc.getSituacao().toString());
            linhas.add(linha.toString());
        }
        return linhas;
    }

}
