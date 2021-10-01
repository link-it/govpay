package it.govpay.core.utils.validator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.apache.commons.jcs.access.exception.InvalidArgumentException;
import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;

import it.govpay.core.dao.commons.Versamento.SingoloVersamento.Tributo.TipoContabilita;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.SingoloVersamento.TipoBollo;
import it.govpay.model.Versamento.TipoSogliaVersamento;

public class ValidatoreUtils {
	
	public static void validaCF(ValidatorFactory vf, String nomeCampo, String codiceFiscale) throws ValidationException {
		validaField(vf, nomeCampo, codiceFiscale, CostantiValidazione.PATTERN_CF, null, null, true);
	}
	
	public static void validaSeveritaA(ValidatorFactory vf, String nomeCampo, Integer valoreCampo) throws ValidationException {
		vf.getValidator(nomeCampo, BigInteger.valueOf(valoreCampo)).min(BigInteger.ZERO);
	}
	
	public static void validaSeveritaDa(ValidatorFactory vf, String nomeCampo, Integer valoreCampo) throws ValidationException {
		vf.getValidator(nomeCampo, BigInteger.valueOf(valoreCampo)).min(BigInteger.ZERO);
	}
	
	public static void validaRisultatiPerPagina(ValidatorFactory vf, String nomeCampo, Integer valoreCampo) throws ValidationException {
		if(valoreCampo != null) {
			vf.getValidator(nomeCampo, BigInteger.valueOf(valoreCampo)).min(BigInteger.ZERO).max(BigInteger.valueOf(GovpayConfig.getInstance().getDimensioneMassimaListaRisultati()));
		}
	}
	
	public static void validaRisultatiPerPagina(ValidatorFactory vf, String nomeCampo, BigInteger valoreCampo) throws ValidationException {
		vf.getValidator(nomeCampo, valoreCampo).min(BigInteger.ZERO).maxOrEquals(BigInteger.valueOf(GovpayConfig.getInstance().getDimensioneMassimaListaRisultati()));
	}
	
	public static void checkIsNotNull(ValidatorFactory vf, String nomeCampo, String valoreCampo) throws ValidationException {
		vf.getValidator(nomeCampo, valoreCampo).notNull();
	}
	
	public static void checkIsNull(ValidatorFactory vf, String nomeCampo, String valoreCampo) throws ValidationException {
		vf.getValidator(nomeCampo, valoreCampo).isNull();
	}
	
	public static void validaTassonomia(ValidatorFactory vf, String nomeCampo, String tassonomia) throws ValidationException {
		vf.getValidator(nomeCampo, tassonomia).minLength(1).maxLength(35);
	}
	
	public static void validaCartellaPagamento(ValidatorFactory vf, String nomeCampo, String cartellaPagamento) throws ValidationException {
		vf.getValidator(nomeCampo, cartellaPagamento).minLength(1).maxLength(35);
	}
	
	public static void validaNomePendenza(ValidatorFactory vf, String nomeCampo, String nome) throws ValidationException {
		vf.getValidator(nomeCampo, nome).minLength(1).maxLength(35);
	}

	public static void validaCausale(ValidatorFactory vf, String nomeCampo, String causale) throws ValidationException {
		vf.getValidator(nomeCampo, causale).notNull().minLength(1).maxLength(140);
	}

	public static void validaImporto(ValidatorFactory vf, String nomeCampo, BigDecimal importo) throws ValidationException {
		validaImporto(vf, nomeCampo, importo, true);
	}
	
	public static void validaImportoOpzionale(ValidatorFactory vf, String nomeCampo, BigDecimal importo) throws ValidationException {
		validaImporto(vf, nomeCampo, importo, false);
	}
	
	private static void validaImporto(ValidatorFactory vf, String nomeCampo, BigDecimal importo, boolean notnull) throws ValidationException {
		
		
		BigDecimalValidator bigDecimalValidator = vf.getValidator(nomeCampo, importo);
		
		if(notnull)
			bigDecimalValidator.notNull();
		
		bigDecimalValidator.minOrEquals(BigDecimal.ZERO).totalDigits(18).checkDecimalDigits();
	}

	
	public static void validaData(ValidatorFactory vf, String nomeCampo, Date date) throws ValidationException {
		vf.getValidator(nomeCampo, date).isValid();
	}
	
	public static void validaNumeroAvviso(ValidatorFactory vf, String nomeCampo, String numeroAvviso) throws ValidationException {
		vf.getValidator(nomeCampo, numeroAvviso).maxLength(18).pattern(CostantiValidazione.PATTERN_NUMERO_AVVISO);
		IuvUtils.toIuv(numeroAvviso);
	}
	
	public static void validaAnnoRiferimento(ValidatorFactory vf, String nomeCampo, BigDecimal annoRiferimento) throws ValidationException {
		if(annoRiferimento != null)
			vf.getValidator(nomeCampo, annoRiferimento.toBigInteger().toString()).pattern(CostantiValidazione.PATTERN_ANNO_RIFERIMENTO);
	}

	public static void validaCodiceContabilita(ValidatorFactory vf, String nomeCampo, String codiceContabilita) throws ValidationException {
		validaCodiceContabilita(vf, nomeCampo, codiceContabilita, true);
	}

	public static void validaCodiceContabilita(ValidatorFactory vf, String nomeCampo, String codiceContabilita, boolean notNull) throws ValidationException {
		validaField(vf, nomeCampo, codiceContabilita, CostantiValidazione.PATTERN_COD_CONTABILITA, null, 255, notNull);
	}
	
	public static void validaDescrizione(ValidatorFactory vf, String nomeCampo, String descrizione) throws ValidationException {
		vf.getValidator(nomeCampo, descrizione).notNull().minLength(1).maxLength(255);
	}
	
	public static void validaDescrizioneCausaleRPT(ValidatorFactory vf, String nomeCampo, String descrizioneCausaleRPT) throws ValidationException {
		vf.getValidator(nomeCampo, descrizioneCausaleRPT).minLength(1).maxLength(140);
	}

	public static void validaTipoContabilita(ValidatorFactory vf, String nomeCampo, String tipoContabilita) throws ValidationException {
		vf.getValidator(nomeCampo, tipoContabilita).notNull();
		
		try {
			TipoContabilita.valueOf(tipoContabilita);
		} catch(InvalidArgumentException e) {
			throw new ValidationException("Codifica inesistente per tipoContabilita. Valore fornito [" + tipoContabilita + "] valori possibili " + ArrayUtils.toString(TipoContabilita.values()));
		}
		
	}
	
	public static void validaTipoContabilita(ValidatorFactory vf, String nomeCampo, Enum<?> enumValue) throws ValidationException {
		vf.getValidator(nomeCampo, enumValue).notNull();
		
		try {
			TipoContabilita.valueOf(enumValue.toString());
		} catch(InvalidArgumentException e) {
			throw new ValidationException("Codifica inesistente per tipoContabilita. Valore fornito [" + enumValue + "] valori possibili " + ArrayUtils.toString(TipoContabilita.values()));
		}
	}
	
	public static void validaTipoBollo(ValidatorFactory vf, String nomeCampo, String tipoBollo) throws ValidationException {
		vf.getValidator(nomeCampo, tipoBollo).notNull();
		
		try {
			TipoBollo.toEnum(tipoBollo);
		} catch(ServiceException e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	public static void validaTipoBollo(ValidatorFactory vf, String nomeCampo, Enum<?> enumValue) throws ValidationException {
		vf.getValidator(nomeCampo, enumValue).notNull();
		
		try {
			TipoBollo.toEnum(enumValue.toString());
		} catch(ServiceException e) {
			throw new ValidationException(e.getMessage());
		}
	}
	
	public static void validaHashDocumento(ValidatorFactory vf, String nomeCampo, String hashDocumento) throws ValidationException {
		vf.getValidator(nomeCampo, hashDocumento).notNull().minLength(1).maxLength(70);
	}
	
	public static void validaProvinciaResidenza(ValidatorFactory vf, String nomeCampo, String provinciaResidenza) throws ValidationException {
		vf.getValidator(nomeCampo, provinciaResidenza).notNull().pattern(CostantiValidazione.PATTERN_PROVINCIA);
	}
	
	public static void validaRata(ValidatorFactory vf, String nomeCampo, BigDecimal rata) throws ValidationException {
		vf.getValidator(nomeCampo, rata).min(BigDecimal.ONE);
	}
	
	public static void validaSogliaGiorni(ValidatorFactory vf, String nomeCampo, BigDecimal giorni) throws ValidationException {
		vf.getValidator(nomeCampo, giorni).notNull().minOrEquals(BigDecimal.ONE);
	}
	
	public static void validaSogliaTipo(ValidatorFactory vf, String nomeCampo, String tipo) throws ValidationException {
		vf.getValidator(nomeCampo, tipo).notNull();
		
		TipoSogliaVersamento pCheck = null;
		for(TipoSogliaVersamento p : TipoSogliaVersamento.values()){
			if(p.getCodifica().equals(tipo)) {
				pCheck = p;
				break;
			}
		}
		if(pCheck == null)
			throw new ValidationException("Codifica inesistente per '"+nomeCampo+"'. Valore fornito [" + tipo + "] valori possibili " + ArrayUtils.toString(TipoSogliaVersamento.values()));
	}
	
	public static StringValidator validaField(ValidatorFactory vf, String fieldName, String fieldValue, String pattern, Integer minLength, Integer maxLength, boolean notNull) throws ValidationException {
		StringValidator validator = vf.getValidator(fieldName, fieldValue);
		
		if(notNull)
			validator.notNull();
		
		if(minLength != null)
			validator.minLength(minLength);
		
		if(maxLength != null)
			validator.maxLength(maxLength);
		
		if(pattern != null)
			validator.pattern(pattern);
		
		return validator;
	}
}
