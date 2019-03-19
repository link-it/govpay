package it.govpay.core.utils.validator;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.core.utils.IuvUtils;
import it.govpay.ec.v1.beans.PendenzaVerificata;
import it.govpay.ec.v1.beans.VocePendenza;

public class PendenzaVerificataValidator  implements IValidable{

	private PendenzaVerificata pendenzaVerificata= null;
	private ValidatorFactory vf = null; 

	public PendenzaVerificataValidator(PendenzaVerificata pendenzaVerificata){
		this.pendenzaVerificata = pendenzaVerificata;
		this.vf = ValidatorFactory.newInstance();
	}

	@Override
	public void validate() throws ValidationException {
		if(this.pendenzaVerificata != null) {

			validaIdPendenza(this.pendenzaVerificata.getIdPendenza());
			validaIdA2A(this.pendenzaVerificata.getIdA2A());
			validaIdDominio(this.pendenzaVerificata.getIdDominio());
			validaIdUnitaOperativa(this.pendenzaVerificata.getIdUnitaOperativa());
			validaNomePendenza(this.pendenzaVerificata.getNome());
			validaCausale( this.pendenzaVerificata.getCausale());
			
			if(this.pendenzaVerificata.getSoggettoPagatore() == null)
				throw new ValidationException("Il campo soggettoPagatore non deve essere vuoto.");

			new SoggettoPagatoreValidator(this.pendenzaVerificata.getSoggettoPagatore()).validate();

			validaImporto(this.pendenzaVerificata.getImporto());
			validaNumeroAvviso(this.pendenzaVerificata.getNumeroAvviso());
			validaDataValidita(this.pendenzaVerificata.getDataValidita()); 
			validaDataScadenza(this.pendenzaVerificata.getDataScadenza());
			validaAnnoRiferimento(this.pendenzaVerificata.getAnnoRiferimento());
			validaCartellaPagamento(this.pendenzaVerificata.getCartellaPagamento());

			if(this.pendenzaVerificata.getVoci() == null || this.pendenzaVerificata.getVoci().isEmpty())
				throw new ValidationException("Il campo voci non deve essere vuoto.");

			if(this.pendenzaVerificata.getVoci().size() < 1)
				throw new ValidationException("Il campo voci deve avere almeno 1 elemento.");

			if(this.pendenzaVerificata.getVoci().size() > 5)
				throw new ValidationException("Il campo voci deve avere massimo 5 elemento.");

			for (VocePendenza vocePendenza : this.pendenzaVerificata.getVoci()) {
				new VocePendenzaValidator(vocePendenza).validate();
			}
		}
	}

	public void validaIdA2A(String idA2A) throws ValidationException {
		vf.getValidator("idA2A", idA2A).minLength(1).maxLength(35);
	}

	public void validaIdDominio(String idDominio) throws ValidationException {
		vf.getValidator("idDominio", idDominio).notNull().minLength(1).maxLength(35);
	}

	public void validaIdUnitaOperativa(String idUnitaOperativa) throws ValidationException {
		vf.getValidator("idUnitaOperativa", idUnitaOperativa).minLength(1).maxLength(35);
	}

	public void validaNomePendenza(String nome) throws ValidationException {
		vf.getValidator("nome", nome).minLength(1).maxLength(35);
	}

	public void validaCausale(String causale) throws ValidationException {
		vf.getValidator("causale", causale).notNull().minLength(1).maxLength(140);
	}

	public void validaImporto(BigDecimal importo) throws ValidationException {
		vf.getValidator("importo", importo).notNull().minOrEquals(BigDecimal.ZERO).maxOrEquals(BigDecimal.valueOf(999999.99)).checkDecimalDigits();
	}

	public void validaNumeroAvviso(String numeroAvviso) throws ValidationException {
		vf.getValidator("numeroAvviso", numeroAvviso).maxLength(18).pattern("[0-9]{18}");
		IuvUtils.toIuv(numeroAvviso);
	}

	public void validaDataValidita(Date date) throws ValidationException {
		vf.getValidator("dataValidita", date).isValid();
	}

	public void validaDataScadenza(Date date) throws ValidationException {
		vf.getValidator("dataScadenza", date).isValid();
	}

	public void validaAnnoRiferimento(BigDecimal annoRiferimento) throws ValidationException {
		if(annoRiferimento != null)
			vf.getValidator("annoRiferimento", annoRiferimento.toBigInteger().toString()).pattern("[0-9]{4}");
	}

	public void validaCartellaPagamento(String cartellaPagamento) throws ValidationException {
		vf.getValidator("cartellaPagamento", cartellaPagamento).minLength(1).maxLength(35);
	}

	public void validaIdPendenza(String idPendenza) throws ValidationException {
		vf.getValidator("idPendenza", idPendenza).notNull().minLength(1).maxLength(35);
	}
}
