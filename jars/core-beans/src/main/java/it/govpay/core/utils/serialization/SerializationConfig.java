package it.govpay.core.utils.serialization;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author Pintori Giuliano (pintori@link.it)
 * @author  $Author$
 * @version $Rev$, $Date$
 * 
 */
public class SerializationConfig extends org.openspcoop2.utils.serialization.SerializationConfig{

	private boolean failOnNumbersForEnums = false; // default
	
	public boolean isFailOnNumbersForEnums() {
		return failOnNumbersForEnums;
	}
	public void setFailOnNumbersForEnums(boolean failOnNumbersForEnums) {
		this.failOnNumbersForEnums = failOnNumbersForEnums;
	}
}
