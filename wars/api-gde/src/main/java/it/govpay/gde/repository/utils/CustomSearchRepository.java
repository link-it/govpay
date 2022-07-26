package it.govpay.gde.repository.utils;

import java.io.Serializable;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;


@NoRepositoryBean
public interface CustomSearchRepository<T, ID extends Serializable, V> {
	
	public Page<T> findAll(Pageable pageable, Map<V, Object> searchParams);

}
