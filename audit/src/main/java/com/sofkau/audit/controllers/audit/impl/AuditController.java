package com.sofkau.audit.controllers.audit.impl;

import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;
import com.sofkau.audit.commons.dtos.exit.TransactionExitDTO;
import com.sofkau.audit.controllers.audit.IAuditController;
import com.sofkau.audit.services.audit.IAuditServices;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@AllArgsConstructor
@RestController
@RequestMapping("/api/audit")
public class AuditController implements IAuditController {

    @Autowired
    private IAuditServices auditServices;


    @Override
    @GetMapping("balance")
    public Mono<AccountBalanceExitDTO> getAccountBalance(@Validated @PathVariable Integer accountNumber) {
        return auditServices.getAccountBalance(accountNumber);
    }

    @Override
    @GetMapping("/Audit")
    public Flux<TransactionExitDTO> getAuditAccount(@Validated @PathVariable Integer accountNumber) {
        return auditServices.getAuditAccount(accountNumber);
    }
}
