package it.govpay.pagopa.v2.business;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.govpay.pagopa.v2.entity.ApplicazioneEntity;
import it.govpay.pagopa.v2.entity.DominioEntity;
import it.govpay.pagopa.v2.enumeration.EsitoOperazione;
import it.govpay.pagopa.v2.exception.GovPayException;
import it.govpay.pagopa.v2.repository.ApplicazioneRepository;
import it.govpay.pagopa.v2.utils.UtenzaUtils;


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
			Optional<ApplicazioneEntity> findOne = this.applicazioneRepository.findOneByCodApplicazione(codApplicazione);

			if(findOne.isPresent()) {
				ApplicazioneEntity applicazione = findOne.get();
				if(applicazione.getUtenza().getAutorizzazioneDominiStar() || UtenzaUtils.isIdDominioAutorizzato(applicazione.getUtenza(), dominio.getId())) { 
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