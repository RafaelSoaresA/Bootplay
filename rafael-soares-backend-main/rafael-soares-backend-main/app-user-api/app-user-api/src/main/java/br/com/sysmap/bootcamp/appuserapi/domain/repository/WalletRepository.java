package br.com.sysmap.bootcamp.appuserapi.domain.repository;

import br.com.sysmap.bootcamp.appuserapi.domain.entities.Users;
import br.com.sysmap.bootcamp.appuserapi.domain.entities.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUsers(Users users);
}
