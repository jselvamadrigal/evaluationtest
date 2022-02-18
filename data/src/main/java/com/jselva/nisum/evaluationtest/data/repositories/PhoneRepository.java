package com.jselva.nisum.evaluationtest.data.repositories;

import com.jselva.nisum.evaluationtest.data.entity.Phone;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneRepository extends PagingAndSortingRepository<Phone, Long> {
    Optional<Phone> findByNumber(String number);
}