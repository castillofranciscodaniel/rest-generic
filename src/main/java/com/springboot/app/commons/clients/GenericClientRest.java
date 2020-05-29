package com.springboot.app.commons.clients;

import java.awt.print.Pageable;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

// la primera opción es indicandole la url con la que se tiene que conectar, pero con 
// @RibbonClient no hace falta.. porque la saca de la lista de url posibles del 
// application.properties
// @FeignClient(name = "products-service", url = "${service.product.url}" + "/products")

public interface GenericClientRest<T> {

	@GetMapping(name = "list")
	Page<T> list(Pageable pageable);

	@GetMapping(name = "listAll", path = "listAll")
	List<T> list();

	@GetMapping(name = "findById", path = "{id}")
	T findById(@PathVariable("id") Long id);

	@PostMapping(name = "create")
	T create(@Valid @RequestBody T t);

	@PostMapping(name = "createBatch", path = "batch")
	Boolean createBatch(@Valid @RequestBody List<T> listT);

	@PutMapping(name = "update")
	T update(@Valid @RequestBody T t);

	@PutMapping(name = "updateBatch", path = "batch")
	T updateBatch(@Valid @RequestBody List<T> listT);

	@DeleteMapping(name = "delete")
	Boolean delete(@Valid @ModelAttribute T t);

	@DeleteMapping(name = "deleteById", path = "{id}")
	Boolean delete(@PathVariable("id") Long id);

	@DeleteMapping(name = "deleteBatch", path = "batch")
	Boolean delete(@Valid @RequestBody List<T> listT);

}
