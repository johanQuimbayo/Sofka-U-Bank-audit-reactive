package com.sofkau.audit.data.repository;

import com.sofkau.audit.data.models.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {

    @Tailable
    public Flux<Transaction> findWithTailableCursorByAccountNumber(Integer accountNumber);
}
