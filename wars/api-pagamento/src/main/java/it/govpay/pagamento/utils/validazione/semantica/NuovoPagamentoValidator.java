package it.govpay.pagamento.utils.validazione.semantica;

import java.time.LocalDate;
import java.util.Date;

import org.openspcoop2.utils.json.ValidationException;

import it.govpay.core.dao.pagamenti.dto.PagamentiPortaleDTO;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.validator.ValidatorFactory;

public class NuovoPagamentoValidator {
	private ValidatorFactory vf;
	public NuovoPagamentoValidator() {
		this.vf = ValidatorFactory.newInstance();
	}

	public void valida(PagamentiPortaleDTO pagamentiPortaleDTO) throws UnprocessableEntityException {
		this.validaDataEsecuzionePagamento(pagamentiPortaleDTO.getDataEsecuzionePagamento());
	}
	
	public void validaDataEsecuzionePagamento(Date dataEsecuzionePagamento) throws UnprocessableEntityException {
		try {
			vf.getValidator("dataEsecuzionePagamento", dataEsecuzionePagamento).after(LocalDate.now()).insideDays(30);
		} catch (ValidationException e) {
			throw new UnprocessableEntityException(e.getMessage());
		}
	}
}
