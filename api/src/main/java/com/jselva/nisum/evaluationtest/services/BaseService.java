package com.jselva.nisum.evaluationtest.services;

import java.util.List;
import java.util.Optional;

public interface BaseService<T, D> {
    Optional<T> save(D d);

    Optional<T> find(String name);

    List<T> getAll();

    void remove(String name);

    T convertToEntity(D dto);

    D convertToDto(T entity);
}
