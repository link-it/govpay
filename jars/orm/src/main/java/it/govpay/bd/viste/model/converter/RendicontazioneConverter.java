package it.govpay.bd.viste.model.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Fr;
import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.viste.model.Rendicontazione;
import it.govpay.model.Anagrafica;
import it.govpay.model.Anagrafica.TIPO;
import it.govpay.model.Fr.StatoFr;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoAllegato;
import it.govpay.model.Pagamento.TipoPagamento;
import it.govpay.model.Rendicontazione.EsitoRendicontazione;
import it.govpay.model.Rendicontazione.StatoRendicontazione;
import it.govpay.model.SingoloVersamento.StatoSingoloVersamento;
import it.govpay.model.Versamento.StatoPagamento;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.Versamento.TipoSogliaVersamento;
import it.govpay.model.Versamento.TipologiaTipoVersamento;


public class RendicontazioneConverter {

	public static Rendicontazione toDTO(it.govpay.orm.VistaRendicontazione vo) throws ServiceException {
		Rendicontazione dto = new Rendicontazione();
		dto.setId(vo.getId());

		Fr fr= new Fr();

		fr.setStato(StatoFr.valueOf(vo.getFrStato()));
		fr.setCodBicRiversamento(vo.getFrCodBicRiversamento());
		fr.setCodFlusso(vo.getFrCodFlusso());
		fr.setCodDominio(vo.getFrCodDominio());
		fr.setCodPsp(vo.getFrCodPsp());
		fr.setDataAcquisizione(vo.getFrDataAcquisizione());
		fr.setDataFlusso(vo.getFrDataOraFlusso());
		fr.setDataRegolamento(vo.getFrDataRegolamento());
		fr.setDescrizioneStato(vo.getFrDescrizioneStato());
		fr.setId(vo.getFrId());
		fr.setImportoTotalePagamenti(BigDecimal.valueOf(vo.getFrImportoTotalePagamenti()));
		fr.setIur(vo.getFrIur());
		fr.setNumeroPagamenti(vo.getFrNumeroPagamenti());
		//		fr.setXml(vo.getFrXml()); // no blob
		if(vo.getFrIdIncasso() != null)
			fr.setIdIncasso(vo.getFrIdIncasso().getId());
		fr.setRagioneSocialeDominio(vo.getFrRagioneSocialeDominio());
		fr.setRagioneSocialePsp(vo.getFrRagioneSocialePsp());
		fr.setObsoleto(vo.getFrObsoleto());

		dto.setFr(fr);

		it.govpay.bd.model.Rendicontazione rendicontazione = new it.govpay.bd.model.Rendicontazione();

		rendicontazione.setAnomalie(vo.getRndAnomalie());
		rendicontazione.setIuv(vo.getRndIuv());
		rendicontazione.setIur(vo.getRndIur());
		rendicontazione.setIndiceDati(vo.getRndIndiceDati());
		rendicontazione.setImporto(BigDecimal.valueOf(vo.getRndImportoPagato()));
		rendicontazione.setData(vo.getRndData());
		rendicontazione.setEsito(EsitoRendicontazione.toEnum(vo.getRndEsito()));
		rendicontazione.setStato(StatoRendicontazione.valueOf(vo.getRndStato()));
		if(vo.getRndIdPagamento() != null)
			rendicontazione.setIdPagamento(vo.getRndIdPagamento().getId());

		dto.setRendicontazione(rendicontazione);

		SingoloVersamento singoloVersamento = new SingoloVersamento();

		if(vo.getSngIdTributo() != null)
			singoloVersamento.setIdTributo(vo.getSngIdTributo().getId());
		singoloVersamento.setImportoSingoloVersamento(BigDecimal.valueOf(vo.getSngImportoSingoloVersamento()));
		singoloVersamento.setCodSingoloVersamentoEnte(vo.getSngCodSingVersEnte());
		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.valueOf(vo.getSngStatoSingoloVersamento()));
		singoloVersamento.setDatiAllegati(vo.getSngDatiAllegati());
		singoloVersamento.setDescrizione(vo.getSngDescrizione());
		singoloVersamento.setIndiceDati(vo.getSngIndiceDati()); 
		singoloVersamento.setDescrizioneCausaleRPT(vo.getSngDescrizioneCausaleRPT());
		singoloVersamento.setContabilita(vo.getSngContabilita());

		dto.setSingoloVersamento(singoloVersamento);

		Versamento versamento = new Versamento();


		versamento.setId(vo.getId());
		versamento.setIdApplicazione(vo.getVrsIdApplicazione().getId());

		if(vo.getVrsIdUo() != null)
			versamento.setIdUo(vo.getVrsIdUo().getId());

		if(vo.getVrsIdDominio() != null)
			versamento.setIdDominio(vo.getVrsIdDominio().getId());

		if(vo.getVrsIdTipoVersamento() != null)
			versamento.setIdTipoVersamento(vo.getVrsIdTipoVersamento().getId());

		if(vo.getVrsIdTipoVersamentoDominio() != null)
			versamento.setIdTipoVersamentoDominio(vo.getVrsIdTipoVersamentoDominio().getId());
		versamento.setNome(vo.getVrsNome());
		versamento.setCodVersamentoEnte(vo.getVrsCodVersamentoEnte());
		versamento.setStatoVersamento(StatoVersamento.valueOf(vo.getVrsStatoVersamento()));
		versamento.setDescrizioneStato(vo.getVrsDescrizioneStato());
		versamento.setImportoTotale(BigDecimal.valueOf(vo.getVrsImportoTotale()));
		versamento.setAggiornabile(vo.isVrsAggiornabile());
		versamento.setDataCreazione(vo.getVrsDataCreazione());
		versamento.setDataValidita(vo.getVrsDataValidita());
		versamento.setDataScadenza(vo.getVrsDataScadenza());
		versamento.setDataUltimoAggiornamento(vo.getVrsDataOraUltimoAgg());
		try {
			versamento.setCausaleVersamento(vo.getVrsCausaleVersamento());
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
		Anagrafica debitore = new Anagrafica();
		if(vo.getVrsDebitoreTipo()!=null)
			debitore.setTipo(TIPO.valueOf(vo.getVrsDebitoreTipo()));
		debitore.setRagioneSociale(vo.getVrsDebitoreAnagrafica());
		debitore.setCap(vo.getVrsDebitoreCap());
		debitore.setCellulare(vo.getVrsDebitoreCellulare());
		debitore.setCivico(vo.getVrsDebitoreCivico());
		debitore.setCodUnivoco(vo.getVrsDebitoreIdentificativo());
		debitore.setEmail(vo.getVrsDebitoreEmail());
		debitore.setFax(vo.getVrsDebitoreFax());
		debitore.setIndirizzo(vo.getVrsDebitoreIndirizzo());
		debitore.setLocalita(vo.getVrsDebitoreLocalita());
		debitore.setNazione(vo.getVrsDebitoreNazione());
		debitore.setProvincia(vo.getVrsDebitoreProvincia());
		debitore.setTelefono(vo.getVrsDebitoreTelefono());
		versamento.setAnagraficaDebitore(debitore);

		if(vo.getVrsCodAnnoTributario() != null && !vo.getVrsCodAnnoTributario().isEmpty())
			versamento.setCodAnnoTributario(Integer.parseInt(vo.getVrsCodAnnoTributario()));

		versamento.setCodLotto(vo.getVrsCodLotto());

		versamento.setTassonomiaAvviso(vo.getVrsTassonomiaAvviso()); 
		versamento.setTassonomia(vo.getVrsTassonomia());

		versamento.setCodVersamentoLotto(vo.getVrsCodVersamentoLotto()); 
		versamento.setCodBundlekey(vo.getVrsCodBundlekey()); 
		versamento.setDatiAllegati(vo.getVrsDatiAllegati());
		if(vo.getVrsIncasso() != null) {
			versamento.setIncasso(vo.getVrsIncasso().equals(it.govpay.model.Versamento.INCASSO_TRUE) ? true : false);
		}
		versamento.setAnomalie(vo.getVrsAnomalie());

		versamento.setIuvVersamento(vo.getVrsIuvVersamento());
		versamento.setNumeroAvviso(vo.getVrsNumeroAvviso());

		// se il numero avviso e' impostato lo iuv proposto deve coincidere con quello inserito a partire dall'avviso
		if(versamento.getNumeroAvviso() !=  null) {
			versamento.setIuvProposto(versamento.getIuvVersamento());
		}

		versamento.setAck(vo.isVrsAck());
		versamento.setAnomalo(vo.isVrsAnomalo());

		versamento.setDirezione(vo.getVrsDirezione());
		versamento.setDivisione(vo.getVrsDivisione());
		versamento.setIdSessione(vo.getVrsIdSessione());

		versamento.setDataPagamento(vo.getVrsDataPagamento());
		if(vo.getVrsImportoPagato() != null)
			versamento.setImportoPagato(BigDecimal.valueOf(vo.getVrsImportoPagato())); 
		if(vo.getVrsImportoIncassato() != null)
			versamento.setImportoIncassato(BigDecimal.valueOf(vo.getVrsImportoIncassato()));
		if(vo.getVrsStatoPagamento() != null)
			versamento.setStatoPagamento(StatoPagamento.valueOf(vo.getVrsStatoPagamento())); 
		versamento.setIuvPagamento(vo.getVrsIuvPagamento());

		versamento.setDataPagamento(vo.getVrsDataPagamento());
		if(vo.getVrsImportoPagato() != null)
			versamento.setImportoPagato(BigDecimal.valueOf(vo.getVrsImportoPagato()));
		if(vo.getVrsImportoIncassato() != null)
			versamento.setImportoIncassato(BigDecimal.valueOf(vo.getVrsImportoIncassato()));
		if(vo.getVrsStatoPagamento() != null)
			versamento.setStatoPagamento(StatoPagamento.valueOf(vo.getVrsStatoPagamento())); 
		versamento.setIuvPagamento(vo.getVrsIuvPagamento());
		
		if(vo.getVrsIdDocumento() != null)
			versamento.setIdDocumento(vo.getVrsIdDocumento().getId());
		if(vo.getVrsCodRata() != null) {
			if(vo.getVrsCodRata().startsWith(TipoSogliaVersamento.ENTRO.toString())) {
				versamento.setTipoSoglia(TipoSogliaVersamento.ENTRO);
				String gg = vo.getVrsCodRata().substring(vo.getVrsCodRata().indexOf(TipoSogliaVersamento.ENTRO.toString())+ TipoSogliaVersamento.ENTRO.toString().length());
				versamento.setGiorniSoglia(Integer.parseInt(gg));
			} else if(vo.getVrsCodRata().startsWith(TipoSogliaVersamento.OLTRE.toString())) {
				versamento.setTipoSoglia(TipoSogliaVersamento.OLTRE);
				String gg = vo.getVrsCodRata().substring(vo.getVrsCodRata().indexOf(TipoSogliaVersamento.OLTRE.toString())+ TipoSogliaVersamento.OLTRE.toString().length());
				versamento.setGiorniSoglia(Integer.parseInt(gg));
			} else {
				versamento.setNumeroRata(Integer.parseInt(vo.getVrsCodRata()));
			}
		}
		
		if(vo.getVrsTipo() != null)
			versamento.setTipo(TipologiaTipoVersamento.toEnum(vo.getVrsTipo()));
		
		versamento.setProprieta(vo.getVrsProprieta());

		dto.setVersamento(versamento );
		
		it.govpay.bd.model.Pagamento pagamento = new it.govpay.bd.model.Pagamento();

		pagamento.setCodDominio(vo.getPagCodDominio());
		pagamento.setIuv(vo.getPagIuv());
		pagamento.setIur(vo.getPagIur());
		pagamento.setIndiceDati(vo.getPagIndiceDati());
		pagamento.setImportoPagato(BigDecimal.valueOf(vo.getPagImportoPagato()));
		pagamento.setDataAcquisizione(vo.getPagDataAcquisizione());
		pagamento.setDataPagamento(vo.getPagDataPagamento());
		if(vo.getPagCommissioniPsp() != null)
			pagamento.setCommissioniPsp(BigDecimal.valueOf(vo.getPagCommissioniPsp()));
		if(vo.getPagTipoAllegato() != null)
			pagamento.setTipoAllegato(TipoAllegato.valueOf(vo.getPagTipoAllegato()));
		pagamento.setAllegato(vo.getPagAllegato());

		pagamento.setDataAcquisizioneRevoca(vo.getPagDataAcquisizioneRevoca());
		pagamento.setCausaleRevoca(vo.getPagCausaleRevoca());
		pagamento.setDatiRevoca(vo.getPagDatiRevoca());
		pagamento.setEsitoRevoca(vo.getPagEsitoRevoca());
		pagamento.setDatiEsitoRevoca(vo.getPagDatiEsitoRevoca());
		if(vo.getPagImportoRevocato() != null)
			pagamento.setImportoRevocato(BigDecimal.valueOf(vo.getPagImportoRevocato()));
		if(vo.getPagStato() != null)
			pagamento.setStato(Stato.valueOf(vo.getPagStato()));

		pagamento.setTipo(TipoPagamento.valueOf(vo.getPagTipo()));
		
		dto.setPagamento(pagamento);
		
		singoloVersamento.setVersamento(versamento);
		pagamento.setSingoloVersamento(singoloVersamento);
		
		rendicontazione.setPagamento(pagamento);
		
		// Rpt
		
		if(vo.getRptIuv() != null) {
			Rpt rpt = new Rpt();
			rpt.setIuv(vo.getRptIuv());
			rpt.setCodDominio(vo.getPagCodDominio());
			rpt.setCcp(vo.getRptCcp());
			dto.setRpt(rpt);
			pagamento.setRpt(rpt);
		}
		
		// Incasso
		
		if(vo.getRncTrn() != null) {
			Incasso incasso = new Incasso();
			incasso.setCodDominio(vo.getPagCodDominio());
			incasso.setTrn(vo.getRncTrn());
			dto.setIncasso(incasso );
			pagamento.setIncasso(incasso);
		}
		
		return dto;
	}



	public static List<Rendicontazione> toDTO(
			List<it.govpay.orm.VistaRendicontazione> rendicontazioneVOLst) throws ServiceException {
		List<Rendicontazione> dto = new ArrayList<>();
		for(it.govpay.orm.VistaRendicontazione vo : rendicontazioneVOLst) {
			dto.add(toDTO(vo));
		}
		return dto;
	}
}
