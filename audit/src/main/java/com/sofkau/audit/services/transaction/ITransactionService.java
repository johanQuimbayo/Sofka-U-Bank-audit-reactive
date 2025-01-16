package com.sofkau.audit.services.transaction;

import com.sofkau.audit.commons.dtos.entry.TransactionRequestDTO;
import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;
import reactor.core.publisher.Mono;

public interface ITransactionService {

    Mono<AccountBalanceExitDTO> transaction(TransactionRequestDTO transactionRequestDTO);
}
