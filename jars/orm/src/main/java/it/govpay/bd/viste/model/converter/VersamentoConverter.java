package it.govpay.bd.viste.model.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Documento;
import it.govpay.bd.model.Versamento;
import it.govpay.model.Anagrafica;
import it.govpay.model.Anagrafica.TIPO;
import it.govpay.model.Versamento.StatoPagamento;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.Versamento.TipoSogliaVersamento;
import it.govpay.model.Versamento.TipologiaTipoVersamento;

public class VersamentoConverter {
	
	public static List<Versamento> toDTOList(List<it.govpay.orm.VistaVersamento> versamenti) throws ServiceException {
		List<Versamento> lstDTO = new ArrayList<>();
		if(versamenti != null && !versamenti.isEmpty()) {
			for(it.govpay.orm.VistaVersamento versamento: versamenti){
				lstDTO.add(toDTO(versamento));
			}
		}
		return lstDTO;
	}

	public static Versamento toDTO(it.govpay.orm.VistaVersamento vo) throws ServiceException {
		try {
			Versamento dto = new Versamento();
			dto.setId(vo.getId());
			dto.setIdApplicazione(vo.getIdApplicazione().getId());
			
			if(vo.getIdUo() != null)
				dto.setIdUo(vo.getIdUo().getId());
			
			if(vo.getIdDominio() != null)
				dto.setIdDominio(vo.getIdDominio().getId());
			
			if(vo.getIdTipoVersamento() != null)
				dto.setIdTipoVersamento(vo.getIdTipoVersamento().getId());
			
			if(vo.getIdTipoVersamentoDominio() != null)
				dto.setIdTipoVersamentoDominio(vo.getIdTipoVersamentoDominio().getId());
			dto.setNome(vo.getNome());
			dto.setCodVersamentoEnte(vo.getCodVersamentoEnte());
			dto.setStatoVersamento(StatoVersamento.valueOf(vo.getStatoVersamento()));
			dto.setDescrizioneStato(vo.getDescrizioneStato());
			dto.setImportoTotale(BigDecimal.valueOf(vo.getImportoTotale()));
			dto.setAggiornabile(vo.isAggiornabile());
			dto.setDataCreazione(vo.getDataCreazione());
			dto.setDataValidita(vo.getDataValidita());
			dto.setDataScadenza(vo.getDataScadenza());
			dto.setDataUltimoAggiornamento(vo.getDataOraUltimoAggiornamento());
			dto.setCausaleVersamento(vo.getCausaleVersamento());
			Anagrafica debitore = new Anagrafica();
			if(vo.getDebitoreTipo()!=null)
				debitore.setTipo(TIPO.valueOf(vo.getDebitoreTipo()));
			debitore.setRagioneSociale(vo.getDebitoreAnagrafica());
			debitore.setCap(vo.getDebitoreCap());
			debitore.setCellulare(vo.getDebitoreCellulare());
			debitore.setCivico(vo.getDebitoreCivico());
			debitore.setCodUnivoco(vo.getDebitoreIdentificativo());
			debitore.setEmail(vo.getDebitoreEmail());
			debitore.setFax(vo.getDebitoreFax());
			debitore.setIndirizzo(vo.getDebitoreIndirizzo());
			debitore.setLocalita(vo.getDebitoreLocalita());
			debitore.setNazione(vo.getDebitoreNazione());
			debitore.setProvincia(vo.getDebitoreProvincia());
			debitore.setTelefono(vo.getDebitoreTelefono());
			dto.setAnagraficaDebitore(debitore);
			
			if(vo.getCodAnnoTributario() != null && !vo.getCodAnnoTributario().isEmpty())
				dto.setCodAnnoTributario(Integer.parseInt(vo.getCodAnnoTributario()));
			
			dto.setCodLotto(vo.getCodLotto());
			
			dto.setTassonomiaAvviso(vo.getTassonomiaAvviso()); 
			dto.setTassonomia(vo.getTassonomia());
			
			dto.setCodVersamentoLotto(vo.getCodVersamentoLotto()); 
			dto.setCodBundlekey(vo.getCodBundlekey()); 
			dto.setDatiAllegati(vo.getDatiAllegati());
			if(vo.getIncasso() != null) {
				dto.setIncasso(vo.getIncasso().equals(it.govpay.model.Versamento.INCASSO_TRUE) ? true : false);
			}
			dto.setAnomalie(vo.getAnomalie());
			
			dto.setIuvVersamento(vo.getIuvVersamento());
			dto.setNumeroAvviso(vo.getNumeroAvviso());
			
			// se il numero avviso e' impostato lo iuv proposto deve coincidere con quello inserito a partire dall'avviso
			// TODO controllare
			if(dto.getNumeroAvviso() !=  null) {
				dto.setIuvProposto(dto.getIuvVersamento());
			}
			
			dto.setAck(vo.isAck());
			dto.setAnomalo(vo.isAnomalo());
			
			dto.setDirezione(vo.getDirezione());
			dto.setDivisione(vo.getDivisione());
			dto.setIdSessione(vo.getIdSessione());
			
			dto.setDataPagamento(vo.getDataPagamento());
			if(vo.getImportoPagato() != null)
				dto.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
			if(vo.getImportoIncassato() != null)
			dto.setImportoIncassato(BigDecimal.valueOf(vo.getImportoIncassato()));
			if(vo.getStatoPagamento() != null)
				dto.setStatoPagamento(StatoPagamento.valueOf(vo.getStatoPagamento())); 
			dto.setIuvPagamento(vo.getIuvPagamento());
			
			dto.setDataPagamento(vo.getDataPagamento());
			if(vo.getImportoPagato() != null)
				dto.setImportoPagato(BigDecimal.valueOf(vo.getImportoPagato()));
			if(vo.getImportoIncassato() != null)
			dto.setImportoIncassato(BigDecimal.valueOf(vo.getImportoIncassato()));
			if(vo.getStatoPagamento() != null)
				dto.setStatoPagamento(StatoPagamento.valueOf(vo.getStatoPagamento())); 
			dto.setIuvPagamento(vo.getIuvPagamento());
			
			if(vo.getIdDocumento() != null) {
				Documento documento = new Documento();
				documento.setId(vo.getIdDocumento().getId());
				documento.setCodDocumento(vo.getCodDocumento());
				documento.setDescrizione(vo.getDocDescrizione());
				documento.setIdApplicazione(vo.getIdApplicazione().getId());
				dto.setDocumento(documento);
				dto.setIdDocumento(vo.getIdDocumento().getId());
				dto.setCodDocumento(vo.getCodDocumento());
			}
			
			if(vo.getCodRata() != null) {
				if(vo.getCodRata().startsWith(TipoSogliaVersamento.ENTRO.toString())) {
					dto.setTipoSoglia(TipoSogliaVersamento.ENTRO);
					String gg = vo.getCodRata().substring(vo.getCodRata().indexOf(TipoSogliaVersamento.ENTRO.toString())+ TipoSogliaVersamento.ENTRO.toString().length());
					dto.setGiorniSoglia(Integer.parseInt(gg));
				} else if(vo.getCodRata().startsWith(TipoSogliaVersamento.OLTRE.toString())) {
					dto.setTipoSoglia(TipoSogliaVersamento.OLTRE);
					String gg = vo.getCodRata().substring(vo.getCodRata().indexOf(TipoSogliaVersamento.OLTRE.toString())+ TipoSogliaVersamento.OLTRE.toString().length());
					dto.setGiorniSoglia(Integer.parseInt(gg));
				} else if(vo.getCodRata().startsWith(TipoSogliaVersamento.CDSRI.toString())) {
					dto.setTipoSoglia(TipoSogliaVersamento.CDSRI);
					String gg = vo.getCodRata().substring(vo.getCodRata().indexOf(TipoSogliaVersamento.CDSRI.toString())+ TipoSogliaVersamento.CDSRI.toString().length());
					dto.setGiorniSoglia(Integer.parseInt(gg));
				} else if(vo.getCodRata().startsWith(TipoSogliaVersamento.CDSSC.toString())) {
					dto.setTipoSoglia(TipoSogliaVersamento.CDSSC);
					String gg = vo.getCodRata().substring(vo.getCodRata().indexOf(TipoSogliaVersamento.CDSSC.toString())+ TipoSogliaVersamento.CDSSC.toString().length());
					dto.setGiorniSoglia(Integer.parseInt(gg));
				} else {
					dto.setNumeroRata(Integer.parseInt(vo.getCodRata()));
				}
			}
			
			if(vo.getTipo() != null)
				dto.setTipo(TipologiaTipoVersamento.toEnum(vo.getTipo()));
			
			dto.setDataNotificaAvviso(vo.getDataNotificaAvviso());
			dto.setAvvisoNotificato(vo.getAvvisoNotificato());
			dto.setAvvMailDataPromemoriaScadenza(vo.getAvvMailDataPromScadenza()); 
			dto.setAvvMailPromemoriaScadenzaNotificato(vo.getAvvMailPromScadNotificato());
			dto.setAvvAppIODataPromemoriaScadenza(vo.getAvvAppIoDataPromScadenza()); 
			dto.setAvvAppIOPromemoriaScadenzaNotificato(vo.getAvvAppIoPromScadNotificato());
			
			dto.setProprieta(vo.getProprieta());
			
			return dto;
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException(e);
		}
	}

}
