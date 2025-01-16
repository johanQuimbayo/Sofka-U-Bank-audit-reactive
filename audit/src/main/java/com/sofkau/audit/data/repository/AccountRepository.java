package com.sofkau.audit.data.repository;

import com.sofkau.audit.data.models.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AccountRepository  extends ReactiveMongoRepository<Account, String> {


    public Mono<Account> findByAccountNumber (Integer accountNumber);

    @Tailable
    public Flux<Account> findWithTailableCursorByAccountNumber (Integer accountNumber);
}
