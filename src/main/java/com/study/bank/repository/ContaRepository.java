package com.study.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.study.bank.domain.Conta;

public interface ContaRepository extends JpaRepository<Conta, Integer> {

}
