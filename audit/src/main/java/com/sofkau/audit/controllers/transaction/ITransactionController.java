package com.sofkau.audit.controllers.transaction;

import com.sofkau.audit.commons.dtos.entry.TransactionRequestDTO;
import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;
import reactor.core.publisher.Mono;

public interface ITransactionController {

    Mono<AccountBalanceExitDTO> transaction(TransactionRequestDTO transactionRequestDTO);
}
