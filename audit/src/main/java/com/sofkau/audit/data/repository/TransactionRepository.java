package com.sofkau.audit.data.repository;

import com.sofkau.audit.data.models.Transaction;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import reactor.core.publisher.Flux;


public interface TransactionRepository extends ReactiveMongoRepository<Transaction, ObjectId> {

    @Tailable
    public Flux<Transaction> findWithTailableCursorByAccountNumber(Integer accountNumber);
}
