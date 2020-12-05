package com.generic.dbcache.repository;

import com.generic.dbcache.value.AbstractValue;
import com.generic.dbcache.value.GenericCollection;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.mongodb.repository.MongoRepository;

@NoRepositoryBean
public interface GenericMongoDBRepository<V extends AbstractValue> extends MongoRepository<GenericCollection<V>,String>{

}
