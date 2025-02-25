package com.Isabela01vSilva.bank_isabela.controller.request;

import java.time.LocalDate;

public record HistoricoEntreDatasResquest(Long id,
                                          LocalDate dataInicio,
                                          LocalDate dataFim) {
}
