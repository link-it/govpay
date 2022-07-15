package it.govpay.web.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import it.govpay.web.entity.IntermediarioEntity;
import it.govpay.web.entity.IntermediarioEntity_;

public class IntermediarioFilter implements Specification<IntermediarioEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Optional<String> codIntermediario = Optional.empty();
	
	@Override
	public Predicate toPredicate(Root<IntermediarioEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predLst = new ArrayList<>();

		query.distinct(true);

		if (codIntermediario.isPresent()) {
			predLst.add(cb.equal(root.get(IntermediarioEntity_.codIntermediario), codIntermediario.get()));
		}
		
		// per non generare where 1 = 1
		if(predLst.isEmpty())
			return null;

		return cb.and(predLst.toArray(new Predicate[]{}));	
	}

	public Optional<String> getCodIntermediario() {
		return codIntermediario;
	}

	public void setCodIntermediario(Optional<String> codIntermediario) {
		this.codIntermediario = codIntermediario;
	}
	
	
}
