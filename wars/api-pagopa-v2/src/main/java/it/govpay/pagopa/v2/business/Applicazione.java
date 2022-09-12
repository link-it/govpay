package it.govpay.pagopa.v2.business;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import it.govpay.core.exception.GovPayException;
import it.govpay.pagopa.v2.beans.Connettore;
import it.govpay.pagopa.v2.beans.mapper.ConnettoreMapper;
import it.govpay.pagopa.v2.entity.ApplicazioneEntity;
import it.govpay.pagopa.v2.entity.ConnettoreEntity;
import it.govpay.pagopa.v2.entity.DominioEntity;
import it.govpay.pagopa.v2.enumeration.EsitoOperazione;
import it.govpay.pagopa.v2.repository.ApplicazioneRepository;
import it.govpay.pagopa.v2.repository.ConnettoreRepository;
import it.govpay.pagopa.v2.utils.UtenzaUtils;


@Component
public class Applicazione {

	@Autowired
	private ApplicazioneRepository applicazioneRepository;
	
	@Autowired
	private ConnettoreRepository connettoreRepository;
	
	@Autowired
	private ConnettoreMapper connettoreMapper;
	
	public Connettore getConnettoreApplicazione(ApplicazioneEntity applicazioneEntity) {
		String codConnettoreIntegrazione = applicazioneEntity.getCodConnettoreIntegrazione();
		List<ConnettoreEntity> findAllByCodConnettore = this.connettoreRepository.findAllByCodConnettore(codConnettoreIntegrazione);
		return this.connettoreMapper.getConnettore(findAllByCodConnettore);
	}

	public ApplicazioneEntity getApplicazioneDominio(DominioEntity dominio, String iuv) throws GovPayException {
		return getApplicazioneDominio(dominio, iuv, true);
	}

	/**
	 * Restituisce la prima applicazione registrata che puo' gestire gli IUV per il Dominio passsato come parametro
	 * Un'applicazione e' autorizzata se:
	 * - il campo autorizzazioneDominiStar dell'utenza ad essa collegata e' true (utenza gestisce tutti i domini)
	 * - in caso contrario se l'id del dominio e' presente tra quelli autorizzati per l'utenza.
	 * Se l'applicazione puo' gestire il dominio si controlla se puo' gestire lo IUV, controllando se il pattern di gestione IUV dell'applicazione
	 * fa il match con lo IUV passato come parametro.  
	 * 
	 * @param dominio
	 * @param iuv
	 * @param throwException
	 * @return
	 * @throws GovPayException
	 */
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