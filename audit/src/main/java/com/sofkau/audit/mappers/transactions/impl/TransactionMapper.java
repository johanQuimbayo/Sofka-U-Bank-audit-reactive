package com.sofkau.audit.mappers.transactions.impl;

import com.sofkau.audit.commons.constans.TransactionsType;
import com.sofkau.audit.commons.dtos.entry.TransactionRequestDTO;
import com.sofkau.audit.commons.dtos.exit.TransactionExitDTO;
import com.sofkau.audit.data.models.Account;
import com.sofkau.audit.data.models.Transaction;
import com.sofkau.audit.mappers.transactions.ITransactionMapper;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TransactionMapper implements ITransactionMapper {

    @Override
    public Transaction toEntity(TransactionRequestDTO transactionRequestDTO, Account account) {
        if (transactionRequestDTO == null) {
            throw new IllegalArgumentException("transactionEntryDTO can´t be empty");
        }
        return Transaction.builder()
                .userDocument(transactionRequestDTO.getUserDocument())
                .accountNumber(transactionRequestDTO.getAccountNumber())
                .amount(transactionRequestDTO.getAmount())
                .type(transactionRequestDTO.getType())
                .isSuccess(true)
                .dateOperation(new Date())
                .preBalance(transactionRequestDTO.getType().equals(TransactionsType.DEPOSIT) ? account.getBalance() - transactionRequestDTO.getAmount() :  account.getBalance() + transactionRequestDTO.getAmount())
                .postBalance(account.getBalance())
                .build();


    }

    @Override
    public TransactionExitDTO toExitDTO(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("transaction can't be empty");
        }
        return TransactionExitDTO.builder()
                .id(transaction.getId().toString())
                .userDocument(transaction.getUserDocument())
                .accountNumber(transaction.getAccountNumber())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .isSuccess(transaction.getIsSuccess())
                .dateOperation(transaction.getDateOperation())
                .preBalance(transaction.getPreBalance())
                .postBalance(transaction.getPostBalance())
                .build();
    }
}

