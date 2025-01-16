package com.sofkau.audit.data.repository;

import com.sofkau.audit.data.models.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository  extends ReactiveMongoRepository<Account, String> {

    @Tailable
    public Mono<Account> findWithTailableCursorByAccountNumber(Integer accountNumber);
}
