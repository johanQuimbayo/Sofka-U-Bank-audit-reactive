package com.sofkau.audit.data.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
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
        private  String id;

        private Integer accountNumber;

        private Double balance;

        private String type;

        private Integer userDocument;
}
