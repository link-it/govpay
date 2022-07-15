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
import it.govpay.web.entity.DominioEntity_;
import it.govpay.web.entity.StazioneEntity_;
import it.govpay.web.entity.VersamentoEntity;
import it.govpay.web.entity.VersamentoEntity_;

public class VersamentoFilter implements Specification<VersamentoEntity> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Optional<String> codApplicazione = Optional.empty();
	
	private Optional<String> codDominio = Optional.empty();
	private Optional<String> iuv = Optional.empty();
	
	@Override
	public Predicate toPredicate(Root<VersamentoEntity> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
		List<Predicate> predLst = new ArrayList<>();

		query.distinct(true);

		if (codApplicazione.isPresent()) {
			predLst.add(cb.equal(root.get(VersamentoEntity_.applicazione).get(ApplicazioneEntity_.codApplicazione), codApplicazione.get()));
		}
		
		if (codDominio.isPresent()) {
			predLst.add(cb.equal(root.get(VersamentoEntity_.dominio).get(DominioEntity_.codDominio), codDominio.get()));
		}
		
		if (iuv.isPresent()) {
			predLst.add(cb.equal(root.get(VersamentoEntity_.iuvVersamento), iuv.get()));
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

	public Optional<String> getCodDominio() {
		return codDominio;
	}

	public void setCodDominio(Optional<String> codDominio) {
		this.codDominio = codDominio;
	}

	public Optional<String> getIuv() {
		return iuv;
	}

	public void setIuv(Optional<String> iuv) {
		this.iuv = iuv;
	}
	
}
