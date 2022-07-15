package it.govpay.web.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import it.govpay.web.entity.StazioneEntity;
import it.govpay.web.entity.StazioneEntity_;

public class StazioneFilter implements Specification<StazioneEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Optional<String> codStazione = Optional.empty();
	
	@Override
	public Predicate toPredicate(Root<StazioneEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predLst = new ArrayList<>();

		query.distinct(true);

		if (codStazione.isPresent()) {
			predLst.add(cb.equal(root.get(StazioneEntity_.codStazione), codStazione.get()));
		}
		
		// per non generare where 1 = 1
		if(predLst.isEmpty())
			return null;

		return cb.and(predLst.toArray(new Predicate[]{}));	
	}

	public Optional<String> getStazione() {
		return codStazione;
	}

	public void setCodStazione(Optional<String> codStazione) {
		this.codStazione = codStazione;
	}
	
	
}
