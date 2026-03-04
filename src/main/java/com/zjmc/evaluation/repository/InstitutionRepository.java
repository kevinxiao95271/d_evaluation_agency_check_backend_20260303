package com.zjmc.evaluation.repository;

import com.zjmc.evaluation.entity.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {

    List<Institution> findByTypeAndStatus(Institution.InstitutionType type, Integer status);

    List<Institution> findByStatus(Integer status);

    boolean existsByName(String name);
}
