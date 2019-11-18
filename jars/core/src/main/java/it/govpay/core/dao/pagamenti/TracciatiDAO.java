/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.model.Operazione;
import it.govpay.bd.model.Tracciato;
import it.govpay.bd.pagamento.OperazioniBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.filters.OperazioneFilter;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.beans.tracciati.TracciatoPendenza;
import it.govpay.core.business.Tracciati;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.ListaOperazioniTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.ListaOperazioniTracciatoDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PostTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.PostTracciatoDTOResponse;
import it.govpay.core.dao.pagamenti.exception.TracciatoNonTrovatoException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Tracciato.STATO_ELABORAZIONE;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;
import it.govpay.orm.constants.StatoTracciatoType;

public class TracciatiDAO extends BaseDAO{

	public TracciatiDAO() {
	}

	public Tracciato leggiTracciato(LeggiTracciatoDTO leggiTracciatoDTO) throws ServiceException,TracciatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			TracciatiBD tracciatoBD = new TracciatiBD(bd);
			Tracciato tracciato = tracciatoBD.getTracciato(leggiTracciatoDTO.getId());
			tracciato.getOperatore(bd);
			return tracciato;

		} catch (NotFoundException e) {
			throw new TracciatoNonTrovatoException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public byte[] leggiRichiestaTracciato(LeggiTracciatoDTO leggiTracciatoDTO) throws ServiceException,TracciatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			TracciatiBD tracciatoBD = new TracciatiBD(bd);
			Tracciato tracciato = tracciatoBD.getTracciato(leggiTracciatoDTO.getId());
			tracciato.getOperatore(bd);
			byte[] rawRichiesta = tracciato.getRawRichiesta();
			if(rawRichiesta == null)
				throw new NotFoundException("File di richiesta non salvato");
			return rawRichiesta;

		} catch (NotFoundException e) {
			throw new TracciatoNonTrovatoException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public byte[] leggiEsitoTracciato(LeggiTracciatoDTO leggiTracciatoDTO) throws ServiceException,TracciatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			TracciatiBD tracciatoBD = new TracciatiBD(bd);
			Tracciato tracciato = tracciatoBD.getTracciato(leggiTracciatoDTO.getId());
			
			tracciato.getOperatore(bd);
			byte[] rawEsito = tracciato.getRawEsito();
			if(rawEsito == null)
				throw new NotFoundException("File di esito non salvato");
			return rawEsito;


		} catch (NotFoundException e) {
			throw new TracciatoNonTrovatoException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
	
	public ListaTracciatiDTOResponse listaTracciati(ListaTracciatiDTO listaTracciatiDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			return this.listaTracciati(listaTracciatiDTO, bd);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public ListaTracciatiDTOResponse listaTracciati(ListaTracciatiDTO listaTracciatiDTO, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {

		TracciatiBD tracciatoBD = new TracciatiBD(bd);
		TracciatoFilter filter = tracciatoBD.newFilter();

		filter.setCodDominio(listaTracciatiDTO.getIdDominio());
		filter.setDomini(listaTracciatiDTO.getCodDomini());
		filter.setTipo(listaTracciatiDTO.getTipoTracciato());
		filter.setOffset(listaTracciatiDTO.getOffset());
		filter.setLimit(listaTracciatiDTO.getLimit());
		filter.setOperatore(listaTracciatiDTO.getOperatore());
		filter.setStato(listaTracciatiDTO.getStatoTracciato()); 
		filter.setDettaglioStato(listaTracciatiDTO.getDettaglioStato()); 
		filter.setCodTipoVersamento(listaTracciatiDTO.getIdTipoPendenza());
		filter.setFormato(listaTracciatiDTO.getFormatoTracciato());
		
		List<FilterSortWrapper> filterSortList = new ArrayList<>();
		FilterSortWrapper fsw = new FilterSortWrapper();
		fsw.setSortOrder(SortOrder.DESC);
		fsw.setField(it.govpay.orm.Tracciato.model().DATA_CARICAMENTO);
		filterSortList.add(fsw );
		filter.setFilterSortList(filterSortList );

		long count = tracciatoBD.count(filter);

		List<Tracciato> resList = new ArrayList<>();
		if(count > 0) {
			List<Tracciato> resListTmp = new ArrayList<>();
			
			resListTmp = tracciatoBD.findAll(filter);
			
			if(!resListTmp.isEmpty()) {
				for (Tracciato tracciato : resListTmp) {
					tracciato.getOperatore(bd);
					resList.add(tracciato);
				}
			}
		} 

		return new ListaTracciatiDTOResponse(count, resList);
	}
	
	public PostTracciatoDTOResponse create(PostTracciatoDTO postTracciatoDTO) throws NotAuthenticatedException, NotAuthorizedException, GovPayException {
		PostTracciatoDTOResponse postTracciatoDTOResponse = new PostTracciatoDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			
			SerializationConfig config = new SerializationConfig();
			config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
			config.setIgnoreNullValues(true);
			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
	
//			if(!AuthorizationManager.isDominioAuthorized(postTracciatoDTO.getUser(), postTracciatoDTO.getIdDominio())) {
//				throw AuthorizationManager.toNotAuthorizedException(postTracciatoDTO.getUser(), postTracciatoDTO.getIdDominio(), null);
//			}
			
			TracciatiBD tracciatoBD = new TracciatiBD(bd);
			
			it.govpay.core.beans.tracciati.TracciatoPendenza beanDati = new TracciatoPendenza();
			beanDati.setStepElaborazione(StatoTracciatoType.NUOVO.getValue());
			
			beanDati.setAvvisaturaAbilitata(postTracciatoDTO.getAvvisaturaDigitale());
			beanDati.setAvvisaturaModalita(postTracciatoDTO.getAvvisaturaModalita() != null ? postTracciatoDTO.getAvvisaturaModalita().getValue() : null); 
			
			Tracciato tracciato = new Tracciato();
			tracciato.setCodDominio(postTracciatoDTO.getIdDominio());
			tracciato.setBeanDati(serializer.getObject(beanDati));
			tracciato.setDataCaricamento(new Date());
			tracciato.setFileNameRichiesta(postTracciatoDTO.getNomeFile());
			tracciato.setRawRichiesta(postTracciatoDTO.getContenuto());
		
			tracciato.setIdOperatore(postTracciatoDTO.getOperatore().getId());
			tracciato.setTipo(TIPO_TRACCIATO.PENDENZA);
			tracciato.setStato(STATO_ELABORAZIONE.ELABORAZIONE);
			tracciato.setFormato(postTracciatoDTO.getFormato());
			tracciato.setCodTipoVersamento(postTracciatoDTO.getIdTipoPendenza());
			
			tracciatoBD.insertTracciato(tracciato);
			
			// avvio elaborazione tracciato
			it.govpay.core.business.Operazioni.setEseguiElaborazioneTracciati();
			
			postTracciatoDTOResponse.setCreated(true);
			
			tracciato.getOperatore(bd);
			postTracciatoDTOResponse.setTracciato(tracciato);
			return postTracciatoDTOResponse;
		} catch (ServiceException | IOException e) {
			throw new GovPayException(e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}

	}
	
	public ListaOperazioniTracciatoDTOResponse listaOperazioniTracciatoPendenza(ListaOperazioniTracciatoDTO listaOperazioniTracciatoDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			return this.listaOperazioniTracciatoPendenza(listaOperazioniTracciatoDTO, bd);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
	
	public ListaOperazioniTracciatoDTOResponse listaOperazioniTracciatoPendenza(ListaOperazioniTracciatoDTO listaOperazioniTracciatoDTO, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		OperazioniBD operazioniBD = new OperazioniBD(bd);
		OperazioneFilter filter = operazioniBD.newFilter();

		filter.setIdTracciato(listaOperazioniTracciatoDTO.getIdTracciato());
		filter.setOffset(listaOperazioniTracciatoDTO.getOffset());
		filter.setLimit(listaOperazioniTracciatoDTO.getLimit());
		filter.setStato(listaOperazioniTracciatoDTO.getStato());
		filter.setTipo(listaOperazioniTracciatoDTO.getTipo());

		long count = operazioniBD.count(filter);

		List<Operazione> resList = new ArrayList<>();
		if(count > 0) {
			List<Operazione> resListTmp = operazioniBD.findAll(filter);
			
			Tracciati tracciatiBD = new Tracciati(bd);
			for (Operazione operazione : resListTmp) {
				resList.add(tracciatiBD.fillOperazione(operazione).getOperazione());
			}
		} 
		
		return new ListaOperazioniTracciatoDTOResponse(count, resList);
	}
	
}
