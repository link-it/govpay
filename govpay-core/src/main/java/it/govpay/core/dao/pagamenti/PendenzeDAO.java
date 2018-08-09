package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.exception.ValidationException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.core.business.model.Iuv;
import it.govpay.core.business.model.PrintAvvisoDTO;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PatchPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PatchPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.rs.v1.beans.base.PatchOp;
import it.govpay.core.rs.v1.beans.base.PatchOp.OpEnum;
import it.govpay.core.rs.v1.beans.base.StatoPendenza;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.avvisi.AvvisoPagamento;
import it.govpay.model.avvisi.AvvisoPagamentoInput;

public class PendenzeDAO extends BaseDAO{

	public PendenzeDAO() {
	}

	public ListaPendenzeDTOResponse listaPendenze(ListaPendenzeDTO listaPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			return listaPendenze(listaPendenzaDTO, bd);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public ListaPendenzeDTOResponse listaPendenze(ListaPendenzeDTO listaPendenzaDTO, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		this.autorizzaRichiesta(listaPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);
		// Autorizzazione sui domini
		List<Long> idDomini = AclEngine.getIdDominiAutorizzati((Utenza) listaPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
		if(idDomini == null) {
			throw new NotAuthorizedException("L'utenza autenticata ["+listaPendenzaDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
		}
		
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		VersamentoFilter filter = versamentiBD.newFilter();
		
		if(idDomini != null && idDomini.size() > 0)
			filter.setIdDomini(idDomini);

		filter.setOffset(listaPendenzaDTO.getOffset());
		filter.setLimit(listaPendenzaDTO.getLimit());
		filter.setDataInizio(listaPendenzaDTO.getDataDa());
		filter.setDataFine(listaPendenzaDTO.getDataA());
		if(listaPendenzaDTO.getStato()!=null) {
			try {
				StatoVersamento statoVersamento = null;
				StatoPendenza statoPendenza = StatoPendenza.valueOf(listaPendenzaDTO.getStato());
				
				//TODO mapping...piu' stati?
				switch(statoPendenza) {
				case ANNULLATA: statoVersamento = StatoVersamento.ANNULLATO;
					break;
				case ESEGUITA: statoVersamento = StatoVersamento.ESEGUITO;
					break;
				case ESEGUITA_PARZIALE: statoVersamento = StatoVersamento.PARZIALMENTE_ESEGUITO;
					break;
				case NON_ESEGUITA: statoVersamento = StatoVersamento.NON_ESEGUITO;
					break;
				case SCADUTA: statoVersamento = StatoVersamento.NON_ESEGUITO; //TODO aggiungere data scadenza < ora
					break;
				default:
					break;
				
				}
				filter.setStatoVersamento(statoVersamento);
			} catch(Exception e) {
				return new ListaPendenzeDTOResponse(0, new ArrayList<LeggiPendenzaDTOResponse>());
			}
		}
		filter.setCodDominio(listaPendenzaDTO.getIdDominio() );
		filter.setCodPagamentoPortale(listaPendenzaDTO.getIdPagamento());
		filter.setCodUnivocoDebitore(listaPendenzaDTO.getIdDebitore());
		filter.setCodApplicazione(listaPendenzaDTO.getIdA2A());
		filter.setFilterSortList(listaPendenzaDTO.getFieldSortList());

		long count = versamentiBD.count(filter);

		List<LeggiPendenzaDTOResponse> resList = new ArrayList<LeggiPendenzaDTOResponse>();
		if(count > 0) {
			List<Versamento> findAll = versamentiBD.findAll(filter);

			for (Versamento versamento : findAll) {
				LeggiPendenzaDTOResponse elem = new LeggiPendenzaDTOResponse();
				elem.setVersamento(versamento);
				elem.setApplicazione(versamento.getApplicazione(versamentiBD));
				elem.setDominio(versamento.getDominio(versamentiBD));
				elem.setUnitaOperativa(versamento.getUo(versamentiBD));
				List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(versamentiBD);
				for (SingoloVersamento singoloVersamento : singoliVersamenti) {
					singoloVersamento.getCodContabilita(bd);
					singoloVersamento.getIbanAccredito(bd);
					singoloVersamento.getTipoContabilita(bd);
					singoloVersamento.getTributo(bd);
					
				}
				elem.setLstSingoliVersamenti(singoliVersamenti);

				resList.add(elem);
			}
		} 

		return new ListaPendenzeDTOResponse(count, resList);
	}

	public LeggiPendenzaDTOResponse leggiPendenza(LeggiPendenzaDTO leggiPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();
		Versamento versamento;
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(leggiPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			versamento = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(versamentiBD, leggiPendenzaDTO.getCodA2A()).getId(), leggiPendenzaDTO.getCodPendenza());
			
			Dominio dominio = versamento.getDominio(versamentiBD);
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(leggiPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, dominio.getCodDominio(), null, bd);
			
			response.setVersamento(versamento);
			response.setApplicazione(versamento.getApplicazione(versamentiBD));
		
			response.setDominio(dominio);
			response.setUnitaOperativa(versamento.getUo(versamentiBD));
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(versamentiBD);
			response.setLstSingoliVersamenti(singoliVersamenti);
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				singoloVersamento.getCodContabilita(bd);
				singoloVersamento.getIbanAccredito(bd);
				singoloVersamento.getTipoContabilita(bd);
				singoloVersamento.getTributo(bd);
				
			}

		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}
	
	
	public PatchPendenzaDTOResponse patch(PatchPendenzaDTO patchPendenzaDTO) throws PendenzaNonTrovataException, GovPayException, NotAuthorizedException, NotAuthenticatedException, ValidationException{
		
		PatchPendenzaDTOResponse response = new PatchPendenzaDTOResponse();
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(patchPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA, bd);

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(bd, patchPendenzaDTO.getIdA2a()).getId(), patchPendenzaDTO.getIdPendenza());
			
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(patchPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA, versamentoLetto.getDominio(bd).getCodDominio(), null, bd);
			
			for(PatchOp op: patchPendenzaDTO.getOp()) {
				
				if("/stato".equals(op.getPath())) {
					
					if(!op.getOp().equals(OpEnum.REPLACE)) {
						throw new ValidationException("Op '"+op.getOp()+"' non valida per il path '"+op.getPath()+"'");
					}
					
					StatoVersamento nuovoStato = StatoVersamento.valueOf((String) op.getValue());

					switch (nuovoStato) {
					case ANNULLATO:
						if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
							versamentoLetto.setStatoVersamento(StatoVersamento.ANNULLATO);
						} else {
							throw new ValidationException("Non e' consentito aggiornare lo stato di una pendenza ad ANNULLATO da uno stato diverso da NON_ESEGUITO");
						}
						break;
					case NON_ESEGUITO:
						if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
							versamentoLetto.setStatoVersamento(StatoVersamento.NON_ESEGUITO);
						} else {
							throw new ValidationException("Non e' consentito aggiornare lo stato di una pendenza ad NON_ESEGUITO da uno stato diverso da ANNULLATO");
						}
					default:
						throw new ValidationException("Non e' consentito aggiornare lo stato di una pendenza ad " + nuovoStato.name());
					}
				}
				
				if("/descrizioneStato".equals(op.getPath())) {
					
					if(!op.getOp().equals(OpEnum.REPLACE)) {
						throw new ValidationException("Op '"+op.getOp()+"' non valida per il path '"+op.getPath()+"'");
					}
					
					String descrizioneStato = (String) op.getValue();
					versamentoLetto.setDescrizioneStato(descrizioneStato);
				}
			}
			
			versamentoLetto.setDataUltimoAggiornamento(new Date());
			versamentiBD.updateVersamento(versamentoLetto);
			return response;
			
		} catch (ServiceException e) {
			throw new GovPayException(e);
		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}

	}
	
	public PutPendenzaDTOResponse createOrUpdate(PutPendenzaDTO putVersamentoDTO) throws PendenzaNonTrovataException, GovPayException, NotAuthorizedException, NotAuthenticatedException{ 
		PutPendenzaDTOResponse createOrUpdatePendenzaResponse = new PutPendenzaDTOResponse();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(putVersamentoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA, bd);
			
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			Versamento chiediVersamento = versamentoBusiness.chiediVersamento(putVersamentoDTO.getVersamento());
			
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(putVersamentoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA, chiediVersamento.getDominio(bd).getCodDominio(), null, bd);
			
			
			createOrUpdatePendenzaResponse.setCreated(false);
			VersamentiBD versamentiBD = new VersamentiBD(bd);

			try {
				versamentiBD.getVersamento(AnagraficaManager.getApplicazione(versamentiBD, putVersamentoDTO.getVersamento().getCodApplicazione()).getId(), putVersamentoDTO.getVersamento().getCodVersamentoEnte());
			}catch(NotFoundException e) {
				createOrUpdatePendenzaResponse.setCreated(true);
			}
			
			if(putVersamentoDTO.isAvvisaturaDigitale()) {
				chiediVersamento.setDaAvvisare(true);
			}
			
			versamentoBusiness.caricaVersamento(chiediVersamento, chiediVersamento.getNumeroAvviso() == null, true);
			
			// restituisco il versamento creato
			createOrUpdatePendenzaResponse.setVersamento(chiediVersamento);
			createOrUpdatePendenzaResponse.setDominio(chiediVersamento.getDominio(bd));
			
			Iuv iuv = IuvUtils.toIuv(chiediVersamento, chiediVersamento.getApplicazione(bd), chiediVersamento.getDominio(bd));
			
			createOrUpdatePendenzaResponse.setBarCode(iuv.getBarCode() != null ? new String(iuv.getBarCode()) : null);
			createOrUpdatePendenzaResponse.setQrCode(iuv.getQrCode() != null ? new String(iuv.getQrCode()) : null);
			
			if(putVersamentoDTO.isStampaAvviso()) {
				it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento(bd);
				AvvisoPagamento avvisoPagamento = new AvvisoPagamento();
				avvisoPagamento.setCodDominio(chiediVersamento.getDominio(bd).getCodDominio());
				avvisoPagamento.setIuv(iuv.getIuv());
				PrintAvvisoDTO printAvvisoDTO = new PrintAvvisoDTO();
				printAvvisoDTO.setAvviso(avvisoPagamento);
				AvvisoPagamentoInput input = avvisoBD.fromVersamento(avvisoPagamento, chiediVersamento);
				printAvvisoDTO.setInput(input); 
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvviso(printAvvisoDTO);
				createOrUpdatePendenzaResponse.setPdf(Base64.getEncoder().encodeToString(printAvvisoDTOResponse.getAvviso().getPdf()));
			}

		} catch (ServiceException e) {
			throw new GovPayException(e);
		} catch (Exception e) {
			throw new GovPayException(e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return createOrUpdatePendenzaResponse;
	}

}
