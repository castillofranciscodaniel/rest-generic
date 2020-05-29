package com.springboot.app.commons.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
//si los borro.. tengo que borrar la dependencia de jackson del pom.xml
@JsonIgnoreProperties(value = { "createdAt", "updatedAt" }, allowGetters = true)
public abstract class DateAuditNoSql<ID> extends ModelNoSql<ID> {

	public DateAuditNoSql() {
		super();
	}

	public DateAuditNoSql(ID modelId) {
		super(modelId);
	}

	@CreatedDate
	@Temporal(TemporalType.DATE)
	@Column(updatable = false)
	private Date createAt;

	@LastModifiedDate
	@Temporal(TemporalType.DATE)
	@Column(updatable = false)
	private Date updatedAt;

}