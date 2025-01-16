package com.sofkau.audit.services.audit.impl;

import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;
import com.sofkau.audit.commons.dtos.exit.TransactionExitDTO;
import com.sofkau.audit.data.repository.AccountRepository;
import com.sofkau.audit.data.repository.TransactionRepository;
import com.sofkau.audit.exceptions.NotFoundException;
import com.sofkau.audit.mappers.transactions.ITransactionMapper;
import com.sofkau.audit.services.audit.IAuditServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Service
public class AuditServices implements IAuditServices {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ITransactionMapper transactionMapper;


    @Override
    public Flux<AccountBalanceExitDTO> getAccountBalance(Integer accountNumber) {
        return accountRepository.findWithTailableCursorByAccountNumber(accountNumber)
                .switchIfEmpty(Flux.error(new NotFoundException("Account not found")))
                .map(account -> AccountBalanceExitDTO.builder()
                .userDocument(account.getUserDocument())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance()).build());
    }


    @Override
    public Flux<TransactionExitDTO> getAuditAccount(Integer accountNumber) {
        return transactionRepository.findWithTailableCursorByAccountNumber(accountNumber)
                .onErrorResume(e -> {
                    System.out.println("error " + e.getMessage());
                    return Flux.error(new RuntimeException("Error accessing audit logs"));
                })
                .map(transaction -> transactionMapper.toExitDTO(transaction));
    }
}
