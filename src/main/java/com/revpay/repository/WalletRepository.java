package com.revpay.repository;

import com.revpay.model.entity.User;
import com.revpay.model.entity.Wallet;
import jakarta.persistence.LockModeType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUser(User user);

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.user.userId = :userId")
    Optional<Wallet> findByUserUserIdForUpdate(@Param("userId") Long userId);
}