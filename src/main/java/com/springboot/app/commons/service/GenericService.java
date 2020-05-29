package com.springboot.app.commons.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GenericService<T, ID>  {

	T save(T t) throws ResourceNotFoundException;

	void saveAll(List<T> t);

	Boolean delete(T t);

	Boolean deleteById(ID id);

	Boolean deleteAll(List<T> listT);

	T findById(ID id);

	List<T> findAll();

	Page<T> findAll(Pageable pageable);
}
