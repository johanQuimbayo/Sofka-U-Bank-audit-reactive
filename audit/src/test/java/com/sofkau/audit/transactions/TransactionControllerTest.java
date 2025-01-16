package com.sofkau.audit.transactions;

import com.sofkau.audit.commons.constans.TransactionsType;
import com.sofkau.audit.commons.dtos.entry.TransactionRequestDTO;
import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;
import com.sofkau.audit.controllers.transaction.impl.TransactionController;
import com.sofkau.audit.exceptions.NotFoundException;
import com.sofkau.audit.services.transaction.ITransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebFluxTest(TransactionController.class)
@ExtendWith(SpringExtension.class)
class TransactionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ITransactionService transactionService;

    private static final String BASE_URL = "/api/transaction";

    @Test
    void transaction_successful() {
        // Arrange
        TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                .userDocument(1001112312)
                .accountNumber(1532287967)
                .amount(500.0)
                .type(TransactionsType.DEPOSIT)
                .build();

        AccountBalanceExitDTO responseDTO = AccountBalanceExitDTO.builder()
                .userDocument(1001112312)
                .accountNumber(1532287967)
                .balance(1500.0)
                .build();

        Mockito.when(transactionService.transaction(Mockito.any(TransactionRequestDTO.class)))
                .thenReturn(Mono.just(responseDTO));

        // Act & Assert
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(AccountBalanceExitDTO.class)
                .value(result -> {
                    assertEquals(1001112312, result.getUserDocument());
                    assertEquals(1532287967, result.getAccountNumber());
                    assertEquals(1500.0, result.getBalance());
                });
    }

    @Test
    void transaction_validationError_negativeAmount() {
        // Arrange
        TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                .userDocument(1001112312)
                .accountNumber(1532287967)
                .amount(-500.0)
                .type(TransactionsType.DEPOSIT)
                .build();

        // Act & Assert
        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void transaction_accountNotFound() {
        TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                .userDocument(1001112312)
                .accountNumber(9999) // Non-existent account
                .amount(500.0)
                .type(TransactionsType.DEPOSIT)
                .build();

        Mockito.when(transactionService.transaction(Mockito.any(TransactionRequestDTO.class)))
                .thenReturn(Mono.error(new NotFoundException("Account not found")));


        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("Account not found")));
    }

    @Test
    void transaction_insufficientFunds() {

        TransactionRequestDTO requestDTO = TransactionRequestDTO.builder()
                .userDocument(1001112312)
                .accountNumber(1532287967)
                .amount(2000.0) // Amount greater than balance
                .type(TransactionsType.WITHDRAW)
                .build();

        Mockito.when(transactionService.transaction(Mockito.any(TransactionRequestDTO.class)))
                .thenReturn(Mono.error(new IllegalArgumentException("The withdraw can't be greater than the account balance")));


        webTestClient.post()
                .uri(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("Internal Server Error")));
    }
}

