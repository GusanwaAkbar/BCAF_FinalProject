package coid.bcafinance.mgaspringfinalexam.core;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;

public interface IService<T> {
    ResponseEntity<Object> save(T t, HttpServletRequest request); //001 - 010
    ResponseEntity<Object> findById(Long id, HttpServletRequest request); //011 - 020
    ResponseEntity<Object> delete(Long id, HttpServletRequest request); //021 - 030
    ResponseEntity<Object> update(Long id, T t, HttpServletRequest request); //031 - 040
    ResponseEntity<Object> find(Pageable pageable, String filterBy, String value, HttpServletRequest request);

}

/*
Created By IntelliJ IDEA 2023.2.5 (Ultimate Edition)
@Author farha a.k.a. Farkhan Hamzah Firdaus
Java Developer
Crated on 10/03/2024 20:38
@Last Modified 10/03/2024 20:38
Version 1.0
*/