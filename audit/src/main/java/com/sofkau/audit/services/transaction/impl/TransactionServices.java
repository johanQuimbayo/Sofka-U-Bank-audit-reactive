package com.sofkau.audit.services.transaction.impl;

import com.sofkau.audit.commons.constans.TransactionsType;
import com.sofkau.audit.commons.dtos.entry.TransactionRequestDTO;
import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;
import com.sofkau.audit.data.models.Account;
import com.sofkau.audit.data.models.Transaction;
import com.sofkau.audit.data.repository.AccountRepository;
import com.sofkau.audit.data.repository.TransactionRepository;
import com.sofkau.audit.exceptions.NotFoundException;
import com.sofkau.audit.mappers.transactions.ITransactionMapper;
import com.sofkau.audit.services.transaction.ITransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class TransactionServices implements ITransactionService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ITransactionMapper transactionMapper;




    @Override
    public Mono<AccountBalanceExitDTO> transaction(TransactionRequestDTO transactionRequestDTO) {

        return validateTransactionAmount(transactionRequestDTO)
                .then(validateTransactionAccount(transactionRequestDTO.getAccountNumber()))
                .flatMap(account -> updateAccount(account, transactionRequestDTO))
                .flatMap( account ->   saveTransaction(transactionRequestDTO,account))
                .map(account ->AccountBalanceExitDTO.builder()
                            .userDocument(account.getUserDocument())
                            .accountNumber(account.getAccountNumber())
                            .balance(account.getBalance()).build());
    }


    private Mono<Void> validateTransactionAmount(TransactionRequestDTO transactionRequestDTO) {
        if (transactionRequestDTO.getAmount() <= 0) {
            return Mono.error(new IllegalArgumentException("Deposit amount must be greater than zero"));
        }
        return Mono.empty();
    }

    private Mono<Account> validateTransactionAccount(Integer accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .switchIfEmpty( Mono.error(new NotFoundException("Account not found")));
    }

    private Mono<Account> updateAccount(Account account, TransactionRequestDTO transactionRequestDTO) {
        if (transactionRequestDTO.getType().equals(TransactionsType.DEPOSIT)) {
            account.setBalance(account.getBalance() + transactionRequestDTO.getAmount());
        }else {
            if( transactionRequestDTO.getAmount() > account.getBalance()) {
                return Mono.error(new IllegalArgumentException("the withdraw cant be greater than the account balance"));
            }
            account.setBalance(account.getBalance() - transactionRequestDTO.getAmount());
        }
        return accountRepository.save(account);
    }

    private Mono<Account> saveTransaction(TransactionRequestDTO transactionRequestDTO, Account account) {
        return Mono.defer(() -> {
            Transaction transaction = transactionMapper.toEntity(transactionRequestDTO, account);
            return transactionRepository.insert(transaction).thenReturn(account);
        });
    }
}
