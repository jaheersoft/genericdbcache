package com.generic.dbcache.value;

import java.util.Date;
import org.springframework.data.annotation.Id;

public class GenericCollection<V extends AbstractValue> {

	@Id
	private String collectionId;
	private Date createdAt;
	private Date updateAt;
	private String createdBy;
	private String updatedBy;
	private V value;

	public String getCollectionId() {
		return collectionId;
	}

	public GenericCollection<V> collectionId(String collectionId) {
		this.collectionId = collectionId;
		return this;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public GenericCollection<V> createdAt(Date createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public Date getUpdateAt() {
		return updateAt;
	}

	public GenericCollection<V> updateAt(Date updateAt) {
		this.updateAt = updateAt;
		return this;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public GenericCollection<V> createdBy(String createdBy) {
		this.createdBy = createdBy;
		return this;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public GenericCollection<V> updatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
		return this;
	}

	public V getValue() {
		return value;
	}

	public GenericCollection<V> value(V value) {
		this.value = value;
		return this;
	}
}
