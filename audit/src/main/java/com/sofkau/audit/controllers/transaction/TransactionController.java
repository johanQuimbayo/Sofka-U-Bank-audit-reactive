package com.sofkau.audit.controllers.transaction;


import com.sofkau.audit.commons.dtos.entry.TransactionRequestDTO;
import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;
import com.sofkau.audit.services.transaction.ITransactionService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/api/transaction")
public class TransactionController implements ITransactionController {

    @Autowired
    private ITransactionService transactionService;


    @Override
    @PostMapping()
    public Mono<AccountBalanceExitDTO> transaction(@Validated @RequestBody TransactionRequestDTO transactionRequestDTO) {
        return transactionService.transaction( transactionRequestDTO);
    }
}
