package com.sofkau.audit.controllers.transaction.impl;


import com.sofkau.audit.commons.dtos.entry.TransactionRequestDTO;
import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;
import com.sofkau.audit.controllers.transaction.ITransactionController;
import com.sofkau.audit.services.transaction.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Validated
@AllArgsConstructor
@RestController
@RequestMapping("/api/transaction")
public class TransactionController implements ITransactionController {

    @Autowired
    private ITransactionService transactionService;


    @Override
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<AccountBalanceExitDTO> transaction(@Validated @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return transactionService.transaction( transactionRequestDTO);
    }
}
