package com.jselva.nisum.evaluationtest.data.repositories;

import com.jselva.nisum.evaluationtest.data.entity.Role;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
    Optional<Role> findByName(String name);
}