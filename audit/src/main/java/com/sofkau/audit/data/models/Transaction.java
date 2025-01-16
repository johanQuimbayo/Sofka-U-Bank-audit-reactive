package com.sofkau.audit.data.models;

import com.sofkau.audit.commons.constans.TransactionsType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "transaction")
public class Transaction {

    @Id
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
