package com.study.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.study.bank.domain.Extrato;

public interface ExtratoRepository extends JpaRepository<Extrato, String>, JpaSpecificationExecutor<Extrato> {

}
