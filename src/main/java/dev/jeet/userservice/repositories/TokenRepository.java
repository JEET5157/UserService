package dev.jeet.userservice.repositories;

import dev.jeet.userservice.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Date;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    Token save(Token token);

    Optional<Token> findByValueAndDeletedAndExpiryAtGreaterThan(String tokenValue, boolean deleted, Date date);

    Optional<Token> findByValueAndDeleted(String tokenValue, boolean deleted);
}
