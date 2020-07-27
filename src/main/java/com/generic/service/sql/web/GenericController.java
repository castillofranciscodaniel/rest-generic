package com.generic.service.sql.web;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestHeader;

import com.generic.service.sql.models.Model;
import com.generic.service.sql.models.UserAudit;
import com.generic.service.sql.models.util.ErrorBody;
import com.generic.service.sql.service.GenericService;
import com.generic.service.sql.service.ResourceNotFoundException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public abstract class GenericController<T extends Model<ID>, ID> {

	private final String HEADER_AUTH = "authorization";
	private final String PREFIX_BEARER = "Bearer ";
	private GenericService<T, ID> genericService;
	private Optional<String> jwtKey;
	private List<String> authorities;
	private ID userId;

	public GenericController(GenericService<T, ID> genericService) {
		this.genericService = genericService;
	}

	public GenericController(GenericService<T, ID> genericService, Optional<String> jwtKey) {
		this.genericService = genericService;
		this.jwtKey = jwtKey;
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
	public ResponseEntity<?> findById(@PathVariable("id") ID id, @RequestHeader Map<String, String> headers)
			throws Exception {
		this.setUserIdAndRoles(headers, id);
		T t = this.genericService.findById(id);

		UserAudit<ID> userAudit = (UserAudit<ID>) t;
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
	
	private Boolean isSuperOrIdOK(Map<String, String> headers, ID userId) {
		
		this.setUserIdAndRoles(headers, userId);
		
		List<String> superUsers = List.of("ROLE_DEVELOPER", "ROLE_SUPER_ADMIN");
		
		for (int i = 0; i < this.authorities.size(); i++) {
			for (int j = 0; j < superUsers.size(); j++) {
				if(authorities.get(i).equals(superUsers.get(j))) {
					return true;
				}
				
			}
		}
		return false;
	}

	private void setUserIdAndRoles(Map<String, String> headers, ID userID) {
		if (this.jwtKey.isPresent()) {
			if (headers.containsKey(HEADER_AUTH)) {
				String tokenBearer = headers.get(HEADER_AUTH);
				Claims claims = this.getClaimsFromToken(tokenBearer);
				this.setUpSpringAuthentication(claims);
			}
		}
	}

	private Claims getClaimsFromToken(String tokenBearer) {
		String jwtToken = tokenBearer.replace(PREFIX_BEARER, "");
		return Jwts.parser().setSigningKey(this.jwtKey.get().getBytes()).parseClaimsJws(jwtToken).getBody();
	}

	@SuppressWarnings("unchecked")
	private void setUpSpringAuthentication(Claims claims) {
		this.authorities = (List<String>) claims.get("authorities");
		this.userId = (ID)(claims.get("userId").toString());
	}

}
