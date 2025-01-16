package com.sofkau.audit.commons.dtos.entry;

import com.sofkau.audit.commons.constans.TransactionsType;


import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class TransactionRequestDTO {

    @NotNull
    private Integer userDocument;

    @NotNull
    private Integer accountNumber;

    @NotNull
    @DecimalMin(value = "0.01", message = "The amount must be greater than zero")
    private Double amount;

    @NotNull
    private TransactionsType type;

}
