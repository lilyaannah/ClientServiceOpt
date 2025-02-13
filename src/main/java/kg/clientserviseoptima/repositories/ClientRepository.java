package kg.clientserviseoptima.repositories;

import jakarta.transaction.Transactional;
import kg.clientserviseoptima.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("SELECT c FROM Client c WHERE c.clientPhoneNumber = :clientPhoneNumber")
    Optional<Client> findByClientPhoneNumber(@Param("clientPhoneNumber") String clientPhoneNumber);

    @Query("SELECT c FROM Client c WHERE c.clientPhoneNumber = :clientPhoneNumber AND c.clientStatus <> 'DELETED'")
    Optional<Client> findByClientPhoneNumberAndStatusNotDeleted(@Param("clientPhoneNumber") String clientPhoneNumber);

    @Modifying
    @Transactional
    @Query("UPDATE Client c SET c.lastActiveTime = :time WHERE c.clientPhoneNumber = :phone")
    void updateLastActiveTime(@Param("phone") String phone, @Param("time") LocalDateTime time);
}
