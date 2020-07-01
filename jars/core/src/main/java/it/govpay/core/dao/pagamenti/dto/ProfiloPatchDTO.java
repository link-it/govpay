/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import org.springframework.security.core.Authentication;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 28 giu 2018 $
 * 
 */
public class ProfiloPatchDTO extends AbstractPatchDTO {

	public ProfiloPatchDTO(Authentication user) {
		super(user);
	}

}
