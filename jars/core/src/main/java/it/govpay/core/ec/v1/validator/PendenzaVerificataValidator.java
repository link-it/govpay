package it.govpay.core.ec.v1.validator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;
import it.govpay.ec.v1.beans.Documento;
import it.govpay.ec.v1.beans.NuovoAllegatoPendenza;
import it.govpay.ec.v1.beans.PendenzaVerificata;
import it.govpay.ec.v1.beans.Soggetto;
import it.govpay.ec.v1.beans.VocePendenza;
import it.govpay.model.Versamento.TipoSogliaVersamento;

public class PendenzaVerificataValidator  implements IValidable{

	private PendenzaVerificata pendenzaVerificata= null;
	private ValidatorFactory vf = null; 
	private ValidatoreIdentificativi validatoreId = null;

	public PendenzaVerificataValidator(PendenzaVerificata pendenzaVerificata){
		this.pendenzaVerificata = pendenzaVerificata;
		this.vf = ValidatorFactory.newInstance();
		this.validatoreId = ValidatoreIdentificativi.newInstance();
	}

	@Override
	public void validate() throws ValidationException {
		if(this.pendenzaVerificata != null) {

			validaIdPendenza(this.pendenzaVerificata.getIdPendenza());
			validaIdA2A(this.pendenzaVerificata.getIdA2A());
			validaIdDominio(this.pendenzaVerificata.getIdDominio());
			validaIdUnitaOperativa(this.pendenzaVerificata.getIdUnitaOperativa());
			validaIdTipoPendenza(this.pendenzaVerificata.getIdTipoPendenza());
			validaNomePendenza(this.pendenzaVerificata.getNome());
			validaCausale( this.pendenzaVerificata.getCausale());

			Soggetto soggetto = this.pendenzaVerificata.getSoggettoPagatore();
			if(soggetto == null)
				throw new ValidationException("Il campo soggettoPagatore non deve essere vuoto.");

			SoggettoPagatoreValidator soggettoPagatoreValidator = SoggettoPagatoreValidator.newInstance();

			soggettoPagatoreValidator.validaTipo("tipo", soggetto.getTipo() != null ? soggetto.getTipo().toString() : null);
			soggettoPagatoreValidator.validaIdentificativo("identificativo", soggetto.getIdentificativo());
			soggettoPagatoreValidator.validaAnagrafica("anagrafica", soggetto.getAnagrafica());
			soggettoPagatoreValidator.validaIndirizzo("indirizzo", soggetto.getIndirizzo());
			soggettoPagatoreValidator.validaCivico("civico", soggetto.getCivico());
			soggettoPagatoreValidator.validaCap("cap", soggetto.getCap());
			soggettoPagatoreValidator.validaLocalita("localita", soggetto.getLocalita());
			soggettoPagatoreValidator.validaProvincia("provincia", soggetto.getProvincia());
			soggettoPagatoreValidator.validaNazione("nazione", soggetto.getNazione());
			soggettoPagatoreValidator.validaEmail("email", soggetto.getEmail());
			soggettoPagatoreValidator.validaCellulare("cellulare", soggetto.getCellulare());

			validaImporto(this.pendenzaVerificata.getImporto());
			validaNumeroAvviso(this.pendenzaVerificata.getNumeroAvviso());
			validaDataValidita(this.pendenzaVerificata.getDataValidita()); 
			validaDataScadenza(this.pendenzaVerificata.getDataScadenza());
			validaAnnoRiferimento(this.pendenzaVerificata.getAnnoRiferimento());
			validaCartellaPagamento(this.pendenzaVerificata.getCartellaPagamento());
			validaDocumento(this.pendenzaVerificata.getDocumento());

			if(this.pendenzaVerificata.getVoci() == null || this.pendenzaVerificata.getVoci().isEmpty())
				throw new ValidationException("Il campo voci non deve essere vuoto.");

			if(this.pendenzaVerificata.getVoci().size() < 1)
				throw new ValidationException("Il campo voci deve avere almeno 1 elemento.");

			if(this.pendenzaVerificata.getVoci().size() > 5)
				throw new ValidationException("Il campo voci deve avere massimo 5 elemento.");

			for (VocePendenza vocePendenza : this.pendenzaVerificata.getVoci()) {
				VocePendenzaValidator vocePendenzaValidator = new VocePendenzaValidator(vocePendenza);
				vocePendenzaValidator.validate();
				vocePendenzaValidator.validazioneSemanticaContabilita(vf, this.pendenzaVerificata.getIdA2A(), this.pendenzaVerificata.getIdPendenza());
			}

			validaAllegati(this.pendenzaVerificata.getAllegati());
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

	public void validaDocumento(Documento documento) throws ValidationException {
		if(documento != null) {
			this.validatoreId.validaIdDocumento("identificativo", documento.getIdentificativo());
			this.vf.getValidator("descrizione", documento.getDescrizione()).notNull().minLength(1).maxLength(255);
			if(documento.getRata() != null) {
				ValidatoreUtils.validaRata(vf, "rata", documento.getRata());
			} else if(documento.getSoglia() != null) {
				ValidatoreUtils.validaSogliaTipo(vf, "tipo", documento.getSoglia().getTipo());

				try {
					TipoSogliaVersamento tipoSoglia = TipoSogliaVersamento.toEnum(documento.getSoglia().getTipo());

					switch(tipoSoglia) {
					case ENTRO:
					case OLTRE:
						ValidatoreUtils.validaSogliaGiorni(vf, "giorni", documento.getSoglia().getGiorni());
						break;
					case RIDOTTO:
					case SCONTATO:
						try {
						this.vf.getValidator("giorni", documento.getSoglia().getGiorni()).isNull();
						} catch (Exception e) {
							throw new ValidationException("Il campo giorni deve essere vuoto quando il campo tipo assume valore 'RIDOTTO' o 'SCONTATO'.");
						}
						break;
					}
				}catch (ServiceException e) {
					throw new ValidationException(e);
				}
			}
		}
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
