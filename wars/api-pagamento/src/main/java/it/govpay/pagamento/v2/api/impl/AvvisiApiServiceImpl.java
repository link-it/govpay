package it.govpay.pagamento.v2.api.impl;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.service.BaseImpl;
import org.openspcoop2.utils.service.context.IContext;
import org.openspcoop2.utils.transport.http.HttpRequestMethod;

import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTO;
import it.govpay.core.dao.anagrafica.dto.GetAvvisoDTOResponse;
import it.govpay.core.dao.pagamenti.AvvisiDAO;
import it.govpay.exception.WebApplicationExceptionMapper;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v2.acl.Acl;
import it.govpay.pagamento.v2.acl.AuthorizationRules;
import it.govpay.pagamento.v2.acl.impl.TipoUtenzaOnlyAcl;
import it.govpay.pagamento.v2.api.AvvisiApi;
import it.govpay.pagamento.v2.beans.Avviso;
import it.govpay.pagamento.v2.beans.converter.PendenzeConverter;
/**
 * GovPay - API Pagamento
 */
public class AvvisiApiServiceImpl extends BaseImpl implements AvvisiApi {
	
	public static UriBuilder basePath = UriBuilder.fromPath("/avvisi");

	public AvvisiApiServiceImpl(){
		super(org.slf4j.LoggerFactory.getLogger(AvvisiApiServiceImpl.class));
	}

	private AuthorizationRules getAuthorizationRules() throws Exception{
		AuthorizationRules ac = new AuthorizationRules();
		
		/*
		 * Utenti anonimi possono chiamare:
		 * - getAvviso - per ottenere le informazioni di un avviso o della pendenza ad esso associata
		 */
		{
			TIPO_UTENZA[] tipiUtenza = { TIPO_UTENZA.ANONIMO };
			
			Map<HttpRequestMethod, String[]> resources = new HashMap<HttpRequestMethod, String[]>();
			{
				String[] location = { "/avvisi/{idDominio}/{numeroAvviso}" };
				resources.put(HttpRequestMethod.GET, location);
			}
			
			Acl acl = new TipoUtenzaOnlyAcl(tipiUtenza, resources);
			ac.addAcl(acl);
		}
		/*
		 * Utenti CITTADINO e APPLICAZIONE possono chiamare tutte le operazioni:
		 */
		{
			TIPO_UTENZA[] tipiUtenza = { TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE };
			Acl acl = new TipoUtenzaOnlyAcl(tipiUtenza);
			ac.addAcl(acl);
		}
		
		return ac;
	}

	@Override
	public Avviso getAvviso(String idDominio, String numeroAvviso, String idPagatore) {
		IContext context = this.getContext();
		try {
			context.getLogger().info("Invocazione in corso ...");     
			getAuthorizationRules().authorize(context);
			context.getLogger().debug("Autorizzazione completata con successo");      
			
			String accept = "";
			if(StringUtils.isNotEmpty(this.getContext().getServletRequest().getHeader("Accept"))) {
				accept = this.getContext().getServletRequest().getHeader("Accept");
			}
			
			GetAvvisoDTO getAvvisoDTO = new GetAvvisoDTO(context.getAuthentication(), idDominio);
			getAvvisoDTO.setAccessoAnonimo(true);
			getAvvisoDTO.setCfDebitore(idPagatore);
			getAvvisoDTO.setNumeroAvviso(numeroAvviso);
			
			AvvisiDAO avvisiDAO = new AvvisiDAO();
			
			GetAvvisoDTOResponse getAvvisoDTOResponse = avvisiDAO.getAvviso(getAvvisoDTO);
			Avviso avviso = PendenzeConverter.toAvviso(getAvvisoDTOResponse.getVersamento(), getAvvisoDTOResponse.getDominio(), getAvvisoDTOResponse.getBarCode(), getAvvisoDTOResponse.getQrCode());
			
			if(accept.toLowerCase().contains("application/pdf")) {
				context.getServletResponse().setHeader("content-disposition", "attachment; filename=\""+getAvvisoDTOResponse.getFilenameAvviso()+"\"");
			}
			
			context.getLogger().info("Invocazione completata con successo");
			return avviso;
		}
		catch(javax.ws.rs.WebApplicationException e) {
			context.getLogger().error("Invocazione terminata con errore '4xx': %s",e, e.getMessage());
			throw e;
		}
		catch(Throwable e) {
			context.getLogger().error("Invocazione terminata con errore: %s",e, e.getMessage());
			throw WebApplicationExceptionMapper.handleException(e);
		}
	}

   
}

