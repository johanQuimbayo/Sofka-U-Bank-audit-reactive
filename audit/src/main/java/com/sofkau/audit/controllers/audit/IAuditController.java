package com.sofkau.audit.controllers.audit;

import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;
import com.sofkau.audit.commons.dtos.exit.TransactionExitDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IAuditController {

    Flux<AccountBalanceExitDTO> getAccountBalance(Integer accountNumber);

    Flux<TransactionExitDTO> getAuditAccount(Integer accountNumber);

}
