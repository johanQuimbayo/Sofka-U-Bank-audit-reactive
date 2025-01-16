package com.sofkau.audit.audit;

import com.sofkau.audit.commons.constans.TransactionsType;
import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;
import com.sofkau.audit.commons.dtos.exit.TransactionExitDTO;

import com.sofkau.audit.data.models.Account;
import com.sofkau.audit.data.models.Transaction;
import com.sofkau.audit.data.repository.AccountRepository;
import com.sofkau.audit.data.repository.TransactionRepository;
import com.sofkau.audit.exceptions.NotFoundException;
import com.sofkau.audit.mappers.transactions.ITransactionMapper;
import com.sofkau.audit.services.audit.impl.AuditServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
class AuditServicesTest {

    @InjectMocks
    private AuditServices auditServices;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ITransactionMapper transactionMapper;



    @Test
    void getAccountBalance_accountNotFound() {

        int accountNumber = 9999; // Non-existent account

        Mockito.when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Mono.empty());


        Flux<AccountBalanceExitDTO> result = auditServices.getAccountBalance(accountNumber);


        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Account not found"))
                .verify();
    }

    @Test
    void getAuditAccount_successful() {

        int accountNumber = 1532287967;
        Transaction transaction1 = new Transaction("1", 12345678, accountNumber, 500.0, TransactionsType.DEPOSIT, true, new Date(), 1000.0, 1500.0);
        Transaction transaction2 = new Transaction("2", 12345678, accountNumber, 300.0, TransactionsType.WITHDRAW, true, new Date(), 1500.0, 1200.0);

        TransactionExitDTO transactionDTO1 = TransactionExitDTO.builder()
                .id("1")
                .userDocument(12345678)
                .accountNumber(accountNumber)
                .amount(500.0)
                .type(TransactionsType.DEPOSIT)
                .isSuccess(true)
                .dateOperation(transaction1.getDateOperation())
                .preBalance(1000.0)
                .postBalance(1500.0)
                .build();

        TransactionExitDTO transactionDTO2 = TransactionExitDTO.builder()
                .id("2")
                .userDocument(1001112312)
                .accountNumber(accountNumber)
                .amount(300.0)
                .type(TransactionsType.WITHDRAW)
                .isSuccess(true)
                .dateOperation(transaction2.getDateOperation())
                .preBalance(1500.0)
                .postBalance(1200.0)
                .build();

        Mockito.when(transactionRepository.findWithTailableCursorByAccountNumber(accountNumber))
                .thenReturn(Flux.just(transaction1, transaction2));
        Mockito.when(transactionMapper.toExitDTO(transaction1)).thenReturn(transactionDTO1);
        Mockito.when(transactionMapper.toExitDTO(transaction2)).thenReturn(transactionDTO2);


        Flux<TransactionExitDTO> result = auditServices.getAuditAccount(accountNumber);


        StepVerifier.create(result)
                .expectNext(transactionDTO1, transactionDTO2)
                .verifyComplete();
    }

    @Test
    void getAuditAccount_errorAccessingLogs() {

        int accountNumber = 1001;

        Mockito.when(transactionRepository.findWithTailableCursorByAccountNumber(accountNumber))
                .thenReturn(Flux.error(new RuntimeException("Error accessing audit logs")));


        Flux<TransactionExitDTO> result = auditServices.getAuditAccount(accountNumber);


        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals("Error accessing audit logs"))
                .verify();
    }
}

