package com.sofkau.audit.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "account")
public class Account {
        @Id
        private  Object id;

        private Integer accountNumber;

        private Double balance;

        private String type;

        private Integer userDocument;
}
