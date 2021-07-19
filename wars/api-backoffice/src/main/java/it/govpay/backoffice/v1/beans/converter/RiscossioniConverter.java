package it.govpay.backoffice.v1.beans.converter;

import java.io.IOException;
import java.math.BigDecimal;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.binary.Base64;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.xml.sax.SAXException;

import it.gov.agenziaentrate._2014.marcadabollo.MarcaDaBollo;
import it.govpay.backoffice.v1.beans.Allegato;
import it.govpay.backoffice.v1.beans.Allegato.TipoEnum;
import it.govpay.backoffice.v1.beans.Riscossione;
import it.govpay.backoffice.v1.beans.RiscossioneIndex;
import it.govpay.backoffice.v1.beans.StatoRiscossione;
import it.govpay.backoffice.v1.beans.TipoRiscossione;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.rs.BaseRsService;

public class RiscossioniConverter {
	
	public static RiscossioneIndex toRsModelIndex(it.govpay.bd.viste.model.Pagamento dto) {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		RiscossioneIndex rsModel = new RiscossioneIndex();
		try {
			Pagamento input = dto.getPagamento();
			Versamento versamento = dto.getVersamento();
			SingoloVersamento singoloVersamento = dto.getSingoloVersamento();
			Rpt rpt = dto.getRpt();
			Incasso incasso = dto.getIncasso();
			
			rsModel.setIdDominio(input.getCodDominio());
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));
			rsModel.setImporto(input.getImportoPagato());
			rsModel.setData(input.getDataPagamento());
			switch (input.getTipo()) {
			case ALTRO_INTERMEDIARIO:
				rsModel.setTipo(TipoRiscossione.ALTRO_INTERMEDIARIO);
				break;
			case ENTRATA:
				rsModel.setTipo(TipoRiscossione.ENTRATA);
				break;
			case MBT:
				rsModel.setTipo(TipoRiscossione.MBT);
				break;
			case ENTRATA_PA_NON_INTERMEDIATA:
				rsModel.setTipo(TipoRiscossione.ENTRATA_PA_NON_INTERMEDIATA);
				break;
			}
			
			// solo per i pagamenti interni
			if(!input.getTipo().equals(TipoPagamento.ALTRO_INTERMEDIARIO)) {
				rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(versamento.getApplicazione(configWrapper).getCodApplicazione(), versamento.getCodVersamentoEnte()));
				rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
				
				if(rpt!= null)
					rsModel.setRpp(UriBuilderUtils.getRppByDominioIuvCcp(rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp()));
				
				Stato stato = input.getStato();
				if(stato != null) {
					switch(stato) {
					case INCASSATO: rsModel.setStato(StatoRiscossione.INCASSATA);
						break;
					case PAGATO: rsModel.setStato(StatoRiscossione.RISCOSSA);
						break;
					case PAGATO_SENZA_RPT: rsModel.setStato(StatoRiscossione.RISCOSSA);
						break;
					default:
						break;
					}
				}
				
				rsModel.setCommissioni(input.getCommissioniPsp());
				Allegato allegato = new Allegato();
				allegato.setTesto(Base64.encodeBase64String(input.getAllegato()));
				if(input.getTipoAllegato() != null)
					allegato.setTipo(TipoEnum.fromCodifica(input.getTipoAllegato().toString()));
				
				if(allegato.getTipo() != null && allegato.getTipo().equals(TipoEnum.MARCA_DA_BOLLO)) {
					byte[] xmlMarca = input.getAllegato();
					try {
						allegato.setContenuto(JaxbUtils.toMarcaDaBollo(xmlMarca));
					} catch (JAXBException | SAXException e) {
						allegato.setContenuto(new MarcaDaBollo());
					}
				}
				rsModel.setAllegato(allegato);
			}
			
			if(incasso !=null)
				rsModel.setIncasso(UriBuilderUtils.getIncassiByIdDominioIdIncasso(incasso.getCodDominio(), incasso.getTrn()));
		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}
	
//	public static RiscossioneIndex toRsModelIndex(Pagamento input) {
//		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
//		RiscossioneIndex rsModel = new RiscossioneIndex();
//		try {
//			rsModel.setIdDominio(input.getCodDominio());
//			rsModel.setIuv(input.getIuv());
//			rsModel.setIur(input.getIur());
//			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));
//			
//			rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(input.getSingoloVersamento(null).getVersamento(null).getApplicazione(configWrapper).getCodApplicazione(), input.getSingoloVersamento(null).getVersamento(null).getCodVersamentoEnte()));
//			rsModel.setIdVocePendenza(input.getSingoloVersamento(null).getCodSingoloVersamentoEnte());
//			Rpt rpt = input.getRpt(null);
//			if(rpt!= null)
//				rsModel.setRpp(UriBuilderUtils.getRppByDominioIuvCcp(rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp()));
//			rsModel.setImporto(input.getImportoPagato());
//			rsModel.setData(input.getDataPagamento());
//			Stato stato = input.getStato();
//			switch(stato) {
//			case INCASSATO: rsModel.setStato(StatoRiscossione.INCASSATA);
//				break;
//			case PAGATO: rsModel.setStato(StatoRiscossione.RISCOSSA);
//				break;
//			case PAGATO_SENZA_RPT: rsModel.setStato(StatoRiscossione.RISCOSSA);
//				break;
//			default:
//				break;
//			}
//			
//			if(input.getTipo().equals(TipoPagamento.ENTRATA)) {
//				rsModel.setTipo(TipoRiscossione.ENTRATA);
//			} else {
//				rsModel.setTipo(TipoRiscossione.MBT);
//			} 
//			
//			rsModel.setCommissioni(input.getCommissioniPsp());
//			Allegato allegato = new Allegato();
//			allegato.setTesto(Base64.encodeBase64String(input.getAllegato()));
//			if(input.getTipoAllegato() != null)
//				allegato.setTipo(TipoEnum.fromCodifica(input.getTipoAllegato().toString()));
//			
//			if(allegato.getTipo() != null && allegato.getTipo().equals(TipoEnum.MARCA_DA_BOLLO)) {
//				byte[] xmlMarca = input.getAllegato();
//				try {
//					allegato.setContenuto(JaxbUtils.toMarcaDaBollo(xmlMarca));
//				} catch (JAXBException | SAXException e) {
//					allegato.setContenuto(new MarcaDaBollo());
//				}
//			}
//			rsModel.setAllegato(allegato);
//			
//			if(input.getIncasso(null)!=null)
//				rsModel.setIncasso(UriBuilderUtils.getIncassiByIdDominioIdIncasso(input.getCodDominio(), input.getIncasso(null).getTrn()));
//			
//		} catch(ServiceException e) {
//			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
//		}
//
//		return rsModel;
//	}
	
	public static Riscossione toRsModel(Pagamento input) throws IOException, ValidationException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Riscossione rsModel = new Riscossione();
		try {
			rsModel.setIdDominio(input.getCodDominio());
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));
			rsModel.setImporto(input.getImportoPagato());
			rsModel.setData(input.getDataPagamento());
			switch (input.getTipo()) {
			case ALTRO_INTERMEDIARIO:
				rsModel.setTipo(TipoRiscossione.ALTRO_INTERMEDIARIO);
				break;
			case ENTRATA:
				rsModel.setTipo(TipoRiscossione.ENTRATA);
				break;
			case MBT:
				rsModel.setTipo(TipoRiscossione.MBT);
				break;
			case ENTRATA_PA_NON_INTERMEDIATA:
				rsModel.setTipo(TipoRiscossione.ENTRATA_PA_NON_INTERMEDIATA);
				break;
			}
			
			// solo per i pagamenti interni
			if(!input.getTipo().equals(TipoPagamento.ALTRO_INTERMEDIARIO)) {
				rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(input.getSingoloVersamento(null).getVersamento(null).getApplicazione(configWrapper).getCodApplicazione(), input.getSingoloVersamento(null).getVersamento(null).getCodVersamentoEnte()));
				rsModel.setVocePendenza(PendenzeConverter.toVocePendenzaRiscossioneRsModel(input.getSingoloVersamento(null), input.getSingoloVersamento(null).getVersamento(null)));
				Rpt rpt = input.getRpt(null);
				if(rpt!= null)
					rsModel.setRpp(UriBuilderUtils.getRppByDominioIuvCcp(rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp()));
				rsModel.setImporto(input.getImportoPagato());
				rsModel.setData(input.getDataPagamento());
				Stato stato = input.getStato();
				switch(stato) {
				case INCASSATO: rsModel.setStato(StatoRiscossione.INCASSATA);
					break;
				case PAGATO: rsModel.setStato(StatoRiscossione.RISCOSSA);
					break;
				case PAGATO_SENZA_RPT: rsModel.setStato(StatoRiscossione.RISCOSSA);
					break;
				default:
					break;
				}
				
				
				rsModel.setCommissioni(input.getCommissioniPsp());
				Allegato allegato = new Allegato();
				allegato.setTesto(Base64.encodeBase64String(input.getAllegato()));
				if(input.getTipoAllegato() != null)
					allegato.setTipo(TipoEnum.fromCodifica(input.getTipoAllegato().toString()));
				
				if(allegato.getTipo() != null && allegato.getTipo().equals(TipoEnum.MARCA_DA_BOLLO)) {
					byte[] xmlMarca = input.getAllegato();
					try {
						allegato.setContenuto(JaxbUtils.toMarcaDaBollo(xmlMarca));
					} catch (JAXBException | SAXException e) {
						allegato.setContenuto(new MarcaDaBollo());
					}
				}
				rsModel.setAllegato(allegato);
			}
			if(input.getIncasso(null)!=null)
				rsModel.setIncasso(UriBuilderUtils.getIncassiByIdDominioIdIncasso(input.getCodDominio(), input.getIncasso(null).getTrn()));
			
		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}
	
}
