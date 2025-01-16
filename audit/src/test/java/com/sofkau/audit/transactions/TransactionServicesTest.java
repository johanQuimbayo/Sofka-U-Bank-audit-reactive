package com.sofkau.audit.transactions;

import com.sofkau.audit.commons.constans.TransactionsType;
import com.sofkau.audit.commons.dtos.entry.TransactionRequestDTO;
import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;;
import com.sofkau.audit.data.models.Account;
import com.sofkau.audit.data.models.Transaction;
import com.sofkau.audit.data.repository.AccountRepository;
import com.sofkau.audit.data.repository.TransactionRepository;
import com.sofkau.audit.exceptions.NotFoundException;
import com.sofkau.audit.mappers.transactions.ITransactionMapper;
import com.sofkau.audit.services.transaction.impl.TransactionServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@ExtendWith(MockitoExtension.class)
class TransactionServicesTest {

    @InjectMocks
    private TransactionServices transactionServices;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ITransactionMapper transactionMapper;

    @Test
    void transaction_successfulDeposit() {

        TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                .userDocument(12345678)
                .accountNumber(1001)
                .amount(500.0)
                .type(TransactionsType.DEPOSIT)
                .build();

        Account existingAccount = new Account("1", 1001, 1000.0, "SAVINGS", 12345678);
        Account updatedAccount = new Account("1", 1001, 1500.0, "SAVINGS", 12345678);

        Transaction transaction = new Transaction();
        AccountBalanceExitDTO expectedResponse = AccountBalanceExitDTO.builder()
                .userDocument(12345678)
                .accountNumber(1001)
                .balance(1500.0)
                .build();

        Mockito.when(accountRepository.findByAccountNumber(1001))
                .thenReturn(Mono.just(existingAccount));
        Mockito.when(accountRepository.save(Mockito.any(Account.class)))
                .thenReturn(Mono.just(updatedAccount));
        Mockito.when(transactionMapper.toEntity(Mockito.any(TransactionRequestDTO.class), Mockito.any(Account.class)))
                .thenReturn(transaction);
        Mockito.when(transactionRepository.insert(transaction))
                .thenReturn(Mono.just(transaction));


        Mono<AccountBalanceExitDTO> result = transactionServices.transaction(requestDTO);


        StepVerifier.create(result)
                .expectNext(expectedResponse)
                .verifyComplete();
    }

    @Test
    void transaction_accountNotFound() {
        // Arrange
        TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                .userDocument(12345678)
                .accountNumber(9999) // Non-existent account
                .amount(500.0)
                .type(TransactionsType.DEPOSIT)
                .build();

        Mockito.when(accountRepository.findByAccountNumber(9999))
                .thenReturn(Mono.empty());

        // Act
        Mono<AccountBalanceExitDTO> result = transactionServices.transaction(requestDTO);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof NotFoundException &&
                        throwable.getMessage().equals("Account not found"))
                .verify();
    }

    @Test
    void transaction_insufficientFunds() {
        // Arrange
        TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                .userDocument(12345678)
                .accountNumber(1001)
                .amount(2000.0) // Exceeds balance
                .type(TransactionsType.WITHDRAW)
                .build();

        Account existingAccount = new Account("1", 1001, 1000.0, "SAVINGS", 12345678);

        Mockito.when(accountRepository.findByAccountNumber(1001))
                .thenReturn(Mono.just(existingAccount));

        // Act
        Mono<AccountBalanceExitDTO> result = transactionServices.transaction(requestDTO);

        // Assert
        StepVerifier.create(result)
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().equals("the withdraw cant be greater than the account balance"))
                .verify();
    }

}

