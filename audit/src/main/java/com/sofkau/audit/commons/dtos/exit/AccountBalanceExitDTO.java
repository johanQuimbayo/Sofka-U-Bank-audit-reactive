package com.sofkau.audit.commons.dtos.exit;


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
public class AccountBalanceExitDTO {

    private Integer userDocument;

    private Integer accountNumber;

    private Double balance;

}
