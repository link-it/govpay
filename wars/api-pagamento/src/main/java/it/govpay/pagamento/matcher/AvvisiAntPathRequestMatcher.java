package it.govpay.pagamento.matcher;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import it.govpay.bd.BasicBD;
import it.govpay.bd.configurazione.model.Hardening;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.rs.v1.authentication.hardening.matcher.HardeningAntPathRequestMatcher;

public class AvvisiAntPathRequestMatcher extends HardeningAntPathRequestMatcher {

	private static final Log logger = LogFactory.getLog(AvvisiAntPathRequestMatcher.class);
	private static final String PARAMETER_UUID = "UUID";

	public AvvisiAntPathRequestMatcher(String pattern, String httpMethod, boolean caseSensitive) {
		super(pattern, httpMethod, caseSensitive);
	}

	public AvvisiAntPathRequestMatcher(String pattern, String httpMethod) {
		super(pattern, httpMethod);
	}

	public AvvisiAntPathRequestMatcher(String pattern) {
		super(pattern);
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		boolean authorizedPathMatch = this.doMatches(request);

		if(authorizedPathMatch) {
			Hardening setting = this.readSettings();
			String getPathInfo = request.getPathInfo(); // da /public in poi
			
			if(setting.isAbilitato()) {
				logger.debug("Applico regole di hardening per l'accesso alla risorsa ["+getPathInfo+"]...");
				// per il servizio di avviso i controlli sono in cascata
				// controllo UUID
				// controllo Recaptcha


				// 1. Autorizzazione per UUID
				String uuid = request.getParameter(PARAMETER_UUID);
				boolean authorizedUUID = false;
				logger.debug("Controllo presenza del parametro "+PARAMETER_UUID+"...");
				
				if(uuid != null) {
					logger.debug("Parametro "+PARAMETER_UUID+" trovato, controllo disponibilita' avviso nel sistema...");
					String[] splitAvvisi = getPathInfo.split("/avvisi/");

					String idDominio = null;
					String iuv = null;

					try {
						if(splitAvvisi != null && splitAvvisi.length > 0) {
							String[]  split = splitAvvisi[1].split("/");

							if(split != null && split.length > 1) {
								// id dominio e' il path in posizione 1
								idDominio = split[0];
								// iuv e' il path in posizione 2
								iuv = split[1];	
							}
						}

						if(idDominio != null && iuv != null) {
							authorizedUUID = authorizedPathMatch && checkDirittiAvviso(idDominio, iuv, uuid); //se hai diritto di vedere la risorsa passi altrimenti controllo il captcha
						} else { // parametri non validi
							authorizedUUID = false;
						}
					}catch(Throwable e) {
						logger.error("Errore durante il check disponibilita' avviso: " + e.getMessage(), e);
						authorizedUUID = false;
					}
				} else {
					logger.debug("Parametro "+PARAMETER_UUID+" non trovato.");
				}
				
				// 2. Controllo del captcha se UUID = null oppure se non ho passato il controllo con UUID
				if(!authorizedUUID) {
					logger.debug("Applico controllo tramite validazione ReCaptcha...");
					authorizedPathMatch = authorizedPathMatch && this.applicaControlloReCaptcha(request, setting);
					logger.debug("Controllo tramite validazione ReCaptcha completato.");
				}

				logger.debug("Controllo accesso alla risorsa ["+request.getPathInfo()+"], regole di hardening applicate con esito ["+(authorizedPathMatch ? "OK" : "KO")+"]");
			} else {
				logger.debug("Regole di hardening disabilitate per l'accesso alla risorsa ["+getPathInfo+"], accesso consentito.");
				authorizedPathMatch = true; // se il controllo e' disabilitato passo
			}
		}

		return authorizedPathMatch;
	}

	private boolean checkDirittiAvviso(String idDominio,String iuv, String uuid) {
		boolean authorized = false;

		BasicBD bd = null;
		try {
			logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+", UUID: "+uuid+"] in corso...");
			String transactionId = UUID.randomUUID().toString();
			bd = BasicBD.newInstance(transactionId, true);
			AvvisiDAO avvisiDAO = new AvvisiDAO();
			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(SecurityContextHolder.getContext().getAuthentication(), idDominio);
			if(iuv.length() == 18)
				getAvvisoDTO.setNumeroAvviso(iuv);
			else 
				getAvvisoDTO.setIuv(iuv);
			getAvvisoDTO.setIdentificativoCreazionePendenza(uuid);
			GetAvvisoDTOResponse checkDisponibilitaAvviso = avvisiDAO.checkDisponibilitaAvviso(getAvvisoDTO, bd);
			logger.debug("Controllo diritti sull'avviso [IdDominio:"+idDominio+", Iuv/NumeroAvviso: "+iuv+", UUID: "+uuid+"] completato con esito ["+(checkDisponibilitaAvviso.isFound() ? "accesso consentito" : "accesso negato")+"].");
			authorized = checkDisponibilitaAvviso.isFound();
		} catch(PendenzaNonTrovataException e){
			return false;
		}catch(Exception e){
			throw new RuntimeException("Errore interno, impossibile effettuare il check disponibilita' avviso: "+ e.getMessage(), e);
		}	finally {
			if(bd != null)
				bd.closeConnection();
		}

		return authorized;
	}
}
