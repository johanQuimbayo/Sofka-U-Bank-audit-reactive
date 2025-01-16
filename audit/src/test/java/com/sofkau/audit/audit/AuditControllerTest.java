package com.sofkau.audit.audit;

import com.sofkau.audit.commons.constans.TransactionsType;
import com.sofkau.audit.commons.dtos.exit.AccountBalanceExitDTO;
import com.sofkau.audit.commons.dtos.exit.TransactionExitDTO;
import com.sofkau.audit.controllers.audit.impl.AuditController;
import com.sofkau.audit.exceptions.NotFoundException;
import com.sofkau.audit.services.audit.IAuditServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebFluxTest(AuditController.class)
@ExtendWith(SpringExtension.class)
class AuditControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private IAuditServices auditServices;

    private static final String BASE_URL = "/api/audit";

    @Test
    void getAccountBalance_successful() {
        // Arrange
        int accountNumber = 1532287967;
        AccountBalanceExitDTO responseDTO = AccountBalanceExitDTO.builder()
                .userDocument(1001112312)
                .accountNumber(accountNumber)
                .balance(1500.0)
                .build();

        Mockito.when(auditServices.getAccountBalance(accountNumber))
                .thenReturn(Flux.just(responseDTO));

        // Act & Assert
        webTestClient.get()
                .uri(BASE_URL + "/balance/" + accountNumber)
                .exchange()
                .expectStatus().isOk()
                .expectBody(AccountBalanceExitDTO.class)
                .value(result -> {
                    assertEquals(1001112312, result.getUserDocument());
                    assertEquals(accountNumber, result.getAccountNumber());
                    assertEquals(1500.0, result.getBalance());
                });
    }

    @Test
    void getAccountBalance_accountNotFound() {
        // Arrange
        int accountNumber = 9999; // Non-existent account

        Mockito.when(auditServices.getAccountBalance(accountNumber))
                .thenReturn(Flux.error(new NotFoundException("Account not found")));

        // Act & Assert
        webTestClient.get()
                .uri(BASE_URL + "/balance/" + accountNumber)
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("Account not found")));
    }

    @Test
    void getAuditAccount_successful() {
        // Arrange
        int accountNumber = 1532287967;
        TransactionExitDTO transaction1 = TransactionExitDTO.builder()
                .id("1")
                .userDocument(1001112312)
                .accountNumber(accountNumber)
                .amount(500.0)
                .type(TransactionsType.DEPOSIT)
                .isSuccess(true)
                .dateOperation(new Date())
                .preBalance(1000.0)
                .postBalance(1500.0)
                .build();

        TransactionExitDTO transaction2 = TransactionExitDTO.builder()
                .id("2")
                .userDocument(1001112312)
                .accountNumber(accountNumber)
                .amount(300.0)
                .type(TransactionsType.WITHDRAW)
                .isSuccess(true)
                .dateOperation(new Date())
                .preBalance(1500.0)
                .postBalance(1200.0)
                .build();

        Mockito.when(auditServices.getAuditAccount(accountNumber))
                .thenReturn(Flux.just(transaction1, transaction2));

        webTestClient.get()
                .uri(BASE_URL + "/history/" + accountNumber)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TransactionExitDTO.class)
                .value(transactions -> {
                    assertEquals(2, transactions.size());
                    assertEquals("1", transactions.get(0).getId());
                    assertEquals("2", transactions.get(1).getId());
                });
    }

    @Test
    void getAuditAccount_errorAccessingLogs() {

        int accountNumber = 1532287967;

        Mockito.when(auditServices.getAuditAccount(accountNumber))
                .thenReturn(Flux.error(new RuntimeException("Error accessing audit logs")));


        webTestClient.get()
                .uri(BASE_URL + "/history/" + accountNumber)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .value(response -> assertTrue(response.contains("Internal Server Error")));
    }
}

