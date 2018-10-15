package it.govpay.rs.v1.beans.converter;

import java.math.BigDecimal;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.binary.Base64;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.xml.sax.SAXException;

import it.gov.agenziaentrate._2014.marcadabollo.TipoMarcaDaBollo;
import it.govpay.bd.model.Pagamento;
import it.govpay.core.rs.v1.beans.base.Allegato;
import it.govpay.core.rs.v1.beans.base.Allegato.TipoEnum;
import it.govpay.core.rs.v1.beans.base.Riscossione;
import it.govpay.core.rs.v1.beans.base.StatoRiscossione;
import it.govpay.core.rs.v1.beans.base.TipoRiscossione;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.rs.BaseRsService;

public class RiscossioniConverter {
	
	public static Riscossione toRsModel(Pagamento input) {
		Riscossione rsModel = new Riscossione();
		try {
			rsModel.setIdDominio(input.getCodDominio());
			rsModel.setIuv(input.getIuv());
			rsModel.setIur(input.getIur());
			rsModel.setIndice(new BigDecimal(input.getIndiceDati()));
			
			rsModel.setPendenza(UriBuilderUtils.getPendenzaByIdA2AIdPendenza(input.getSingoloVersamento(null).getVersamento(null).getApplicazione(null).getCodApplicazione(), input.getSingoloVersamento(null).getVersamento(null).getCodVersamentoEnte()));
			rsModel.setIdVocePendenza(input.getSingoloVersamento(null).getCodSingoloVersamentoEnte());
			rsModel.setRpp(UriBuilderUtils.getRppByDominioIuvCcp(input.getRpt(null).getCodDominio(), input.getRpt(null).getIuv(), input.getRpt(null).getCcp()));
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
			
			if(input.getTipo().equals(TipoPagamento.ENTRATA)) {
				rsModel.setTipo(TipoRiscossione.ENTRATA);
			} else {
				rsModel.setTipo(TipoRiscossione.MBT);
			} 
			
			rsModel.setCommissioni(input.getCommissioniPsp());
			Allegato allegato = new Allegato();
			allegato.setTesto(Base64.encodeBase64String(input.getAllegato()));
			if(input.getTipoAllegato() != null)
				allegato.setTipo(TipoEnum.fromCodifica(input.getTipoAllegato().toString()));
			
			if(allegato.getTipo() != null && allegato.getTipo().equals(TipoEnum.MARCA_DA_BOLLO)) {
				byte[] xmlMarca = input.getAllegato();
				try {
					allegato.setContenuto(JaxbUtils.toMarcaDaBollo(xmlMarca, false));
				} catch (JAXBException | SAXException e) {
					allegato.setContenuto(new TipoMarcaDaBollo());
				}
			}
			rsModel.setAllegato(allegato);
			
			if(input.getIncasso(null)!=null)
				rsModel.setIncasso(UriBuilderUtils.getIncassiByIdDominioIdIncasso(input.getCodDominio(), input.getIncasso(null).getTrn()));
			
		} catch(ServiceException e) {
			LoggerWrapperFactory.getLogger(BaseRsService.class).error("Errore nella conversione del pagamento: " + e.getMessage(), e);
		}

		return rsModel;
	}
	
}
