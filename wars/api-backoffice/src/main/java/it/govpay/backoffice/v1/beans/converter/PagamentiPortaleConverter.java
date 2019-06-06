package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IOException;

import it.govpay.backoffice.v1.beans.ContoAddebito;
import it.govpay.backoffice.v1.beans.Nota;
import it.govpay.backoffice.v1.beans.Pagamento;
import it.govpay.backoffice.v1.beans.Pagamento.ModelloEnum;
import it.govpay.backoffice.v1.beans.PagamentoIndex;
import it.govpay.backoffice.v1.beans.PagamentoPost;
import it.govpay.backoffice.v1.beans.Rpp;
import it.govpay.backoffice.v1.beans.StatoPagamento;
import it.govpay.bd.model.Evento;
import it.govpay.bd.model.converter.EventoConverter;
import it.govpay.bd.model.eventi.EventoNota;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.dao.pagamenti.dto.LeggiPagamentoPortaleDTOResponse;
import it.govpay.core.dao.pagamenti.dto.LeggiRptDTOResponse;
import it.govpay.core.utils.UriBuilderUtils;

public class PagamentiPortaleConverter {

	public static final String PENDENZE_KEY = "pendenze";
	public static final String VOCI_PENDENZE_KEY = "voci";
	public static final String ID_A2A_KEY = "idA2A";
	public static final String ID_PENDENZA_KEY = "idPendenza";
	public static final String ID_DOMINIO_KEY = "idDominio";
	public static final String IUV_KEY = "iuv";

	public static Pagamento toRsModel(LeggiPagamentoPortaleDTOResponse dto) throws ServiceException, IOException {
		return toRsModel(dto.getPagamento(), dto.getListaRpp(),dto.getEventi());
	}

	public static Pagamento toRsModel(it.govpay.bd.model.PagamentoPortale pagamentoPortale,List<LeggiRptDTOResponse> listaRpp, List<Evento> eventi) throws ServiceException, IOException {
		Pagamento rsModel = new Pagamento();

		PagamentoPost pagamentiPortaleRequest = null;
		if(pagamentoPortale.getJsonRequest() != null) {
			try {
				pagamentiPortaleRequest = JSONSerializable.parse(pagamentoPortale.getJsonRequest(), PagamentoPost.class);
			
				if(pagamentiPortaleRequest.getContoAddebito()!=null) {
					ContoAddebito contoAddebito = new ContoAddebito();
					contoAddebito.setBic(pagamentiPortaleRequest.getContoAddebito().getBic());
					contoAddebito.setIban(pagamentiPortaleRequest.getContoAddebito().getIban());
					rsModel.setContoAddebito(contoAddebito);
				}
				rsModel.setDataEsecuzionePagamento(pagamentiPortaleRequest.getDataEsecuzionePagamento());
				rsModel.setCredenzialiPagatore(pagamentiPortaleRequest.getCredenzialiPagatore());
				rsModel.setSoggettoVersante(AnagraficaConverter.toSoggettoRsModel(AnagraficaConverter.toAnagrafica(pagamentiPortaleRequest.getSoggettoVersante())));
				rsModel.setAutenticazioneSoggetto(it.govpay.backoffice.v1.beans.Pagamento.AutenticazioneSoggettoEnum.fromValue(pagamentiPortaleRequest.getAutenticazioneSoggetto()));
			} catch (ServiceException | ValidationException e) {
				
			}
		}

		rsModel.setId(pagamentoPortale.getIdSessione());
		rsModel.setIdSessionePortale(pagamentoPortale.getIdSessionePortale());
		rsModel.setIdSessionePsp(pagamentoPortale.getIdSessionePsp());
		rsModel.setNome(pagamentoPortale.getNome());
		rsModel.setStato(StatoPagamento.valueOf(pagamentoPortale.getStato().toString()));
		rsModel.setDescrizioneStato(pagamentoPortale.getDescrizioneStato()); 
		rsModel.setPspRedirectUrl(pagamentoPortale.getPspRedirectUrl());
		rsModel.setUrlRitorno(pagamentoPortale.getUrlRitorno());
		rsModel.setDataRichiestaPagamento(pagamentoPortale.getDataRichiesta());
		rsModel.setImporto(pagamentoPortale.getImporto()); 
		
		if(listaRpp !=null) {
			List<Rpp> rpp = new ArrayList<>();
			for(LeggiRptDTOResponse leggiRptDtoResponse: listaRpp) {
				rpp.add(RptConverter.toRsModel(leggiRptDtoResponse.getRpt()));
			}
			rsModel.setRpp(rpp);
		}
		
		if(eventi !=null && !eventi.isEmpty()) {
			List<Nota> note = new ArrayList<>();
			for(Evento evento: eventi) {
				switch (evento.getCategoriaEvento()) { 
				case INTERFACCIA_INTEGRAZIONE:
				case INTERFACCIA_COOPERAZIONE:
					break;
				case INTERNO:
				case UTENTE:
				default:
					EventoNota nota = EventoConverter.toEventoNota(evento);
					note.add(NoteConverter.toRsModel(nota));
					break;
				}
			}
			rsModel.setNote(note);
		}

		rsModel.setVerificato(pagamentoPortale.isAck());

		if(pagamentoPortale.getTipo() == 1) {
			rsModel.setModello(ModelloEnum.ENTE);	
		} else if(pagamentoPortale.getTipo() == 3) {
			rsModel.setModello(ModelloEnum.PSP);
		}
		
		return rsModel;
	}
	public static PagamentoIndex toRsModelIndex(LeggiPagamentoPortaleDTOResponse dto) throws ServiceException, IOException {
		it.govpay.bd.model.PagamentoPortale pagamentoPortale = dto.getPagamento();
		PagamentoIndex rsModel = new PagamentoIndex();

		PagamentoPost pagamentiPortaleRequest = null;
		
		if(pagamentoPortale.getJsonRequest() != null) {
			try {
				pagamentiPortaleRequest = JSONSerializable.parse(pagamentoPortale.getJsonRequest(), PagamentoPost.class);
				if(pagamentiPortaleRequest.getContoAddebito()!=null) {
					ContoAddebito contoAddebito = new ContoAddebito();
					contoAddebito.setBic(pagamentiPortaleRequest.getContoAddebito().getBic());
					contoAddebito.setIban(pagamentiPortaleRequest.getContoAddebito().getIban());
					rsModel.setContoAddebito(contoAddebito);
				}
				rsModel.setDataEsecuzionePagamento(pagamentiPortaleRequest.getDataEsecuzionePagamento());
				rsModel.setCredenzialiPagatore(pagamentiPortaleRequest.getCredenzialiPagatore());
				rsModel.setSoggettoVersante(AnagraficaConverter.toSoggettoRsModel(AnagraficaConverter.toAnagrafica(pagamentiPortaleRequest.getSoggettoVersante())));
				rsModel.setAutenticazioneSoggetto(it.govpay.backoffice.v1.beans.PagamentoIndex.AutenticazioneSoggettoEnum.fromValue(pagamentiPortaleRequest.getAutenticazioneSoggetto()));
			} catch (ServiceException | ValidationException e) {
				
			}
		}
		rsModel.setId(pagamentoPortale.getIdSessione());
		rsModel.setIdSessionePortale(pagamentoPortale.getIdSessionePortale());
		rsModel.setIdSessionePsp(pagamentoPortale.getIdSessionePsp());
		rsModel.setNome(pagamentoPortale.getNome());
		rsModel.setStato(StatoPagamento.valueOf(pagamentoPortale.getStato().toString()));
		rsModel.setDescrizioneStato(pagamentoPortale.getDescrizioneStato());
		rsModel.setPspRedirectUrl(pagamentoPortale.getPspRedirectUrl());
		rsModel.setUrlRitorno(pagamentoPortale.getUrlRitorno());
		rsModel.setDataRichiestaPagamento(pagamentoPortale.getDataRichiesta());
		rsModel.setRpp(UriBuilderUtils.getRptsByPagamento(pagamentoPortale.getIdSessione()));

		rsModel.setImporto(pagamentoPortale.getImporto()); 
		
		if(dto.getEventi() !=null && !dto.getEventi() .isEmpty()) {
			List<Nota> note = new ArrayList<>();
			for(Evento evento: dto.getEventi()) {
				switch (evento.getCategoriaEvento()) { 
				case INTERFACCIA_INTEGRAZIONE:
				case INTERFACCIA_COOPERAZIONE:
					break;
				case INTERNO:
				case UTENTE:
				default:
					EventoNota nota = EventoConverter.toEventoNota(evento);
					note.add(NoteConverter.toRsModel(nota));
					break;
				}
			}
			rsModel.setNote(note);
		}

		rsModel.setVerificato(pagamentoPortale.isAck());

		if(pagamentoPortale.getTipo() == 1) {
			rsModel.setModello(it.govpay.backoffice.v1.beans.PagamentoIndex.ModelloEnum.ENTE);	
		} else if(pagamentoPortale.getTipo() == 3) {
			rsModel.setModello(it.govpay.backoffice.v1.beans.PagamentoIndex.ModelloEnum.PSP);
		}

		return rsModel;

	}
}
