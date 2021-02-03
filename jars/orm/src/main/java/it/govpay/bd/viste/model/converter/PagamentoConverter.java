package it.govpay.bd.viste.model.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Incasso;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.viste.model.Pagamento;
import it.govpay.model.Pagamento.Stato;
import it.govpay.model.Pagamento.TipoAllegato;
import it.govpay.model.Pagamento.TipoPagamento;

public class PagamentoConverter {

	public static Pagamento toDTO(it.govpay.orm.VistaPagamento vo) throws ServiceException {
		Pagamento dto = new Pagamento();
		dto.setId(vo.getId());

		it.govpay.bd.model.Pagamento pagamento = new it.govpay.bd.model.Pagamento();

		pagamento.setId(vo.getId());
		pagamento.setCodDominio(vo.getCodDominio());
		pagamento.setIuv(vo.getIuv());
		pagamento.setIur(vo.getIur());
		pagamento.setIndiceDati(vo.getIndiceDati());
		pagamento.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
		pagamento.setDataAcquisizione(vo.getDataAcquisizione());
		pagamento.setDataPagamento(vo.getDataPagamento());
		if(vo.getCommissioniPsp() != null)
			pagamento.setCommissioniPsp(BigDecimal.valueOf(vo.getCommissioniPsp()));
		if(vo.getTipoAllegato() != null)
			pagamento.setTipoAllegato(TipoAllegato.valueOf(vo.getTipoAllegato()));
		pagamento.setAllegato(vo.getAllegato());
//		if(vo.getIdRPT() != null) 
//			dto.setIdRpt(vo.getIdRPT().getId());
//		if(vo.getIdSingoloVersamento() != null)
//			dto.setIdSingoloVersamento(vo.getIdSingoloVersamento().getId());
//		if(vo.getIdRr() != null)
//			dto.setIdRr(vo.getIdRr().getId());

		pagamento.setDataAcquisizioneRevoca(vo.getDataAcquisizioneRevoca());
		pagamento.setCausaleRevoca(vo.getCausaleRevoca());
		pagamento.setDatiRevoca(vo.getDatiRevoca());
		pagamento.setEsitoRevoca(vo.getEsitoRevoca());
		pagamento.setDatiEsitoRevoca(vo.getDatiEsitoRevoca());
		if(vo.getImportoRevocato() != null)
			pagamento.setImportoRevocato(BigDecimal.valueOf(vo.getImportoRevocato()));
		if(vo.getStato() != null)
			pagamento.setStato(Stato.valueOf(vo.getStato()));

//		if(vo.getIdIncasso() != null)
//			pagamento.setIdIncasso(vo.getIdIncasso().getId());
		
		pagamento.setTipo(TipoPagamento.valueOf(vo.getTipo()));
		
		dto.setPagamento(pagamento);
		
		// Rpt
		
		if(vo.getRptIuv() != null) {
			Rpt rpt = new Rpt();
			rpt.setIuv(vo.getRptIuv());
			rpt.setCodDominio(vo.getCodDominio());
			rpt.setCcp(vo.getRptCcp());
			dto.setRpt(rpt);
		}
		
		// Incasso
		
		if(vo.getRncTrn() != null) {
			Incasso incasso = new Incasso();
			incasso.setCodDominio(vo.getCodDominio());
			incasso.setTrn(vo.getRncTrn());
			dto.setIncasso(incasso );
		}

		// SV

		SingoloVersamento singoloVersamento = new SingoloVersamento();

//		if(vo.getSngIdTributo() != null)
//			singoloVersamento.setIdTributo(vo.getSngIdTributo().getId());
//		singoloVersamento.setImportoSingoloVersamento(BigDecimal.valueOf(vo.getSngImportoSingoloVersamento()));
		singoloVersamento.setCodSingoloVersamentoEnte(vo.getSngCodSingVersEnte());
//		singoloVersamento.setStatoSingoloVersamento(StatoSingoloVersamento.valueOf(vo.getSngStatoSingoloVersamento()));
//		singoloVersamento.setDatiAllegati(vo.getSngDatiAllegati());
//		singoloVersamento.setDescrizione(vo.getSngDescrizione());
//		singoloVersamento.setIndiceDati(vo.getSngIndiceDati()); 
//		singoloVersamento.setDescrizioneCausaleRPT(vo.getSngDescrizioneCausaleRPT());

		dto.setSingoloVersamento(singoloVersamento);
		
		// V

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
//		versamento.setNome(vo.getVrsNome());
		versamento.setCodVersamentoEnte(vo.getVrsCodVersamentoEnte());
//		versamento.setStatoVersamento(StatoVersamento.valueOf(vo.getVrsStatoVersamento()));
//		versamento.setDescrizioneStato(vo.getVrsDescrizioneStato());
//		versamento.setImportoTotale(BigDecimal.valueOf(vo.getVrsImportoTotale()));
//		versamento.setAggiornabile(vo.isVrsAggiornabile());
//		versamento.setDataCreazione(vo.getVrsDataCreazione());
//		versamento.setDataValidita(vo.getVrsDataValidita());
//		versamento.setDataScadenza(vo.getVrsDataScadenza());
//		versamento.setDataUltimoAggiornamento(vo.getVrsDataOraUltimoAgg());
//		try {
//			versamento.setCausaleVersamento(vo.getVrsCausaleVersamento());
//		} catch (UnsupportedEncodingException e) {
//			throw new ServiceException(e);
//		}
//		Anagrafica debitore = new Anagrafica();
//		if(vo.getVrsDebitoreTipo()!=null)
//			debitore.setTipo(TIPO.valueOf(vo.getVrsDebitoreTipo()));
//		debitore.setRagioneSociale(vo.getVrsDebitoreAnagrafica());
//		debitore.setCap(vo.getVrsDebitoreCap());
//		debitore.setCellulare(vo.getVrsDebitoreCellulare());
//		debitore.setCivico(vo.getVrsDebitoreCivico());
//		debitore.setCodUnivoco(vo.getVrsDebitoreIdentificativo());
//		debitore.setEmail(vo.getVrsDebitoreEmail());
//		debitore.setFax(vo.getVrsDebitoreFax());
//		debitore.setIndirizzo(vo.getVrsDebitoreIndirizzo());
//		debitore.setLocalita(vo.getVrsDebitoreLocalita());
//		debitore.setNazione(vo.getVrsDebitoreNazione());
//		debitore.setProvincia(vo.getVrsDebitoreProvincia());
//		debitore.setTelefono(vo.getVrsDebitoreTelefono());
//		versamento.setAnagraficaDebitore(debitore);
//
//		if(vo.getVrsCodAnnoTributario() != null && !vo.getVrsCodAnnoTributario().isEmpty())
//			versamento.setCodAnnoTributario(Integer.parseInt(vo.getVrsCodAnnoTributario()));
//
//		versamento.setCodLotto(vo.getVrsCodLotto());

//		versamento.setTassonomiaAvviso(vo.getVrsTassonomiaAvviso()); 
		versamento.setTassonomia(vo.getVrsTassonomia());

//		versamento.setCodVersamentoLotto(vo.getVrsCodVersamentoLotto()); 
//		versamento.setCodBundlekey(vo.getVrsCodBundlekey()); 
//		versamento.setDatiAllegati(vo.getVrsDatiAllegati());
//		if(vo.getVrsIncasso() != null) {
//			versamento.setIncasso(vo.getVrsIncasso().equals(it.govpay.model.Versamento.INCASSO_TRUE) ? true : false);
//		}
//		versamento.setAnomalie(vo.getVrsAnomalie());
//
//		versamento.setIuvVersamento(vo.getVrsIuvVersamento());
//		versamento.setNumeroAvviso(vo.getVrsNumeroAvviso());

		// se il numero avviso e' impostato lo iuv proposto deve coincidere con quello inserito a partire dall'avviso
//		if(versamento.getNumeroAvviso() !=  null) {
//			versamento.setIuvProposto(versamento.getIuvVersamento());
//		}

//		versamento.setAck(vo.isVrsAck());
//		versamento.setAnomalo(vo.isVrsAnomalo());

		versamento.setDirezione(vo.getVrsDirezione());
		versamento.setDivisione(vo.getVrsDivisione());
//		versamento.setIdSessione(vo.getVrsIdSessione());
//
//		versamento.setDataPagamento(vo.getVrsDataPagamento());
//		if(vo.getVrsImportoPagato() != null)
//			versamento.setImportoPagato(BigDecimal.valueOf(vo.getVrsImportoPagato())); 
//		if(vo.getVrsImportoIncassato() != null)
//			versamento.setImportoIncassato(BigDecimal.valueOf(vo.getVrsImportoIncassato()));
//		if(vo.getVrsStatoPagamento() != null)
//			versamento.setStatoPagamento(StatoPagamento.valueOf(vo.getVrsStatoPagamento())); 
//		versamento.setIuvPagamento(vo.getVrsIuvPagamento());
//
//		versamento.setDataPagamento(vo.getVrsDataPagamento());
//		if(vo.getVrsImportoPagato() != null)
//			versamento.setImportoPagato(BigDecimal.valueOf(vo.getVrsImportoPagato()));
//		if(vo.getVrsImportoIncassato() != null)
//			versamento.setImportoIncassato(BigDecimal.valueOf(vo.getVrsImportoIncassato()));
//		if(vo.getVrsStatoPagamento() != null)
//			versamento.setStatoPagamento(StatoPagamento.valueOf(vo.getVrsStatoPagamento())); 
//		versamento.setIuvPagamento(vo.getVrsIuvPagamento());
		
//		if(vo.getVrsIdDocumento() != null)
//			versamento.setIdDocumento(vo.getVrsIdDocumento().getId());
//		if(vo.getVrsCodRata() != null) {
//			if(vo.getVrsCodRata().startsWith(TipoSogliaVersamento.ENTRO.toString())) {
//				versamento.setTipoSoglia(TipoSogliaVersamento.ENTRO);
//				String gg = vo.getVrsCodRata().substring(vo.getVrsCodRata().indexOf(TipoSogliaVersamento.ENTRO.toString())+ TipoSogliaVersamento.ENTRO.toString().length());
//				versamento.setGiorniSoglia(Integer.parseInt(gg));
//			} else if(vo.getVrsCodRata().startsWith(TipoSogliaVersamento.OLTRE.toString())) {
//				versamento.setTipoSoglia(TipoSogliaVersamento.OLTRE);
//				String gg = vo.getVrsCodRata().substring(vo.getVrsCodRata().indexOf(TipoSogliaVersamento.OLTRE.toString())+ TipoSogliaVersamento.OLTRE.toString().length());
//				versamento.setGiorniSoglia(Integer.parseInt(gg));
//			} else {
//				versamento.setNumeroRata(Integer.parseInt(vo.getVrsCodRata()));
//			}
//		}
		
//		if(vo.getVrsTipo() != null)
//			versamento.setTipo(TipologiaTipoVersamento.toEnum(vo.getVrsTipo()));

		dto.setVersamento(versamento );

		return dto;
	}



	public static List<Pagamento> toDTO(
			List<it.govpay.orm.VistaPagamento> pagamentoVOLst) throws ServiceException {
		List<Pagamento> dto = new ArrayList<>();
		for(it.govpay.orm.VistaPagamento vo : pagamentoVOLst) {
			dto.add(toDTO(vo));
		}
		return dto;
	}
}
