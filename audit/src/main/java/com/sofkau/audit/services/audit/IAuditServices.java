package com.sofkau.audit.services.audit;

import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;
import com.sofkau.audit.commons.dtos.exit.TransactionExitDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface IAuditServices {



    Mono<AccountBalanceExitDTO> getAccountBalance(Integer accountId);

    Flux<TransactionExitDTO> getAuditAccount(Integer accountId);
}
