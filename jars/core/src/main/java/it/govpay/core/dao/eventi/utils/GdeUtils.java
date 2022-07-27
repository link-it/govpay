package it.govpay.core.dao.eventi.utils;

import java.math.BigDecimal;
import java.time.ZoneOffset;
import java.util.Date;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.bd.model.Evento;
import it.govpay.bd.model.eventi.DatiPagoPA;
import it.govpay.core.exceptions.BaseExceptionV1;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.EventoContext;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.SeveritaProperties;
import it.govpay.core.utils.client.exception.ClientException;
import it.govpay.gde.GdeInvoker;
import it.govpay.gde.v1.model.NuovoEvento;
import it.govpay.gde.v1.model.NuovoEvento.CategoriaEventoEnum;
import it.govpay.gde.v1.model.NuovoEvento.ComponenteEnum;
import it.govpay.gde.v1.model.NuovoEvento.EsitoEnum;
import it.govpay.gde.v1.model.NuovoEvento.RuoloEnum;

public class GdeUtils {
	
	public static void salvaEvento(EventoContext context) {
		GdeInvoker gdeInvoker = new GdeInvoker(GovpayConfig.getInstance().getGiornaleEventiUrl());
		gdeInvoker.salvaEvento(GdeUtils.toEventoModel(context));
	}
	
	public static void salvaEvento(Evento context) {
		GdeInvoker gdeInvoker = new GdeInvoker(GovpayConfig.getInstance().getGiornaleEventiUrl());
		gdeInvoker.salvaEvento(GdeUtils.toEventoModel(context));
	}

	public static NuovoEvento toEventoModel(Evento context) {
		NuovoEvento dto = new NuovoEvento();

		if(context.getCategoriaEvento() != null) {
			switch (context.getCategoriaEvento()) {
			case INTERFACCIA:
				dto.setCategoriaEvento(CategoriaEventoEnum.INTERFACCIA);
				break;
			case INTERNO:
				dto.setCategoriaEvento(CategoriaEventoEnum.INTERNO);
				break;
			case UTENTE:
				dto.setCategoriaEvento(CategoriaEventoEnum.UTENTE);
				break;
			}
		}
		if(context.getComponente() != null) {
			try {
				dto.setComponente(ComponenteEnum.fromValue(context.getComponente())); 
			} catch(IllegalArgumentException e) {
				dto.setComponente(ComponenteEnum.GOVPAY);
			}
		}
		
		if(context.getData() != null) {
			dto.setDataEvento(context.getData().toInstant().atOffset(ZoneOffset.UTC));
		}
		
		dto.setDatiPagoPA(getDatiPagoPA(context)); 

		dto.setDettaglioEsito(context.getDettaglioEsito());
		if(context.getEsitoEvento() != null) {
			switch(context.getEsitoEvento()) {
			case FAIL:
				dto.setEsito(EsitoEnum.FAIL);
				break;
			case KO:
				dto.setEsito(EsitoEnum.KO);
				break;
			case OK:
				dto.setEsito(EsitoEnum.OK);
				break;
			}
		}
		//dto.setId(context.getId());
		dto.setDurataEvento(context.getIntervallo());

//		dto.setParametriRichiesta(context.getDettaglioRichiesta());
//		dto.setParametriRisposta(context.getDettaglioRisposta());
		
		if(context.getRuoloEvento() != null) {
			switch(context.getRuoloEvento()) {
			case CLIENT:
				dto.setRuolo(RuoloEnum.CLIENT);
				break;
			case SERVER:
				dto.setRuolo(RuoloEnum.SERVER);
				break;
			}
		}
		dto.setSottotipoEsito(context.getSottotipoEsito());
		dto.setSottotipoEvento(context.getSottotipoEvento());
		dto.setTipoEvento(context.getTipoEvento());
		dto.setIdA2A(context.getCodApplicazione());
		dto.setIdPendenza(context.getCodVersamentoEnte());
		dto.setIdDominio(context.getCodDominio());
		dto.setIuv(context.getIuv());
		dto.setCcp(context.getCcp());
		dto.setIdPagamento(context.getIdSessione());
//		dto.setIdFr(context.getIdFr());
//		dto.setIdTracciato(context.getIdTracciato());
//		dto.setIdIncasso(context.getIdIncasso());
		
		if(context.getSeverita() != null) {
			dto.setSeverita(context.getSeverita());
		}
		
		return dto;
	}

	public static NuovoEvento toEventoModel(EventoContext context) {
		NuovoEvento dto = new NuovoEvento();

		if(context.getCategoriaEvento() != null) {
			switch (context.getCategoriaEvento()) {
			case INTERFACCIA:
				dto.setCategoriaEvento(CategoriaEventoEnum.INTERFACCIA);
				break;
			case INTERNO:
				dto.setCategoriaEvento(CategoriaEventoEnum.INTERNO);
				break;
			case UTENTE:
				dto.setCategoriaEvento(CategoriaEventoEnum.UTENTE);
				break;
			}
		}
		if(context.getComponente() != null) {
			switch (context.getComponente()) {
			case API_BACKEND_IO:
				dto.setComponente(ComponenteEnum.API_BACKEND_IO);
				break;
			case API_BACKOFFICE:
				dto.setComponente(ComponenteEnum.API_BACKOFFICE);
				break;
			case API_ENTE:
				dto.setComponente(ComponenteEnum.API_ENTE);
				break;
			case API_GOVPAY:
				dto.setComponente(ComponenteEnum.API_GOVPAY);
				break;
			case API_HYPERSIC_APK:
				dto.setComponente(ComponenteEnum.API_HYPERSIC_APK);
				break;
			case API_MAGGIOLI_JPPA:
				dto.setComponente(ComponenteEnum.API_MAGGIOLI_JPPA);
				break;
			case API_MYPIVOT:
				dto.setComponente(ComponenteEnum.API_MYPIVOT);
				break;
			case API_PAGAMENTO:
				dto.setComponente(ComponenteEnum.API_PAGAMENTO);
				break;
			case API_PAGOPA:
				dto.setComponente(ComponenteEnum.API_PAGOPA);
				break;
			case API_PENDENZE:
				dto.setComponente(ComponenteEnum.API_PENDENZE);
				break;
			case API_RAGIONERIA:
				dto.setComponente(ComponenteEnum.API_RAGIONERIA);
				break;
			case API_SECIM:
				dto.setComponente(ComponenteEnum.API_SECIM);
				break;
			case API_USER:
				dto.setComponente(ComponenteEnum.API_USER);
				break;
			case GOVPAY:
				dto.setComponente(ComponenteEnum.GOVPAY);
				break;
			case API_WC:
				// dto.setComponente(ComponenteEnum.API_BACKEND_IO);
				break;
			}
		}
		
		if(context.getDataRichiesta() != null) {
			dto.setDataEvento(context.getDataRichiesta().toInstant().atOffset(ZoneOffset.UTC));
		}
		
		dto.setDatiPagoPA(getDatiPagoPA(context)); 

		dto.setDettaglioEsito(context.getDescrizioneEsito());
		if(context.getEsito() != null) {
			switch(context.getEsito()) {
			case FAIL:
				dto.setEsito(EsitoEnum.FAIL);
				break;
			case KO:
				dto.setEsito(EsitoEnum.KO);
				break;
			case OK:
				dto.setEsito(EsitoEnum.OK);
				break;
			}
		}
		//dto.setId(context.getId());
		if(context.getDataRisposta() != null) {
			if(context.getDataRichiesta() != null) {
				dto.setDurataEvento  (context.getDataRisposta().getTime() - context.getDataRichiesta().getTime());
			} else {
				dto.setDurataEvento(0l);
			}
		} else {
			dto.setDurataEvento(0l);
		}
		dto.setParametriRichiesta(context.getDettaglioRichiesta());
		dto.setParametriRisposta(context.getDettaglioRisposta());
		if(context.getRole() != null) {
			switch(context.getRole()) {
			case CLIENT:
				dto.setRuolo(RuoloEnum.CLIENT);
				break;
			case SERVER:
				dto.setRuolo(RuoloEnum.SERVER);
				break;
			}
		}
		dto.setSottotipoEsito(context.getSottotipoEsito());
		dto.setSottotipoEvento(context.getSottotipoEvento());
		dto.setTipoEvento(context.getTipoEvento());
		dto.setIdA2A(context.getIdA2A());
		dto.setIdPendenza(context.getIdPendenza());
		dto.setIdDominio(context.getCodDominio());
		dto.setIuv(context.getIuv());
		dto.setCcp(context.getCcp());
		dto.setIdPagamento(context.getIdPagamento());
//		dto.setIdFr(context.getIdFr());
//		dto.setIdTracciato(context.getIdTracciato());
//		dto.setIdIncasso(context.getIdIncasso());
		
		if(context.getSeverita() != null) {
			dto.setSeverita(context.getSeverita());
		} else {
			if(context.getException() != null) {
				LoggerWrapperFactory.getLogger(EventoContext.class).debug("Classe exception: " + context.getException().getClass());
				
				if(context.getException() instanceof GovPayException) {
					try {
						dto.setSeverita(SeveritaProperties.getInstance().getSeverita(((GovPayException) context.getException()).getCodEsito()));
					} catch (Exception e) {
						LoggerWrapperFactory.getLogger(EventoContext.class).error("Errore durante la decodifica del livello di severita': " + e.getMessage(),e);
					}
				}
				
				if(context.getException() instanceof BaseExceptionV1) {
					try {
						dto.setSeverita(SeveritaProperties.getInstance().getSeverita(((BaseExceptionV1) context.getException()).getCategoria()));
					} catch (Exception e) {
						LoggerWrapperFactory.getLogger(EventoContext.class).error("Errore durante la decodifica del livello di severita': " + e.getMessage(),e);
					}
				}
				
				if(context.getException() instanceof UnprocessableEntityException) {
					try {
						dto.setSeverita(SeveritaProperties.getInstance().getSeverita(((UnprocessableEntityException) context.getException()).getCategoria()));
					} catch (Exception e) {
						LoggerWrapperFactory.getLogger(EventoContext.class).error("Errore durante la decodifica del livello di severita': " + e.getMessage(),e);
					}
				}
				
				if(context.getException() instanceof ValidationException) {
					try {
						dto.setSeverita(SeveritaProperties.getInstance().getSeverita(it.govpay.core.exceptions.BaseExceptionV1.CategoriaEnum.RICHIESTA));
					} catch (Exception e) {
						LoggerWrapperFactory.getLogger(EventoContext.class).error("Errore durante la decodifica del livello di severita': " + e.getMessage(),e);
					}
				}
				
				if(context.getException() instanceof ClientException) {
					dto.setSeverita(5);
				}
			}	
		}

		return dto;
	}
	
	private static it.govpay.gde.v1.model.DatiPagoPA getDatiPagoPA(Evento evento) {
		DatiPagoPA datiPagoPA = evento.getPagoPA();
		Date dataFlusso = null; // evento.getDataFlusso();
		Long idTracciato = evento.getIdTracciato();
		
		it.govpay.gde.v1.model.DatiPagoPA datiPagoPAModel = getDatiPagoPA(datiPagoPA, dataFlusso, idTracciato);
		return datiPagoPAModel;
	}
	
	private static it.govpay.gde.v1.model.DatiPagoPA getDatiPagoPA(EventoContext evento) {
		DatiPagoPA datiPagoPA = evento.getDatiPagoPA();
		Date dataFlusso = evento.getDataFlusso();
		Long idTracciato = evento.getIdTracciato();
		
		it.govpay.gde.v1.model.DatiPagoPA datiPagoPAModel = getDatiPagoPA(datiPagoPA, dataFlusso, idTracciato);
		return datiPagoPAModel;
	}

	private static it.govpay.gde.v1.model.DatiPagoPA getDatiPagoPA(DatiPagoPA datiPagoPA, Date dataFlusso, Long idTracciato) {
		it.govpay.gde.v1.model.DatiPagoPA datiPagoPAModel = null;
		if(datiPagoPA != null) {
			datiPagoPAModel = new it.govpay.gde.v1.model.DatiPagoPA();
			datiPagoPAModel.setIdCanale(datiPagoPA.getCodCanale());
			datiPagoPAModel.setIdPsp(datiPagoPA.getCodPsp());
			datiPagoPAModel.setIdIntermediarioPsp(datiPagoPA.getCodIntermediarioPsp());
			datiPagoPAModel.setIdIntermediario(datiPagoPA.getCodIntermediario());
			datiPagoPAModel.setIdStazione(datiPagoPA.getCodStazione());
			datiPagoPAModel.setIdDominio(datiPagoPA.getCodDominio());
			if(datiPagoPA.getTipoVersamento() != null) {
				datiPagoPAModel.setTipoVersamento(datiPagoPA.getTipoVersamento().getCodifica());
			}
			if(datiPagoPA.getModelloPagamento() != null) {
				datiPagoPAModel.setModelloPagamento(datiPagoPA.getModelloPagamento().getCodifica() +"");
			}
			
			datiPagoPAModel.setIdFlusso(datiPagoPA.getCodFlusso());
			if(datiPagoPA.getIdTracciato() != null)
				datiPagoPAModel.setIdTracciato(new BigDecimal(datiPagoPA.getIdTracciato()));
			datiPagoPAModel.setIdRiconciliazione(datiPagoPA.getTrn());
			datiPagoPAModel.setSct(datiPagoPA.getSct());
			
			
			if(dataFlusso != null) {
				datiPagoPAModel.setDataFlusso(dataFlusso.toInstant().atOffset(ZoneOffset.UTC));
			}
			
			
			if(idTracciato != null)
				datiPagoPAModel.setIdTracciato(new BigDecimal(idTracciato));
			
		}
		return datiPagoPAModel;
	}
	
	
}
