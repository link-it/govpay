package it.govpay.web.business;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.govpay.web.entity.ApplicazioneEntity;
import it.govpay.web.entity.DominioEntity;
import it.govpay.web.enumeration.EsitoOperazione;
import it.govpay.web.exception.GovPayException;
import it.govpay.web.filters.ApplicazioneFilter;
import it.govpay.web.repository.ApplicazioneRepository;

@Component
public class Applicazione {

	@Autowired
	private ApplicazioneRepository applicazioneRepository;

	public ApplicazioneEntity getApplicazioneDominio(DominioEntity dominio, String iuv) throws GovPayException {
		return getApplicazioneDominio(dominio, iuv, true);
	}

	public ApplicazioneEntity getApplicazioneDominio(DominioEntity dominio, String iuv, boolean throwException) throws GovPayException {

		List<String> applicazioni = this.applicazioneRepository.findListaCodApplicazioni();

		for (String codApplicazione : applicazioni) {
			ApplicazioneFilter applicazioneFilter = new ApplicazioneFilter();
			applicazioneFilter.setCodApplicazione(Optional.of(codApplicazione));
			Optional<ApplicazioneEntity> findOne = this.applicazioneRepository.findOne(applicazioneFilter);

			if(findOne.isPresent()) {
				ApplicazioneEntity applicazione = findOne.get();
				if(applicazione.getUtenza().getAutorizzazioneDominiStar() || applicazione.getUtenza().isIdDominioAutorizzato(dominio.getId())) {
					if(applicazione.getRegExp() != null) {
						Pattern pIuv = Pattern.compile(applicazione.getRegExp());
						if(pIuv.matcher(iuv).matches())
							return applicazione;
					}
				}
			}
		}

		if(throwException)
			throw new GovPayException(EsitoOperazione.APP_006, iuv, dominio.getCodDominio());

		return null;
	}

}
