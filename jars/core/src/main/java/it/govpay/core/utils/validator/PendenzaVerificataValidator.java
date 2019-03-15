package it.govpay.core.utils.validator;

import java.math.BigDecimal;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.core.utils.IuvUtils;
import it.govpay.ec.v1.beans.PendenzaVerificata;
import it.govpay.ec.v1.beans.VocePendenza;

public class PendenzaVerificataValidator  implements IValidable{
	
	private PendenzaVerificata pendenzaVerificata= null;
	
	public PendenzaVerificataValidator(PendenzaVerificata pendenzaVerificata){
		this.pendenzaVerificata = pendenzaVerificata;
	}
	
	@Override
	public void validate() throws ValidationException {
		if(this.pendenzaVerificata != null) {
			ValidatorFactory vf = ValidatorFactory.newInstance();
	
			vf.getValidator("idPendenza", this.pendenzaVerificata.getIdPendenza()).notNull().minLength(1).maxLength(35);
			vf.getValidator("idA2A", this.pendenzaVerificata.getIdA2A()).minLength(1).maxLength(35);
			vf.getValidator("idDominio", this.pendenzaVerificata.getIdDominio()).notNull().minLength(1).maxLength(35);
			vf.getValidator("idUnitaOperativa", this.pendenzaVerificata.getIdUnitaOperativa()).minLength(1).maxLength(35);
			vf.getValidator("nome", this.pendenzaVerificata.getNome()).minLength(1).maxLength(35);
			vf.getValidator("causale", this.pendenzaVerificata.getCausale()).notNull().minLength(1).maxLength(140);
			new SoggettoPagatoreValidator(this.pendenzaVerificata.getSoggettoPagatore()).validate();
			vf.getValidator("importo", this.pendenzaVerificata.getImporto()).notNull().minOrEquals(BigDecimal.ZERO).maxOrEquals(BigDecimal.valueOf(999999.99)).checkDecimalDigits();
			vf.getValidator("numeroAvviso", this.pendenzaVerificata.getNumeroAvviso()).maxLength(18).pattern("[0-9]{18}");
			if(StringUtils.isNotEmpty(this.pendenzaVerificata.getNumeroAvviso()))
				IuvUtils.toIuv(this.pendenzaVerificata.getNumeroAvviso());
			
			if(this.pendenzaVerificata.getDataValidita() != null)
				vf.getValidator("dataValidita", this.pendenzaVerificata.getDataValidita().toDate()).isValid();
			if(this.pendenzaVerificata.getDataScadenza() != null)
				vf.getValidator("dataScadenza", this.pendenzaVerificata.getDataScadenza().toDate()).isValid();
			if(this.pendenzaVerificata.getAnnoRiferimento() != null)
				vf.getValidator("annoRiferimento", this.pendenzaVerificata.getAnnoRiferimento().toBigInteger().toString()).pattern("[0-9]{4}");
			vf.getValidator("cartellaPagamento", this.pendenzaVerificata.getCartellaPagamento()).minLength(1).maxLength(35);
			
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
}
