package it.govpay.core.ec.v1.validator.legacy;

import java.math.BigDecimal;
import java.util.Date;

import org.openspcoop2.utils.json.ValidationException;

import it.govpay.core.dao.commons.Anagrafica;
import it.govpay.core.dao.commons.Versamento;
import it.govpay.core.dao.commons.Versamento.SingoloVersamento;
import it.govpay.core.ec.v1.validator.SoggettoPagatoreValidator;
import it.govpay.core.utils.validator.CostantiValidazione;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.core.utils.validator.ValidatoreUtils;

public class VersamentoValidator  implements IValidable{

	private Versamento pendenzaVerificata= null;
	private ValidatorFactory vf = null; 
	private ValidatoreIdentificativi validatoreId = null;

	public VersamentoValidator(Versamento pendenzaVerificata){
		this.pendenzaVerificata = pendenzaVerificata;
		this.vf = ValidatorFactory.newInstance();
		this.validatoreId = ValidatoreIdentificativi.newInstance();
	}

	@Override
	public void validate() throws ValidationException {
		if(this.pendenzaVerificata != null) {

			validaIdPendenza(this.pendenzaVerificata.getCodVersamentoEnte());
			validaIdA2A(this.pendenzaVerificata.getCodApplicazione());
			validaIdDominio(this.pendenzaVerificata.getCodDominio());
			validaIdUnitaOperativa(this.pendenzaVerificata.getCodUnitaOperativa());
//			validaIdTipoPendenza(this.pendenzaVerificata.getIdTipoPendenza());
			validaNomePendenza(this.pendenzaVerificata.getNome());
			validaCausale( this.pendenzaVerificata.getCausale());
			validaIuv(this.pendenzaVerificata.getIuv());
			
			Anagrafica soggetto = this.pendenzaVerificata.getDebitore();
			if(soggetto == null)
				throw new ValidationException("Il campo soggettoPagatore non deve essere vuoto.");
			
			SoggettoPagatoreValidator soggettoPagatoreValidator = SoggettoPagatoreValidator.newInstance();
			
			soggettoPagatoreValidator.validaTipo("tipo", soggetto.getTipo() != null ? soggetto.getTipo().toString() : null);
			soggettoPagatoreValidator.validaIdentificativo("codUnivoco", soggetto.getCodUnivoco());
			soggettoPagatoreValidator.validaAnagrafica("ragionesociale", soggetto.getRagioneSociale());
			soggettoPagatoreValidator.validaIndirizzo("indirizzo", soggetto.getIndirizzo());
			soggettoPagatoreValidator.validaCivico("civico", soggetto.getCivico());
			soggettoPagatoreValidator.validaCap("cap", soggetto.getCap());
			soggettoPagatoreValidator.validaLocalita("localita", soggetto.getLocalita());
			soggettoPagatoreValidator.validaProvincia("provincia", soggetto.getProvincia());
			soggettoPagatoreValidator.validaNazione("nazione", soggetto.getNazione());
			soggettoPagatoreValidator.validaEmail("email", soggetto.getEmail());
			soggettoPagatoreValidator.validaCellulare("cellulare", soggetto.getCellulare());
			
			validaImporto(this.pendenzaVerificata.getImportoTotale());
			validaNumeroAvviso(this.pendenzaVerificata.getNumeroAvviso());
			validaDataValidita(this.pendenzaVerificata.getDataValidita()); 
			validaDataScadenza(this.pendenzaVerificata.getDataScadenza());
			validaAnnoRiferimento(this.pendenzaVerificata.getAnnoTributario());
			validaCartellaPagamento(this.pendenzaVerificata.getCartellaPagamento());

			if(this.pendenzaVerificata.getSingoloVersamento() == null || this.pendenzaVerificata.getSingoloVersamento().isEmpty())
				throw new ValidationException("Il campo singoloVersamento non deve essere vuoto.");

			if(this.pendenzaVerificata.getSingoloVersamento().size() < 1)
				throw new ValidationException("Il campo singoloVersamento deve avere almeno 1 elemento.");

			if(this.pendenzaVerificata.getSingoloVersamento().size() > 5)
				throw new ValidationException("Il campo singoloVersamento deve avere massimo 5 elemento.");

			for (SingoloVersamento vocePendenza : this.pendenzaVerificata.getSingoloVersamento()) {
				SingoloVersamentoValidator vocePendenzaValidator = new SingoloVersamentoValidator(vocePendenza);
				vocePendenzaValidator.validate();
			}
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
	
	public void validaIuv(String iuv) throws ValidationException {
		this.vf.getValidator("iuv", iuv).minLength(1).maxLength(17).pattern(CostantiValidazione.PATTERN_NUMERO_IUV); 
	}

	public void validaDataValidita(Date date) throws ValidationException {
		ValidatoreUtils.validaData(vf, "dataValidita", date);
	}

	public void validaDataScadenza(Date date) throws ValidationException {
		ValidatoreUtils.validaData(vf, "dataScadenza", date);
	}

	public void validaAnnoRiferimento(Integer annoRiferimento) throws ValidationException {
		ValidatoreUtils.validaAnnoRiferimento(vf, "annoRiferimento", annoRiferimento);
	}

	public void validaCartellaPagamento(String cartellaPagamento) throws ValidationException {
		ValidatoreUtils.validaCartellaPagamento(vf, "cartellaPagamento", cartellaPagamento);
	}

	public void validaIdPendenza(String idPendenza) throws ValidationException {
		this.validatoreId.validaIdPendenza("idPendenza", idPendenza);
	}
}
