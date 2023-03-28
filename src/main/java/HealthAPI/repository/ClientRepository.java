package HealthAPI.repository;

import HealthAPI.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByEmail(String email);

    Client findByTokens(String jwt);

    List<Client> findByDeleted(Boolean deleted);

    Optional<Client> findById(Long id);

    Optional<Client> findByIdAndDeleted(Long id, boolean deleted);
}