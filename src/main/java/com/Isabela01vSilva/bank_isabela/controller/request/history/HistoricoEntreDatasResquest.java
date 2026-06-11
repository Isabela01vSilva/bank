package com.Isabela01vSilva.bank_isabela.controller.request.history;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record HistoricoEntreDatasResquest(Long id,
                                          LocalDateTime dataInicio,
                                          LocalDateTime dataFim) {
}
