/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
import java.util.List;

import it.govpay.core.beans.EsitoOperazione;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.ec.v2.beans.MapEntry;
import it.govpay.ec.v2.beans.NuovaVocePendenza;
import it.govpay.ec.v2.beans.QuotaContabilita;

public class VocePendenzaValidator implements IValidable{

	private NuovaVocePendenza vocePendenza = null;


	public VocePendenzaValidator(NuovaVocePendenza vocePendenza2) {
		this.vocePendenza = vocePendenza2;
	}

	@Override
	public void validate() throws ValidationException {
		if(this.vocePendenza != null) {

			ValidatorFactory vf = ValidatorFactory.newInstance();
			ValidatoreIdentificativi vi = ValidatoreIdentificativi.newInstance();

			vi.validaIdVocePendenza("idVocePendenza", this.vocePendenza.getIdVocePendenza());
			ValidatoreUtils.validaImporto(vf, "importo", this.vocePendenza.getImporto());
			ValidatoreUtils.validaDescrizione(vf, "descrizione", this.vocePendenza.getDescrizione());
			ValidatoreUtils.validaDescrizioneCausaleRPT(vf, "descrizioneCausaleRPT", this.vocePendenza.getDescrizioneCausaleRPT());
			this.validaContabilita(vf);
			this.validaMetadata(vf);
			if(this.vocePendenza.getIdDominio() != null)
				vi.validaIdDominio("idDominio", this.vocePendenza.getIdDominio());

			if(this.vocePendenza.getCodEntrata() != null) {
				vi.validaIdEntrata("codEntrata", this.vocePendenza.getCodEntrata());
				try {
					vf.getValidator("tipoBollo", this.vocePendenza.getTipoBollo()).isNull();
					vf.getValidator("hashDocumento", this.vocePendenza.getHashDocumento()).isNull();
					vf.getValidator("provinciaResidenza", this.vocePendenza.getProvinciaResidenza()).isNull();
					vf.getValidator("ibanAccredito", this.vocePendenza.getIbanAccredito()).isNull();
					vf.getValidator("ibanAppoggio", this.vocePendenza.getIbanAppoggio()).isNull();
					vf.getValidator("codiceTassonomicoPagoPA", this.vocePendenza.getCodiceTassonomicoPagoPA()).isNull();
				} catch (ValidationException ve) {
					throw new ValidationException("Valorizzato codEntrata. " + ve.getMessage());
				}

				return;
			}

			else if(this.vocePendenza.getTipoBollo() != null) {
				ValidatoreUtils.validaTipoBollo(vf, "tipoBollo", this.vocePendenza.getTipoBollo());
				ValidatoreUtils.validaHashDocumento(vf, "hashDocumento", this.vocePendenza.getHashDocumento());
				ValidatoreUtils.validaProvinciaResidenza(vf, "provinciaResidenza", this.vocePendenza.getProvinciaResidenza());
				ValidatoreUtils.validaCodiceTassonomicoPagoPA(vf, "codiceTassonomicoPagoPA", this.vocePendenza.getCodiceTassonomicoPagoPA());
			
				try {
					vf.getValidator("ibanAccredito", this.vocePendenza.getIbanAccredito()).isNull();
					vf.getValidator("ibanAppoggio", this.vocePendenza.getIbanAppoggio()).isNull();
				} catch (ValidationException ve) {
					throw new ValidationException("Valorizzato tipoBollo. " + ve.getMessage());
				}

				return;
			}


			else if(this.vocePendenza.getIbanAccredito() != null) {
				vi.validaIdIbanAccredito("ibanAccredito", this.vocePendenza.getIbanAccredito(), true);
				vi.validaIdIbanAccredito("ibanAppoggio", this.vocePendenza.getIbanAppoggio());
				ValidatoreUtils.validaCodiceTassonomicoPagoPA(vf, "codiceTassonomicoPagoPA", this.vocePendenza.getCodiceTassonomicoPagoPA());

				try {
					vf.getValidator("hashDocumento", this.vocePendenza.getHashDocumento()).isNull();
					vf.getValidator("provinciaResidenza", this.vocePendenza.getProvinciaResidenza()).isNull();
				} catch (ValidationException ve) {
					throw new ValidationException("Valorizzato ibanAccredito. " + ve.getMessage());
				}
			}

			else {
				throw new ValidationException("Uno dei campi tra ibanAccredito, tipoBollo o codEntrata deve essere valorizzato");
			}
		}
	}

	private void validaContabilita(ValidatorFactory vf) throws ValidationException {
		if(this.vocePendenza.getContabilita() != null) {
			List<QuotaContabilita> listaContabilita = this.vocePendenza.getContabilita().getQuote();
			if(listaContabilita != null) {
				for (QuotaContabilita contabilita : listaContabilita) {

					vf.getValidator("capitolo", contabilita.getCapitolo()).notNull().minLength(1).maxLength(64);
					
					vf.getValidator("annoEsercizio", contabilita.getAnnoEsercizio()).notNull();
					ValidatoreUtils.validaAnnoRiferimento(vf, "annoEsercizio", contabilita.getAnnoEsercizio());

					vf.getValidator("accertamento", contabilita.getAccertamento()).minLength(1).maxLength(64);
					ValidatoreUtils.validaImporto(vf, "importo", contabilita.getImporto());

					vf.getValidator("titolo", contabilita.getTitolo()).minLength(1).maxLength(64);
					vf.getValidator("tipologia", contabilita.getTipologia()).minLength(1).maxLength(64);
					vf.getValidator("categoria", contabilita.getCategoria()).minLength(1).maxLength(64);
					vf.getValidator("articolo", contabilita.getArticolo()).minLength(1).maxLength(64);
				}
			}
		}
	}

	public void validazioneSemanticaContabilita(ValidatorFactory vf, String idA2A, String idPendenza) throws ValidationException {
		if(this.vocePendenza.getContabilita() != null) {
			List<QuotaContabilita> listaContabilita = this.vocePendenza.getContabilita().getQuote();
			BigDecimal importoVocePendenza = this.vocePendenza.getImporto();
			if(listaContabilita != null) {
				try {
					BigDecimal somma = BigDecimal.ZERO;
					for (QuotaContabilita voceContabilita : listaContabilita) {
						somma = somma.add(voceContabilita.getImporto());
					}
	
					if(somma.compareTo(vocePendenza.getImporto()) != 0) {
						throw new GovPayException(EsitoOperazione.VER_035, vocePendenza.getIdVocePendenza(), idA2A, idPendenza,
								Double.toString(importoVocePendenza.doubleValue()), Double.toString(somma.doubleValue()));
					}
				}catch (GovPayException e) {
					throw new ValidationException(e.getMessage(), e);
				}
			}
		}
	}
	
	private void validaMetadata(ValidatorFactory vf) throws ValidationException {
		if(this.vocePendenza.getMetadata() != null) {
			if(this.vocePendenza.getMetadata().getMapEntries() == null || this.vocePendenza.getMetadata().getMapEntries().isEmpty())
				throw new ValidationException("Il campo mapEntries non deve essere vuoto.");

			if(this.vocePendenza.getMetadata().getMapEntries().isEmpty())
				throw new ValidationException("Il campo mapEntries deve avere almeno 1 elemento.");

			if(this.vocePendenza.getMetadata().getMapEntries().size() > 15)
				throw new ValidationException("Il campo mapEntries deve avere massimo 15 elemento.");
			
			for (int i = 0; i < this.vocePendenza.getMetadata().getMapEntries().size(); i++) {
				MapEntry entry = this.vocePendenza.getMetadata().getMapEntries().get(i);
				
				vf.getValidator("metadata.mapEntries["+i+"].key", entry.getKey()).notNull().minLength(1).maxLength(140);
				vf.getValidator("metadata.mapEntries["+i+"].value", entry.getValue()).notNull().minLength(1).maxLength(140);
			}
		}
	}
}
