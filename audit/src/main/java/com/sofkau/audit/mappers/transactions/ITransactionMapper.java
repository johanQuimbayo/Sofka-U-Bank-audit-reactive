package com.sofkau.audit.mappers.transactions;

import com.sofkau.audit.commons.dtos.entry.TransactionRequestDTO;
import com.sofkau.audit.commons.dtos.exit.TransactionExitDTO;
import com.sofkau.audit.data.models.Account;
import com.sofkau.audit.data.models.Transaction;

public interface ITransactionMapper {

    Transaction toEntity(TransactionRequestDTO transactionRequestDTO, Account account);

    TransactionExitDTO toExitDTO(Transaction transaction);
}
