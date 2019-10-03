package it.govpay.pagamento.matcher;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import it.govpay.bd.BasicBD;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.rs.v1.authentication.recaptcha.matcher.ReCaptchaAntPathRequestMatcher;

public class AvvisiAntPathRequestMatcher extends ReCaptchaAntPathRequestMatcher {
	
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
		boolean matches = this.doMatches(request);
		
		if(matches) {
			String getPathInfo = request.getPathInfo(); // da /public in poi
			
			// Autorizzazione per UUID
			String uuid = request.getParameter(PARAMETER_UUID);

			if(uuid != null) {
				logger.debug("Controllo accesso alla risorsa ["+request.getPathInfo()+"] parametro UUID trovato, controllo disponibilita' avviso nel sistema...");
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
						matches = matches && checkDirittiAvviso(idDominio, iuv, uuid); //se hai diritto di vedere la risorsa passi altrimenti controllo il captcha
					} else { // parametri non validi
						// restituisco l'errore di autorizzazione
						matches = false;
//						Response errorPayload = CodiceEccezione.AUTORIZZAZIONE.toFaultResponse("Accesso negato: non si dispone dei diritti necessari per accedere alla risorsa richiesta.");
//						AbstractBasicAuthenticationEntryPoint.fillResponse((HttpServletResponse) res, errorPayload);
//						return;
					}
				}catch(Throwable e) {
					logger.error("Errore durante il check disponibilita' avviso: " + e.getMessage(), e);
//					Response errorPayload = CodiceEccezione.AUTORIZZAZIONE.toFaultResponse("Accesso negato: Servizio di check disponibilita' avviso non disponibile.");
//					AbstractBasicAuthenticationEntryPoint.fillResponse((HttpServletResponse) res, errorPayload);
					matches = false;
				}
			} 
			
			matches = matches && this.validateCaptcha(request);
		}
		
		logger.debug("Controllo accesso alla risorsa ["+request.getPathInfo()+"] completato con esito ["+(matches ? "OK" : "KO")+"]");
		
		return matches;
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
