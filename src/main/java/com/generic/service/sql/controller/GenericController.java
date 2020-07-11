package com.generic.service.sql.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.generic.service.sql.models.Model;
import com.generic.service.sql.models.util.ErrorBody;
import com.generic.service.sql.service.GenericService;
import com.generic.service.sql.service.ResourceNotFoundException;

public abstract class GenericController<T extends Model<ID>, ID> {

	private GenericService<T, ID> genericService;

	public GenericController(GenericService<T, ID> genericService) {
		this.genericService = genericService;
	}

	@GetMapping(name = "list")
	public ResponseEntity<Page<T>> list(Pageable pageable) {

		Page<T> tList = this.genericService.findAll(pageable);
		return ResponseEntity.ok(this.genericService.findAll(pageable));
	}

	@GetMapping(name = "listAll", path = "listAll")
	public ResponseEntity<List<T>> list() {
		return ResponseEntity.ok(this.genericService.findAll());
	}

	@GetMapping(name = "findById", path = "{id}")
	public ResponseEntity<?> findById(@PathVariable("id") ID id) throws Exception {
		T t = this.genericService.findById(id);
		if (t == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(t);
	}

	@PostMapping(name = "create")
	public ResponseEntity<?> create(@Valid @RequestBody T t, BindingResult result) throws ResourceNotFoundException {

		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
		}

		if (t.getId() != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorBody("%s no puede tener Id", t));
		}

		T newT = this.genericService.save(t);

		return ResponseEntity.status(HttpStatus.CREATED).body(newT);
	}

	@PostMapping(name = "createBatch", path = "batch")
	public ResponseEntity<?> createBatch(@Valid @RequestBody List<T> listT, BindingResult result)
			throws ResourceNotFoundException {

		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
		}

		for (T t : listT) {
			if (t.getId() != null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorBody("%s no puede tener Id", t));
			}
		}

		this.genericService.saveAll(listT);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@PutMapping(name = "update")
	public ResponseEntity<?> update(@Valid @RequestBody T t, BindingResult result) throws ResourceNotFoundException {

		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
		}
		if (t.getId() == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorBody("%s necesita un Id", t));
		}

		T newT = this.genericService.save(t);

		if (newT == null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Error al actualizar");
		}
		return ResponseEntity.ok().body(newT);

	}

	@PutMapping(name = "updateBatch", path = "batch")
	public ResponseEntity<?> updateBatch(@Valid @RequestBody List<T> listT, BindingResult result)
			throws ResourceNotFoundException {

		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
		}

		for (T t : listT) {
			if (t.getId() == null) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorBody("%s necesita un Id", t));
			}
		}

		this.genericService.saveAll(listT);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@DeleteMapping(name = "delete")
	public ResponseEntity<?> delete(@Valid @ModelAttribute T t, BindingResult result) {
		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
		}

		this.genericService.delete(t);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping(name = "deleteById", path = "{id}")
	public ResponseEntity<?> delete(@PathVariable("id") ID id) {
		this.genericService.deleteById(id);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping(name = "deleteBatch", path = "batch")
	public ResponseEntity<?> delete(@Valid @RequestBody List<T> listT, BindingResult result) {

		if (result.hasErrors()) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
		}

		this.genericService.deleteAll(listT);
		return ResponseEntity.ok().build();
	}

}
