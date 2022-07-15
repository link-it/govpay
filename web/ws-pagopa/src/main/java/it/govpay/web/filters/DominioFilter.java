package it.govpay.web.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import it.govpay.web.entity.DominioEntity;
import it.govpay.web.entity.DominioEntity_;

public class DominioFilter implements Specification<DominioEntity> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Optional<String> codDominio = Optional.empty();
	
	@Override
	public Predicate toPredicate(Root<DominioEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predLst = new ArrayList<>();

		query.distinct(true);

		if (codDominio.isPresent()) {
			predLst.add(cb.equal(root.get(DominioEntity_.codDominio), codDominio.get()));
		}
		
		// per non generare where 1 = 1
		if(predLst.isEmpty())
			return null;

		return cb.and(predLst.toArray(new Predicate[]{}));	
	}

	public Optional<String> getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(Optional<String> codDominio) {
		this.codDominio = codDominio;
	}

	
	
}
