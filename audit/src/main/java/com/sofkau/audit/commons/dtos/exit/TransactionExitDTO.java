package com.sofkau.audit.commons.dtos.exit;

import com.sofkau.audit.commons.constans.TransactionsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class TransactionExitDTO {


    private String id;

    private Integer userDocument;

    private Integer accountNumber;

    private Double amount;

    private TransactionsType type;

    private Boolean isSuccess;

    private Date dateOperation;

    private Double preBalance;

    private Double postBalance;
}
