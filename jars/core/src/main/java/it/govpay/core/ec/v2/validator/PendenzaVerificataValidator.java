/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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
package it.govpay.core.ec.v2.validator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.ec.v2.beans.NuovaPendenza;
import it.govpay.ec.v2.beans.NuovaVocePendenza;
import it.govpay.ec.v2.beans.NuovoAllegatoPendenza;
import it.govpay.ec.v2.beans.NuovoDocumento;
import it.govpay.ec.v2.beans.PendenzaVerificata;
import it.govpay.ec.v2.beans.Soggetto;
import it.govpay.ec.v2.beans.TipoSogliaVincoloPagamento;
import it.govpay.model.Versamento.TipoSogliaVersamento;
import it.govpay.model.exception.CodificaInesistenteException;

public class PendenzaVerificataValidator  implements IValidable{

	private PendenzaVerificata pendenzaVerificata = null;
	private NuovaPendenza pendenza= null;
	private ValidatorFactory vf = null; 
	private ValidatoreIdentificativi validatoreId = null;

	public PendenzaVerificataValidator(PendenzaVerificata pendenzaVerificata){
		this.pendenzaVerificata = pendenzaVerificata;
		this.pendenza = this.pendenzaVerificata.getPendenza();
		this.vf = ValidatorFactory.newInstance();
		this.validatoreId = ValidatoreIdentificativi.newInstance();
	}

	@Override
	public void validate() throws ValidationException {
		if(this.pendenza != null) {

			validaIdPendenza(this.pendenza.getIdPendenza());
			validaIdA2A(this.pendenza.getIdA2A());
			validaIdDominio(this.pendenza.getIdDominio());
			validaIdUnitaOperativa(this.pendenza.getIdUnitaOperativa());
			validaIdTipoPendenza(this.pendenza.getIdTipoPendenza());
			validaCausale( this.pendenza.getCausale());
			
			Soggetto soggetto = this.pendenza.getSoggettoPagatore();
			// Il vincolo di obbligatorieta' del soggetto pagatore e' stato eliminato per consentire di acquisire pendenze senza indicare il debitore.
			if(soggetto != null) {
				SoggettoPagatoreValidator soggettoPagatoreValidator = SoggettoPagatoreValidator.newInstance();
				
				soggettoPagatoreValidator.validaTipo("tipo", soggetto.getTipo());
				soggettoPagatoreValidator.validaIdentificativoNonObbligatorio("identificativo", soggetto.getIdentificativo());
				soggettoPagatoreValidator.validaAnagraficaNonObbligatoria("anagrafica", soggetto.getAnagrafica());
				soggettoPagatoreValidator.validaIndirizzo("indirizzo", soggetto.getIndirizzo());
				soggettoPagatoreValidator.validaCivico("civico", soggetto.getCivico());
				soggettoPagatoreValidator.validaCap("cap", soggetto.getCap());
				soggettoPagatoreValidator.validaLocalita("localita", soggetto.getLocalita());
				soggettoPagatoreValidator.validaProvincia("provincia", soggetto.getProvincia());
				soggettoPagatoreValidator.validaNazione("nazione", soggetto.getNazione());
				soggettoPagatoreValidator.validaEmail("email", soggetto.getEmail());
				soggettoPagatoreValidator.validaCellulare("cellulare", soggetto.getCellulare());
			}
			
			validaImporto(this.pendenza.getImporto());
			validaNumeroAvviso(this.pendenza.getNumeroAvviso());
			validaDataValidita(this.pendenza.getDataValidita()); 
			validaDataScadenza(this.pendenza.getDataScadenza());
			validaAnnoRiferimento(this.pendenza.getAnnoRiferimento());
			validaCartellaPagamento(this.pendenza.getCartellaPagamento());
			validaDocumento(this.pendenza.getDocumento());

			if(this.pendenza.getVoci() == null || this.pendenza.getVoci().isEmpty())
				throw new ValidationException("Il campo voci non deve essere vuoto.");

			if(this.pendenza.getVoci().size() < 1)
				throw new ValidationException("Il campo voci deve avere almeno 1 elemento.");

			if(this.pendenza.getVoci().size() > 5)
				throw new ValidationException("Il campo voci deve avere massimo 5 elemento.");

			for (NuovaVocePendenza vocePendenza : this.pendenza.getVoci()) {
				VocePendenzaValidator vocePendenzaValidator = new VocePendenzaValidator(vocePendenza);
				vocePendenzaValidator.validate();
				vocePendenzaValidator.validazioneSemanticaContabilita(vf, this.pendenza.getIdA2A(), this.pendenza.getIdPendenza());
			}
			
			validaAllegati(this.pendenza.getAllegati());
		}
	}

	public void validaIdA2A(String idA2A) throws ValidationException {
		this.validatoreId.validaIdApplicazione("idA2A", idA2A);
	}

	public void validaIdDominio(String idDominio) throws ValidationException {
		this.validatoreId.validaIdDominio("idDominio", idDominio);
	}

	public void validaIdUnitaOperativa(String idUnitaOperativa) throws ValidationException {
		this.validatoreId.validaIdUO("idUnitaOperativa", idUnitaOperativa, false);
	}
	
	public void validaIdTipoPendenza(String idTipoPendenza) throws ValidationException {
		this.validatoreId.validaIdTipoVersamento("idTipoPendenza", idTipoPendenza, false);
	}

	public void validaNomePendenza(String nome) throws ValidationException {
		ValidatoreUtils.validaNomePendenza(vf, "nome", nome);
	}

	public void validaCausale(String causale) throws ValidationException {
		ValidatoreUtils.validaCausale(vf, "causale", causale);
	}

	public void validaImporto(BigDecimal importo) throws ValidationException {
		ValidatoreUtils.validaImporto(vf, "importo", importo);
	}

	public void validaNumeroAvviso(String numeroAvviso) throws ValidationException {
		ValidatoreUtils.validaNumeroAvviso(vf, "numeroAvviso", numeroAvviso);
	}

	public void validaDataValidita(Date date) throws ValidationException {
		ValidatoreUtils.validaData(vf, "dataValidita", date);
	}

	public void validaDataScadenza(Date date) throws ValidationException {
		ValidatoreUtils.validaData(vf, "dataScadenza", date);
	}

	public void validaAnnoRiferimento(BigDecimal annoRiferimento) throws ValidationException {
		ValidatoreUtils.validaAnnoRiferimento(vf, "annoRiferimento", annoRiferimento);
	}

	public void validaCartellaPagamento(String cartellaPagamento) throws ValidationException {
		ValidatoreUtils.validaCartellaPagamento(vf, "cartellaPagamento", cartellaPagamento);
	}

	public void validaIdPendenza(String idPendenza) throws ValidationException {
		this.validatoreId.validaIdPendenza("idPendenza", idPendenza);
	}
	
	public void validaDocumento(NuovoDocumento nuovoDocumento) throws ValidationException { 
		if(nuovoDocumento != null) {
			this.validatoreId.validaIdDocumento("identificativo", nuovoDocumento.getIdentificativo());
			this.vf.getValidator("descrizione", nuovoDocumento.getDescrizione()).notNull().minLength(1).maxLength(255);
			if(nuovoDocumento.getRata() != null) {
				ValidatoreUtils.validaRata(vf, "rata", nuovoDocumento.getRata());
			} else if(nuovoDocumento.getSoglia() != null) {
				ValidatoreUtils.validaSogliaTipo(vf, "tipo", nuovoDocumento.getSoglia().getTipo());

				try {
					TipoSogliaVersamento tipoSoglia = TipoSogliaVersamento.toEnum(nuovoDocumento.getSoglia().getTipo());

					switch(tipoSoglia) {
					case ENTRO:
					case OLTRE:
						ValidatoreUtils.validaSogliaGiorni(vf, "giorni", nuovoDocumento.getSoglia().getGiorni());
						break;
					case RIDOTTO:
					case SCONTATO:
						try {
						this.vf.getValidator("giorni", nuovoDocumento.getSoglia().getGiorni()).isNull();
						} catch (Exception e) {
							throw new ValidationException("Il campo giorni deve essere vuoto quando il campo tipo assume valore 'RIDOTTO' o 'SCONTATO'.");
						}
						break;
					}
				}catch (CodificaInesistenteException e) {
					throw new ValidationException(e);
				}
			}
		}
	}
	
	public static void validaSogliaTipo(ValidatorFactory vf, String nomeCampo, String tipo) throws ValidationException {
		vf.getValidator(nomeCampo, tipo).notNull();
		
		TipoSogliaVincoloPagamento pCheck = null;
		for(TipoSogliaVincoloPagamento p : TipoSogliaVincoloPagamento.values()){
			if(p.toString().equals(tipo)) {
				pCheck = p;
				break;
			}
		}
		if(pCheck == null)
			throw new ValidationException("Codifica inesistente per '"+nomeCampo+"'. Valore fornito [" + tipo + "] valori possibili " + ArrayUtils.toString(TipoSogliaVincoloPagamento.values()));
	}
	
	public void validaAllegati(List<NuovoAllegatoPendenza> allegati) throws ValidationException {
		if(allegati != null && allegati.size() >0 ) {
			for(NuovoAllegatoPendenza allegato: allegati) {
				this.vf.getValidator("nome", allegato.getNome()).notNull().minLength(1).maxLength(255);
				this.vf.getValidator("tipo", allegato.getTipo()).minLength(1).maxLength(255);
				this.vf.getValidator("descrizione", allegato.getDescrizione()).minLength(1).maxLength(255);
				
				if(allegato.getContenuto() == null)
					throw new ValidationException("Il campo " + "contenuto" + " non deve essere vuoto.");
			}
		}
	}
}
