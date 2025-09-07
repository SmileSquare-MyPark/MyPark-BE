package com.smile.mypark.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smile.mypark.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByProviderId(String providerId);

	@Query("SELECT u FROM User u WHERE u.id = :id")
	Optional<User> findByIdAndNotDeleted(@Param("id") Long id);
}