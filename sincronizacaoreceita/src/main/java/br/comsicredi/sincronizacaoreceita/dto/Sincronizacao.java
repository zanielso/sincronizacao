package br.comsicredi.sincronizacaoreceita.dto;

import br.comsicredi.sincronizacaoreceita.enumerado.SituacaoSincronizacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sincronizacao implements Serializable {

    private String agencia;
    private String conta;
    private BigDecimal saldo;
    private String status;
    private SituacaoSincronizacao situacao;

}
