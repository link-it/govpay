package it.govpay.web.filters;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import it.govpay.web.entity.ApplicazioneEntity;
import it.govpay.web.entity.ApplicazioneEntity_;

public class ApplicazioneFilter implements Specification<ApplicazioneEntity> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Optional<String> codApplicazione = Optional.empty();
	
	@Override
	public Predicate toPredicate(Root<ApplicazioneEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predLst = new ArrayList<>();

		query.distinct(true);

		if (codApplicazione.isPresent()) {
			predLst.add(cb.equal(root.get(ApplicazioneEntity_.codApplicazione), codApplicazione.get()));
		}
		
		// per non generare where 1 = 1
		if(predLst.isEmpty())
			return null;

		return cb.and(predLst.toArray(new Predicate[]{}));	
	}

	public Optional<String> getCodApplicazione() {
		return codApplicazione;
	}

	public void setCodApplicazione(Optional<String> codApplicazione) {
		this.codApplicazione = codApplicazione;
	}
	
}
