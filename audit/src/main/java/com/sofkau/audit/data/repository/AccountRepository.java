package com.sofkau.audit.data.repository;

import com.sofkau.audit.data.models.Account;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Mono;

import java.util.Optional;

public interface AccountRepository  extends ReactiveMongoRepository<Account, ObjectId> {

    public Mono<Account> findByAccountNumber(Integer accountNumber);
}
