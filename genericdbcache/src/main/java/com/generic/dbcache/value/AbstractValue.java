package com.generic.dbcache.value;

import org.springframework.data.annotation.Id;

public abstract class AbstractValue {
	
	@Id
	private String valueId;
	
	public String getValueId() {
		return valueId;
	}

	public AbstractValue valueId(String valueId) {
		this.valueId = valueId;
		return this;
	}
}
