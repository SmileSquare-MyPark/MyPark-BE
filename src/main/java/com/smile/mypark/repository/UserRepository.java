package com.smile.mypark.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smile.mypark.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByuIdx(Long uIdx);

	Optional<User> findByuId(String uId);

	Optional<User> findByIdx(Long idx);

	boolean existsByuId(String uId);

	boolean existsByuIdx(Long uIdx);
}