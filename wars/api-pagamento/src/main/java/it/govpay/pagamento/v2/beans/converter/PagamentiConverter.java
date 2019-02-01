package it.govpay.pagamento.v2.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.UriInfo;

import org.joda.time.LocalDate;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;
import org.springframework.security.core.Authentication;

import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTOResponse;
import it.govpay.core.exceptions.RequestValidationException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Versamento.ModoAvvisatura;
import it.govpay.pagamento.v2.api.impl.PagamentiApiServiceImpl;
import it.govpay.pagamento.v2.api.impl.PendenzeApiServiceImpl;
import it.govpay.pagamento.v2.api.impl.TransazioniApiServiceImpl;
import it.govpay.pagamento.v2.beans.Bollo;
import it.govpay.pagamento.v2.beans.Conto;
import it.govpay.pagamento.v2.beans.Entrata;
import it.govpay.pagamento.v2.beans.ModalitaAvvisaturaDigitale;
import it.govpay.pagamento.v2.beans.NuovaPendenza;
import it.govpay.pagamento.v2.beans.NuovaVocePendenza;
import it.govpay.pagamento.v2.beans.NuovoPagamento;
import it.govpay.pagamento.v2.beans.Pagamenti;
import it.govpay.pagamento.v2.beans.Pagamento;
import it.govpay.pagamento.v2.beans.PagamentoBase;
import it.govpay.pagamento.v2.beans.PagamentoCreato;
import it.govpay.pagamento.v2.beans.PagamentoIndex;
import it.govpay.pagamento.v2.beans.PendenzaIndex;
import it.govpay.pagamento.v2.beans.RiferimentoAvviso;
import it.govpay.pagamento.v2.beans.RiferimentoEntrata;
import it.govpay.pagamento.v2.beans.RiferimentoPendenza;
import it.govpay.pagamento.v2.beans.RppIndex;
import it.govpay.pagamento.v2.beans.Soggetto;
import it.govpay.pagamento.v2.beans.StatoPagamento;
import it.govpay.pagamento.v2.beans.TipoAutenticazioneSoggetto;

public class PagamentiConverter {

	public static Pagamenti toRsModel(List<LeggiPagamentoPortaleDTOResponse> listaDTO, UriInfo uriInfo, int offset, int limit, long total) {
		Pagamenti pagamenti = new Pagamenti();
		
		List<PagamentoIndex> items = new ArrayList<PagamentoIndex>();
		ConverterUtils.popolaLista(pagamenti, uriInfo.getRequestUriBuilder(), listaDTO.size(), offset, limit, total);
		
		for (LeggiPagamentoPortaleDTOResponse dto : listaDTO) {
			items.add(toRsModelIndex(dto.getPagamento()));
		}
		
		pagamenti.setItems(items);
		
		return pagamenti;
	}
	
	public static Pagamento toRsModel(LeggiPagamentoPortaleDTOResponse dto) throws ServiceException {
		Pagamento rsModel = new Pagamento();
		it.govpay.bd.model.PagamentoPortale pagamentoPortale = dto.getPagamento();
		fillPagamentoBase(rsModel, pagamentoPortale);
		
		List<RppIndex> rpp = new ArrayList<>();
		for(LeggiRptDTOResponse leggiRptDtoResponse: dto.getListaRpp()) {
			rpp.add(RppConverter.toRsModelIndex(leggiRptDtoResponse.getRpt(),leggiRptDtoResponse.getVersamento(),leggiRptDtoResponse.getApplicazione()));
		}
		rsModel.setRpps(rpp);

		List<PendenzaIndex> pendenze = new ArrayList<>();
		for(LeggiPendenzaDTOResponse leggiRptDtoResponse: dto.getListaPendenze()) {
			pendenze.add(PendenzeConverter.toPendenzaIndex(leggiRptDtoResponse.getVersamentoIncasso()));
		}
		rsModel.setPendenze(pendenze);
		
		return rsModel;
	}
	
	
	private static PagamentoBase fillPagamentoBase(PagamentoBase rsModel, it.govpay.bd.model.PagamentoPortale pagamentoPortale) {
		NuovoPagamento pagamentiPortaleRequest = null;
		
		if(pagamentoPortale.getJsonRequest()!=null)
			try {
				SerializationConfig serializationConfig = new SerializationConfig();
				serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
				serializationConfig.setIgnoreNullValues(true);
				IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
				pagamentiPortaleRequest = NuovoPagamento.class.cast(deserializer.getObject(pagamentoPortale.getJsonRequest(), NuovoPagamento.class));
			
				if(pagamentiPortaleRequest.getContoAddebito()!=null) {
					Conto contoAddebito = new Conto();
					contoAddebito.setBic(pagamentiPortaleRequest.getContoAddebito().getBic());
					contoAddebito.setIban(pagamentiPortaleRequest.getContoAddebito().getIban());
					rsModel.setContoAddebito(contoAddebito);
				}
				rsModel.setDataEsecuzionePagamento(pagamentiPortaleRequest.getDataEsecuzionePagamento());
				rsModel.setCredenzialiPagatore(pagamentiPortaleRequest.getCredenzialiPagatore());
				rsModel.setSoggettoVersante(pagamentiPortaleRequest.getSoggettoVersante());
				rsModel.setAutenticazioneSoggetto(pagamentiPortaleRequest.getAutenticazioneSoggetto());
			} catch (org.openspcoop2.utils.serialization.IOException e) {
			}
		rsModel.setId(pagamentoPortale.getIdSessione());
		rsModel.setIdSessionePsp(pagamentoPortale.getIdSessionePsp());
		rsModel.setNome(pagamentoPortale.getNome());
		rsModel.setStato(StatoPagamento.valueOf(pagamentoPortale.getStato().toString()));
		rsModel.setRedirect(pagamentoPortale.getPspRedirectUrl());
		rsModel.setUrlRitorno(pagamentoPortale.getUrlRitorno());
		if(pagamentoPortale.getDataRichiesta() != null)
			rsModel.setDataEsecuzionePagamento(new LocalDate(pagamentoPortale.getDataRichiesta()));

		if(pagamentoPortale.getImporto() != null) 
			rsModel.setImporto(BigDecimal.valueOf(pagamentoPortale.getImporto())); 
		
		return rsModel;
	}
	
	public static PagamentoIndex toRsModelIndex(it.govpay.bd.model.PagamentoPortale pagamentoPortale) {
		PagamentoIndex rsModel = new PagamentoIndex();
		
		fillPagamentoBase(rsModel, pagamentoPortale);
		
		rsModel.setHref(PagamentiApiServiceImpl.basePath.clone().path(pagamentoPortale.getIdSessione()).build().toString());
		rsModel.setPendenze(PendenzeApiServiceImpl.basePath.clone().queryParam("idSessionePortale", pagamentoPortale.getIdSessione()).build().toString());
		rsModel.setRpps(TransazioniApiServiceImpl.basePath.clone().queryParam("idSessionePortale", pagamentoPortale.getIdSessione()).build().toString());

		return rsModel;

	}
	
	public static PagamentoCreato getPagamentiPortaleResponseOk(PagamentiPortaleDTOResponse dtoResponse) {
		PagamentoCreato  json = new PagamentoCreato();

		json.setId(dtoResponse.getId());
		json.setHref(PagamentiApiServiceImpl.basePath.clone().path(dtoResponse.getId()).build().toString()); 
		json.setRedirect(dtoResponse.getRedirectUrl());
		json.setIdSession(dtoResponse.getIdSessione()); 
		json.setIdCarrello(dtoResponse.getIdCarrelloRpt()); 

		return json;
	}
	
	public static PagamentiPortaleDTO getPagamentiPortaleDTO(NuovoPagamento nuovoPagamento, Authentication user, String idSessione, String idSessionePortale,Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale) throws Exception {

		PagamentiPortaleDTO pagamentiPortaleDTO = new PagamentiPortaleDTO(user);
		
		pagamentiPortaleDTO.setAvvisaturaDigitale(avvisaturaDigitale);
		ModoAvvisatura avvisaturaModalita = null;
		if(modalitaAvvisaturaDigitale != null) {
				avvisaturaModalita = modalitaAvvisaturaDigitale.equals(ModalitaAvvisaturaDigitale.BATCH) ? ModoAvvisatura.ASICNRONA : ModoAvvisatura.SINCRONA;
		}
		
		pagamentiPortaleDTO.setAvvisaturaModalita(avvisaturaModalita);

		pagamentiPortaleDTO.setIdSessione(idSessione);
		pagamentiPortaleDTO.setIdSessionePortale(idSessionePortale);
		
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
		ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
		pagamentiPortaleDTO.setJsonRichiesta(serializer.getObject(nuovoPagamento));
		
		if(nuovoPagamento.getAutenticazioneSoggetto() != null)
			pagamentiPortaleDTO.setAutenticazioneSoggetto(nuovoPagamento.getAutenticazioneSoggetto().toString());
		else 
			pagamentiPortaleDTO.setAutenticazioneSoggetto(TipoAutenticazioneSoggetto.N_A.toString());

		pagamentiPortaleDTO.setCredenzialiPagatore(nuovoPagamento.getCredenzialiPagatore());
		if(nuovoPagamento.getDataEsecuzionePagamento() != null)
			pagamentiPortaleDTO.setDataEsecuzionePagamento(nuovoPagamento.getDataEsecuzionePagamento().toDate());

		if(nuovoPagamento.getContoAddebito() != null) {
			pagamentiPortaleDTO.setBicAddebito(nuovoPagamento.getContoAddebito().getBic());
			pagamentiPortaleDTO.setIbanAddebito(nuovoPagamento.getContoAddebito().getIban());
		}

		pagamentiPortaleDTO.setUrlRitorno(nuovoPagamento.getUrlRitorno());

		if(nuovoPagamento.getSoggettoVersante() != null);
			pagamentiPortaleDTO.setVersante(toAnagraficaCommons(nuovoPagamento.getSoggettoVersante()));

		if(nuovoPagamento.getPendenze() != null && nuovoPagamento.getPendenze().size() > 0 ) {
			List<Object> listRefs = new ArrayList<>();

			int i =0;
//			for (NuovaPendenza pendenza: nuovoPagamento.getPendenze()) {
			for (Object pendenzaObj: nuovoPagamento.getPendenze()) {

//				if((pendenza.getIdDominio() != null && pendenza.getNumeroAvviso() != null) && (pendenza.getIdA2A() == null && pendenza.getIdPendenza() == null)) {
				if(pendenzaObj instanceof RiferimentoAvviso) {
					RiferimentoAvviso pendenza = (RiferimentoAvviso) pendenzaObj;
					PagamentiPortaleDTO.RefVersamentoAvviso ref = pagamentiPortaleDTO. new RefVersamentoAvviso();
					ref.setIdDominio(pendenza.getIdDominio());
					ref.setNumeroAvviso(pendenza.getNumeroAvviso());
					// ref.setIdDebitore(pendenza.getIdDebitore()); // TODO Riportare dalla V1
					listRefs.add(ref);

//				} else	if((pendenza.getIdDominio() == null) && (pendenza.getIdA2A() != null && pendenza.getIdPendenza() != null)) {
				} else	if(pendenzaObj instanceof RiferimentoPendenza) {
					RiferimentoPendenza pendenza = (RiferimentoPendenza) pendenzaObj;
					PagamentiPortaleDTO.RefVersamentoPendenza ref = pagamentiPortaleDTO. new RefVersamentoPendenza();
					ref.setIdA2A(pendenza.getIdA2A());
					ref.setIdPendenza(pendenza.getIdPendenza());
					listRefs.add(ref);

//				}else if(pendenza.getIdA2A() != null && pendenza.getIdPendenza() != null && pendenza.getIdDominio() != null) {
				}else if(pendenzaObj instanceof NuovaPendenza) {
					NuovaPendenza pendenza = (NuovaPendenza) pendenzaObj;
					it.govpay.core.dao.commons.Versamento versamento = getVersamentoFromPendenza(pendenza);
					
					versamento.setAvvisaturaAbilitata(pagamentiPortaleDTO.getAvvisaturaDigitale());
					versamento.setModoAvvisatura(pagamentiPortaleDTO.getAvvisaturaModalita() != null ? pagamentiPortaleDTO.getAvvisaturaModalita().getValue() : null); 
					
					listRefs.add(versamento);
				} else {
					throw new RequestValidationException("La pendenza "+(i+1)+" e' di un tipo non riconosciuto.");
				}
				i++;
			}

			pagamentiPortaleDTO.setPendenzeOrPendenzeRef(listRefs);
		}

		return pagamentiPortaleDTO;
	}
	
	public static it.govpay.core.dao.commons.Versamento getVersamentoFromPendenza(NuovaPendenza pendenza) {
		it.govpay.core.dao.commons.Versamento versamento = new it.govpay.core.dao.commons.Versamento();

		if(pendenza.getAnnoRiferimento() != null)
			versamento.setAnnoTributario(pendenza.getAnnoRiferimento().intValue());

		versamento.setCausale(pendenza.getDescrizione());
		versamento.setCodApplicazione(pendenza.getIdA2A());

		versamento.setCodDominio(pendenza.getIdDominio());
		versamento.setCodUnitaOperativa(pendenza.getIdUnitaOperativa());
		versamento.setCodVersamentoEnte(pendenza.getIdPendenza());
		if(pendenza.getDataScadenza() != null)
			versamento.setDataScadenza(pendenza.getDataScadenza().toDate());
		if(pendenza.getDataValidita() != null)
			versamento.setDataValidita(pendenza.getDataValidita().toDate());
		versamento.setDataCaricamento(new Date());
		versamento.setDebitore(toAnagraficaCommons(pendenza.getSoggettoPagatore()));
		versamento.setImportoTotale(pendenza.getImporto());
	
		if(pendenza.getTassonomiaAvviso() != null)
			versamento.setTassonomiaAvviso(pendenza.getTassonomiaAvviso().toString());
		
		versamento.setNumeroAvviso(pendenza.getNumeroAvviso());
		if(pendenza.getDatiAllegati() != null)
		versamento.setDatiAllegati(pendenza.getDatiAllegati().toString());
		versamento.setCartellaPagamento(pendenza.getCartellaPagamento());

		// voci pagamento
		fillSingoliVersamentiFromVociPendenza(versamento, pendenza.getVoci());

		return versamento;
	}
	
	
	public static void fillSingoliVersamentiFromVociPendenza(it.govpay.core.dao.commons.Versamento versamento, List<NuovaVocePendenza> voci) {

		if(voci != null && voci.size() > 0) {
			for (NuovaVocePendenza vocePendenza : voci) {
				it.govpay.core.dao.commons.Versamento.SingoloVersamento sv = new it.govpay.core.dao.commons.Versamento.SingoloVersamento();

				//sv.setCodTributo(value); ??

				sv.setCodSingoloVersamentoEnte(vocePendenza.getIdVocePendenza());
				if(vocePendenza.getDatiAllegati() != null)
					sv.setDatiAllegati(vocePendenza.getDatiAllegati().toString());
				sv.setDescrizione(vocePendenza.getDescrizione());
				sv.setImporto(vocePendenza.getImporto());
				 
				Object dettaglioVoceObj = vocePendenza.getDettaglioVoce();

				// Definisce i dati di un bollo telematico
//				if(vocePendenza.getHashDocumento() != null && vocePendenza.getTipoBollo() != null && vocePendenza.getProvinciaResidenza() != null) {
//					it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico bollo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico();
//					bollo.setHash(vocePendenza.getHashDocumento());
//					bollo.setProvincia(vocePendenza.getProvinciaResidenza());
//					bollo.setTipo(vocePendenza.getTipoBollo());
//					sv.setBolloTelematico(bollo);
//				} else if(vocePendenza.getCodEntrata() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
//					sv.setCodTributo(vocePendenza.getCodEntrata());
//
//				} else { // Definisce i dettagli di incasso della singola entrata.
//					it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo();
//					tributo.setCodContabilita(vocePendenza.getCodiceContabilita());
//					tributo.setIbanAccredito(vocePendenza.getIbanAccredito());
//					tributo.setTipoContabilita(it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo.TipoContabilita.valueOf(vocePendenza.getTipoContabilita().name()));
//					sv.setTributo(tributo);
//				}
				
				
				// Definisce i dati di un bollo telematico
				if(dettaglioVoceObj instanceof Bollo) {
					Bollo dettaglioVoceBollo = (Bollo) dettaglioVoceObj; 
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico bollo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.BolloTelematico();
					bollo.setHash(dettaglioVoceBollo.getHashDocumento());
					bollo.setProvincia(dettaglioVoceBollo.getProvinciaResidenza());
					bollo.setTipo(dettaglioVoceBollo.getTipoBollo());
					sv.setBolloTelematico(bollo);
				} else if(dettaglioVoceObj instanceof RiferimentoEntrata) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
					RiferimentoEntrata dettaglioVoceRiferimentoEntrata = (RiferimentoEntrata) dettaglioVoceObj; 
					sv.setCodTributo(dettaglioVoceRiferimentoEntrata.getCodEntrata());

				} else { // Definisce i dettagli di incasso della singola entrata.
					Entrata dettaglioVoceEntrata = (Entrata) dettaglioVoceObj; 
					it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo tributo = new it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo();
					tributo.setCodContabilita(dettaglioVoceEntrata.getCodiceContabilita());
					tributo.setIbanAccredito(dettaglioVoceEntrata.getIbanAccredito());
					tributo.setTipoContabilita(it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo.TipoContabilita.valueOf(dettaglioVoceEntrata.getTipoContabilita().name()));
					sv.setTributo(tributo);
				}

				versamento.getSingoloVersamento().add(sv);
			}
		}
	}
	
	public static it.govpay.core.dao.commons.Anagrafica toAnagraficaCommons(Soggetto anagraficaRest) {
		it.govpay.core.dao.commons.Anagrafica anagraficaCommons = null;
		if(anagraficaRest != null) {
			anagraficaCommons = new it.govpay.core.dao.commons.Anagrafica();
			anagraficaCommons.setCap(anagraficaRest.getCap());
			anagraficaCommons.setCellulare(anagraficaRest.getCellulare());
			anagraficaCommons.setCivico(anagraficaRest.getCivico());
			anagraficaCommons.setCodUnivoco(anagraficaRest.getIdentificativo());
			anagraficaCommons.setEmail(anagraficaRest.getEmail());
			anagraficaCommons.setIndirizzo(anagraficaRest.getIndirizzo());
			anagraficaCommons.setLocalita(anagraficaRest.getLocalita());
			anagraficaCommons.setNazione(anagraficaRest.getNazione());
			anagraficaCommons.setProvincia(anagraficaRest.getProvincia());
			anagraficaCommons.setRagioneSociale(anagraficaRest.getAnagrafica());
			anagraficaCommons.setTipo(anagraficaRest.getTipo().name());
		}

		return anagraficaCommons;
	}
}
