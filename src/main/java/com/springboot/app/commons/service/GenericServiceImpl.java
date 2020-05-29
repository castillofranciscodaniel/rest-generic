package com.springboot.app.commons.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

public abstract class GenericServiceImpl<T, ID> implements GenericService<T, ID> {

	private PagingAndSortingRepository<T, ID> mongoRepository;

	public GenericServiceImpl(PagingAndSortingRepository<T, ID> mongoRepository) {
		this.mongoRepository = mongoRepository;
	}

	@Override
	@Transactional
	public T save(T t) throws ResourceNotFoundException {
		try {
			return this.mongoRepository.save(t);
		} catch (DataAccessException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: " + e);
		}
	}

	@Override
	@Transactional
	public void saveAll(List<T> t) {
		try {
			this.mongoRepository.saveAll(t);
		} catch (Exception e) {

		}
	}

	@Override
	@Transactional
	public Boolean delete(T t) {
		try {
			this.mongoRepository.delete(t);
			return true;
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error id no debe ser null. Error: " + e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servidor. Error: " + e);
		}
	}

	@Override
	@Transactional
	public Boolean deleteById(ID id) {
		try {
			this.mongoRepository.deleteById(id);
			return true;
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error id no debe ser null. Error: " + e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servidor. Error: " + e);
		}

	}

	@Override
	@Transactional
	public Boolean deleteAll(List<T> listT) {
		try {
			// this.mongoRepository.deleteInBatch(listT);
			return true;
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: " + e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servidor. Error: " + e);
		}

	}

	@Override
	@Transactional(readOnly = true)
	public T findById(ID id) {
		try {
			Optional<T> tOptional = this.mongoRepository.findById(id);
			return tOptional.orElse(null);

		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error id no debe ser null. Error: " + e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error en el servidor. Error: " + e);
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findAll() {
		try {
			return (List<T>) this.mongoRepository.findAll();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Page<T> findAll(Pageable pageable) {
		try {
			return this.mongoRepository.findAll(pageable);
		} catch (Exception e) {
			return null;
		}
	}

}
