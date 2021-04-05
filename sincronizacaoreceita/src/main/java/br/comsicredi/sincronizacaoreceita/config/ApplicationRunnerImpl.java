package br.comsicredi.sincronizacaoreceita.config;

import br.comsicredi.sincronizacaoreceita.service.ifaces.ISincronizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {

    @Autowired
    private ISincronizacaoService sincronizacaoService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        String arquivo = args.getSourceArgs()[0];
        sincronizacaoService.sincronizarDados(arquivo);
    }
}
