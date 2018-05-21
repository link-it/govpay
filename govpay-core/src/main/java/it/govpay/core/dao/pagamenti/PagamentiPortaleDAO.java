package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.logger.beans.Property;
import org.slf4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.DominiBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.PagamentoPortale.CODICE_STATO;
import it.govpay.bd.model.PagamentoPortale.STATO;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.bd.pagamento.filters.PagamentoPortaleFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTO;
import it.govpay.core.dao.pagamenti.dto.ListaRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO.RefVersamentoAvviso;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO.RefVersamentoPendenza;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PagamentoPortaleNonTrovatoException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.rs.v1.costanti.EsitoOperazione;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.UrlUtils;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Anagrafica;
import it.govpay.orm.IdVersamento;
import it.govpay.servizi.v2_3.commons.Mittente;
import it.govpay.servizi.v2_5.gpprt.GpAvviaTransazionePagamentoResponse;

public class PagamentiPortaleDAO extends BaseDAO {

	public PagamentiPortaleDAO() {
	}

	public PagamentiPortaleDTOResponse inserisciPagamenti(PagamentiPortaleDTO pagamentiPortaleDTO) throws GovPayException, NotAuthorizedException, ServiceException, NotAuthenticatedException {
		PagamentiPortaleDTOResponse response  = new PagamentiPortaleDTOResponse();
		GpAvviaTransazionePagamentoResponse transazioneResponse = new GpAvviaTransazionePagamentoResponse();
		Logger log = LoggerWrapperFactory.getLogger(WebControllerDAO.class);
		GpContext ctx = GpThreadLocal.get();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ctx.getTransactionId());
			DominiBD dominiBD = new DominiBD(bd);
			List<Versamento> versamenti = new ArrayList<Versamento>();

			// Aggiungo il codSessionePortale al PaymentContext
			ctx.getPagamentoCtx().setCodSessionePortale(pagamentiPortaleDTO.getIdSessionePortale());
			ctx.getContext().getRequest().addGenericProperty(new Property("codSessionePortale", pagamentiPortaleDTO.getIdSessionePortale() != null ? pagamentiPortaleDTO.getIdSessionePortale() : "--Non fornito--"));

			it.govpay.core.business.Applicazione applicazioneBusiness = new it.govpay.core.business.Applicazione(bd);
			this.autorizzaRichiesta(pagamentiPortaleDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA); 
			Applicazione applicazioneAutenticata = applicazioneBusiness.getApplicazioneAutenticata((Utenza) pagamentiPortaleDTO.getUser());
			ctx.log("ws.ricevutaRichiesta");
			String codApplicazione = applicazioneAutenticata.getCodApplicazione();

			applicazioneBusiness.autorizzaApplicazione(codApplicazione, applicazioneAutenticata, bd);
			ctx.log("ws.autorizzazione");

			String codDominio = null;
			String nome = null;
			List<IdVersamento> idVersamento = new ArrayList<IdVersamento>();
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			StringBuilder sbNomeVersamenti = new StringBuilder();
			List<String> listaMultibeneficiari = new ArrayList<String>();
			// 1. Lista Id_versamento
			for(int i = 0; i < pagamentiPortaleDTO.getPendenzeOrPendenzeRef().size(); i++) {
				Object v = pagamentiPortaleDTO.getPendenzeOrPendenzeRef().get(i);
				Versamento versamentoModel = null;
				if(v instanceof it.govpay.core.dao.commons.Versamento) {
					it.govpay.core.dao.commons.Versamento versamento = (it.govpay.core.dao.commons.Versamento) v;
					ctx.log("rpt.acquisizioneVersamento", versamento.getCodApplicazione(), versamento.getCodVersamentoEnte());
					versamentoModel = versamentoBusiness.chiediVersamento(versamento);
				}  else if(v instanceof RefVersamentoAvviso) {
					String idDominio = ((RefVersamentoAvviso)v).getIdDominio();
					try {
						Dominio dominio = dominiBD.getDominio(idDominio);
						versamentoModel = versamentoBusiness.chiediVersamento((RefVersamentoAvviso)v,dominio);
					}catch(NotFoundException e) {
						throw new GovPayException("Il pagamento non puo' essere avviato poiche' uno dei versamenti risulta associato ad un dominio non disponibile [Dominio:"+idDominio+"].", EsitoOperazione.DOM_000, idDominio);
					}
				}  else if(v instanceof RefVersamentoPendenza) {
					versamentoModel = versamentoBusiness.chiediVersamento((RefVersamentoPendenza)v);
				}

				UnitaOperativa uo = versamentoModel.getUo(bd);
				if(!uo.isAbilitato()) {
					throw new GovPayException("Il pagamento non puo' essere avviato poiche' uno dei versamenti risulta associato ad una unita' operativa disabilitata [Uo:"+uo.getCodUo()+"].", EsitoOperazione.UOP_001, uo.getCodUo());
				}

				Dominio dominio = uo.getDominio(bd); 
				if(!dominio.isAbilitato()) {
					throw new GovPayException("Il pagamento non puo' essere avviato poiche' uno dei versamenti risulta associato ad un dominio disabilitato [Dominio:"+dominio.getCodDominio()+"].", EsitoOperazione.DOM_001, dominio.getCodDominio());
				}


				if(i == 0) {
					// 	2. Codice dominio della prima pendenza
					codDominio = dominio.getCodDominio();
					// 3. ente creditore
				}

				if(sbNomeVersamenti.length() >0)
					sbNomeVersamenti.append("#");

				sbNomeVersamenti.append(versamentoModel.getCodVersamentoEnte());

				versamenti.add(versamentoModel);

				// colleziono i domini inserendo solo se non presente in lista
				if(!listaMultibeneficiari.contains(dominio.getCodDominio()))
					listaMultibeneficiari.add(dominio.getCodDominio());
			}

			// 5. somma degli importi delle pendenze
			double sommaImporti = 0;

			for (Versamento vTmp : versamenti) {
				sommaImporti += vTmp.getImportoTotale().doubleValue();
			}

			nome = sbNomeVersamenti.length() > 255 ? (sbNomeVersamenti.substring(0, 252) + "...") : sbNomeVersamenti.toString();
			STATO stato = null;
			CODICE_STATO codiceStato = null;
			String redirectUrl = null;
			String idSessionePsp = null;
			String pspRedirect = null;
			String codCanale = null;		
			String codPsp = null;
			String tipoVersamento = null;
			String descrizioneStato = null;

			PagamentoPortale pagamentoPortale = new PagamentoPortale();
			pagamentoPortale.setIdSessione(pagamentiPortaleDTO.getIdSessione());
			pagamentoPortale.setIdSessionePortale(pagamentiPortaleDTO.getIdSessionePortale());
			pagamentoPortale.setJsonRequest(pagamentiPortaleDTO.getJsonRichiesta());
			pagamentoPortale.setUrlRitorno(UrlUtils.addParameter(pagamentiPortaleDTO.getUrlRitorno() , "idSession",pagamentiPortaleDTO.getIdSessione()));
			pagamentoPortale.setDataRichiesta(new Date());
			pagamentoPortale.setCodApplicazione(codApplicazione);
			pagamentoPortale.setIdVersamento(idVersamento); 
			pagamentoPortale.setWispIdDominio(codDominio);
			pagamentoPortale.setNome(nome);
			pagamentoPortale.setImporto(sommaImporti); 

			// gestione multibeneficiari
			// se ho solo un dominio all'interno della lista allora vuol dire che i tutti pagamenti riferiscono lo stesso dominio
			// lascio null se il numero di domini e' > 1
			if(listaMultibeneficiari.size() == 1) {
				pagamentoPortale.setMultiBeneficiario(listaMultibeneficiari.get(0)); 
			}

			String idSessione = pagamentoPortale.getIdSessione();

			pagamentoPortale.setStato(STATO.IN_CORSO);
			pagamentoPortale.setCodiceStato(CODICE_STATO.SELEZIONE_WISP_IN_CORSO);
			PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(bd);
			pagamentiPortaleBD.insertPagamento(pagamentoPortale);

			// procedo al pagamento
			Anagrafica versanteModel = VersamentoUtils.toAnagraficaModel(pagamentiPortaleDTO.getVersante());
			it.govpay.core.business.Rpt rptBD = new it.govpay.core.business.Rpt(bd);
			List<Rpt> rpts = null;
			
			// sessione di pagamento non in corso
			codiceStato = CODICE_STATO.PAGAMENTO_IN_CORSO_AL_PSP;
			stato = STATO.IN_CORSO;

			try {
				rpts = rptBD.avviaTransazione(versamenti, applicazioneAutenticata, null, pagamentiPortaleDTO.getIbanAddebito(), versanteModel, pagamentiPortaleDTO.getAutenticazioneSoggetto(), pagamentiPortaleDTO.getUrlRitorno(), false, pagamentoPortale);

				Rpt rpt = rpts.get(0);

				GpAvviaTransazionePagamentoResponse.RifTransazione rifTransazione = new GpAvviaTransazionePagamentoResponse.RifTransazione();
				rifTransazione.setCcp(rpt.getCcp());
				rifTransazione.setCodApplicazione(rpt.getVersamento(bd).getApplicazione(bd).getCodApplicazione());
				rifTransazione.setCodDominio(rpt.getCodDominio());
				rifTransazione.setCodVersamentoEnte(rpt.getVersamento(bd).getCodVersamentoEnte());
				rifTransazione.setIuv(rpt.getIuv());
				transazioneResponse.getRifTransazione().add(rifTransazione);

				// se ho un redirect 			
				if(rpt.getPspRedirectURL() != null) {
					codiceStato = CODICE_STATO.PAGAMENTO_IN_CORSO_AL_PSP;
					stato = STATO.IN_CORSO;
					idSessionePsp = rpt.getCodSessione();
					redirectUrl = rpt.getPspRedirectURL();
					idSessione = UrlUtils.getCodSessione(redirectUrl);
					pspRedirect = rpt.getPspRedirectURL(); 
				} else {
					stato = STATO.IN_CORSO;
					codiceStato = CODICE_STATO.PAGAMENTO_IN_ATTESA_DI_ESITO;
					redirectUrl = UrlUtils.addParameter(pagamentiPortaleDTO.getUrlRitorno() , "idSession",pagamentiPortaleDTO.getIdSessione());
				}

				transazioneResponse.setPspSessionId(idSessionePsp);
				transazioneResponse.setUrlRedirect(redirectUrl);
				transazioneResponse.setCodEsito(EsitoOperazione.OK.toString());
				transazioneResponse.setDescrizioneEsito("Operazione completata con successo");
				transazioneResponse.setMittente(Mittente.GOV_PAY);

			}catch(GovPayException e) {
				transazioneResponse = (GpAvviaTransazionePagamentoResponse) e.getWsResponse(transazioneResponse, "ws.ricevutaRichiestaKo", log);

				pagamentoPortale.setCodiceStato(CODICE_STATO.PAGAMENTO_FALLITO);
				pagamentoPortale.setStato(STATO.FALLITO);
				pagamentoPortale.setDescrizioneStato(e.getMessage());
				pagamentiPortaleBD.insertPagamento(pagamentoPortale);
				throw e;
			} catch (Exception e) {
				transazioneResponse = (GpAvviaTransazionePagamentoResponse) new GovPayException(e).getWsResponse(transazioneResponse, "ws.ricevutaRichiestaKo", log);
				throw e;
			}
			
			for(Versamento versamentoModel: versamenti) {
				if(versamentoModel.getId() != null) {
					IdVersamento idV = new IdVersamento();
					idV.setCodVersamentoEnte(versamentoModel.getCodVersamentoEnte());
					idV.setId(versamentoModel.getId());
					idVersamento.add(idV);
				}
			}

			pagamentoPortale.setIdSessionePsp(idSessionePsp);
			pagamentoPortale.setPspRedirectUrl(pspRedirect);
			pagamentoPortale.setCodiceStato(codiceStato);
			pagamentoPortale.setStato(stato);
			pagamentoPortale.setDescrizioneStato(descrizioneStato);
			pagamentoPortale.setCodPsp(codPsp);
			pagamentoPortale.setTipoVersamento(tipoVersamento);
			pagamentoPortale.setCodCanale(codCanale); 

			pagamentiPortaleBD.updatePagamento(pagamentoPortale, true); //inserisce anche i versamenti

			response.setRedirectUrl(redirectUrl);
			response.setId(pagamentoPortale.getIdSessione());
			response.setIdSessione(idSessione); 

			return response;
		}finally {
			if(ctx != null) {
				ctx.setResult(transazioneResponse);
				ctx.log();
			}
			if(bd != null)
				bd.closeConnection();
		}
	}

	public LeggiPagamentoPortaleDTOResponse leggiPagamentoPortale(LeggiPagamentoPortaleDTO leggiPagamentoPortaleDTO) throws ServiceException,PagamentoPortaleNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{
		LeggiPagamentoPortaleDTOResponse leggiPagamentoPortaleDTOResponse = new LeggiPagamentoPortaleDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(leggiPagamentoPortaleDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA,bd);

			PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(bd);
			PagamentoPortale pagamentoPortale = pagamentiPortaleBD.getPagamentoFromCodSessione(leggiPagamentoPortaleDTO.getIdSessione());

			if(pagamentoPortale.getMultiBeneficiario() != null) {
				// controllo che il dominio sia autorizzato
				this.autorizzaRichiesta(leggiPagamentoPortaleDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, pagamentoPortale.getMultiBeneficiario(), null, bd);
			}
			leggiPagamentoPortaleDTOResponse.setPagamento(pagamentoPortale); 

			if(leggiPagamentoPortaleDTO.isRisolviLink()) {
				PendenzeDAO pendenzeDao = new PendenzeDAO();
				ListaPendenzeDTO listaPendenzaDTO = new ListaPendenzeDTO(leggiPagamentoPortaleDTO.getUser());
				listaPendenzaDTO.setIdPagamento(leggiPagamentoPortaleDTO.getIdSessione());
				ListaPendenzeDTOResponse listaPendenze = pendenzeDao.listaPendenze(listaPendenzaDTO, bd);
				leggiPagamentoPortaleDTOResponse.setListaPendenze(listaPendenze.getResults());

				RptDAO rptDao = new RptDAO(); 
				ListaRptDTO listaRptDTO = new ListaRptDTO(leggiPagamentoPortaleDTO.getUser());
				listaRptDTO.setIdPagamento(leggiPagamentoPortaleDTO.getIdSessione());
				ListaRptDTOResponse listaRpt = rptDao.listaRpt(listaRptDTO, bd);
				leggiPagamentoPortaleDTOResponse.setListaRpp(listaRpt.getResults());
			}

			return leggiPagamentoPortaleDTOResponse;
		}catch(NotFoundException e) {
			throw new PagamentoPortaleNonTrovatoException("Non esiste un pagamento associato all'ID ["+leggiPagamentoPortaleDTO.getIdSessione()+"]");
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public ListaPagamentiPortaleDTOResponse listaPagamentiPortale(ListaPagamentiPortaleDTO listaPagamentiPortaleDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(listaPagamentiPortaleDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA,bd);
			// Autorizzazione sui domini
			List<String> codDomini = AclEngine.getDominiAutorizzati((Utenza) listaPagamentiPortaleDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
			if(codDomini == null) {
				throw new NotAuthorizedException("L'utenza autenticata ["+listaPagamentiPortaleDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
			}

			PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(bd);
			PagamentoPortaleFilter filter = pagamentiPortaleBD.newFilter();

			filter.setOffset(listaPagamentiPortaleDTO.getOffset());
			filter.setLimit(listaPagamentiPortaleDTO.getLimit());
			filter.setDataInizio(listaPagamentiPortaleDTO.getDataDa());
			filter.setDataFine(listaPagamentiPortaleDTO.getDataA());
			filter.setStato(listaPagamentiPortaleDTO.getStato());
			filter.setVersante(listaPagamentiPortaleDTO.getVersante());
			filter.setFilterSortList(listaPagamentiPortaleDTO.getFieldSortList());

			if(codDomini != null && codDomini.size() > 0)
				filter.setCodDomini(codDomini);

			long count = pagamentiPortaleBD.count(filter);

			if(count > 0) {
				return new ListaPagamentiPortaleDTOResponse(count, pagamentiPortaleBD.findAll(filter));
			} else {
				return new ListaPagamentiPortaleDTOResponse(count, new ArrayList<PagamentoPortale>());
			}
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
}
