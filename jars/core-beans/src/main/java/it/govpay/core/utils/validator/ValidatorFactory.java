package it.govpay.core.utils.validator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class ValidatorFactory {

	private static final NullPointerException FIELDNAME_NULLPOINTEREXCEPTION = new NullPointerException("Campo fieldName deve essere valorizzato");
	private ValidatorFactory() {
	}
	
	public static ValidatorFactory newInstance() {
		return new ValidatorFactory();
	}
	
	public StringValidator getValidator(String fieldName, String fieldValue) {
		if(fieldName == null) throw FIELDNAME_NULLPOINTEREXCEPTION;
		return new StringValidator(fieldName, fieldValue);
	}
	
	public DateValidator getValidator(String fieldName, Date fieldValue) {
		if(fieldName == null) throw FIELDNAME_NULLPOINTEREXCEPTION;
		return new DateValidator(fieldName, fieldValue);
	}
	
	public EnumValidator getValidator(String fieldName, Enum<?> fieldValue) {
		if(fieldName == null) throw FIELDNAME_NULLPOINTEREXCEPTION;
		return new EnumValidator(fieldName, fieldValue);
	}
	
	public ObjectListValidator getValidator(String fieldName, List<? extends IValidable> fieldValue) {
		if(fieldName == null) throw FIELDNAME_NULLPOINTEREXCEPTION;
		return new ObjectListValidator(fieldName, fieldValue);
	}
	
	public ObjectValidator getValidator(String fieldName, IValidable fieldValue) {
		if(fieldName == null) throw FIELDNAME_NULLPOINTEREXCEPTION;
		return new ObjectValidator(fieldName, fieldValue);
	}
	
	public BigDecimalValidator getValidator(String fieldName, BigDecimal fieldValue) {
		if(fieldName == null) throw FIELDNAME_NULLPOINTEREXCEPTION;
		return new BigDecimalValidator(fieldName, fieldValue);
	}
	
	public BigIntegerValidator getValidator(String fieldName, BigInteger fieldValue) {
		if(fieldName == null) throw FIELDNAME_NULLPOINTEREXCEPTION;
		return new BigIntegerValidator(fieldName, fieldValue);
	}
	
	public DoubleValidator getValidator(String fieldName, Double fieldValue) {
		if(fieldName == null) throw FIELDNAME_NULLPOINTEREXCEPTION;
		return new DoubleValidator(fieldName, fieldValue);
	}
	
	public BooleanValidator getValidator(String fieldName, Boolean fieldValue) {
		if(fieldName == null) throw FIELDNAME_NULLPOINTEREXCEPTION;
		return new BooleanValidator(fieldName, fieldValue);
	}
}
