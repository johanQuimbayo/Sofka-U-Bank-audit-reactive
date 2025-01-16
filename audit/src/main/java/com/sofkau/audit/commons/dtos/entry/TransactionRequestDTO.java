package com.sofkau.audit.commons.dtos.entry;

import com.sofkau.audit.commons.constans.TransactionsType;
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

    private Integer userDocument;

    private Integer accountNumber;

    private Double amount;

    private TransactionsType type;

}
