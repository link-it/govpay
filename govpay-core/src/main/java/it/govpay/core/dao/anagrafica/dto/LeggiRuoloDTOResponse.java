/**
 * 
 */
package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.model.Acl;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiRuoloDTOResponse extends BasicFindResponseDTO<Acl>{

	/**
	 * @param totalResults
	 * @param results
	 */
	public LeggiRuoloDTOResponse(long totalResults, List<Acl> results) {
		super(totalResults, results);
		// TODO Auto-generated constructor stub
	}


}
