package it.govpay.core.dao.anagrafica;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.filters.ApplicazioneFilter;
import it.govpay.core.dao.anagrafica.dto.FindApplicazioniDTOResponse;
import it.govpay.core.dao.anagrafica.dto.ListaAclDTO;
import it.govpay.core.dao.anagrafica.dto.ListaAclDTOResponse;
import it.govpay.core.utils.GpThreadLocal;

public class AclDAO {

	public ListaAclDTOResponse leggiAclRuoloRegistrateSistema(ListaAclDTO listaAclDTO) {
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		try {
			
			AclBD aclBD = new AclBD(bd);
			
			ApplicazioneFilter filter = null;
			if(listaApplicazioniDTO.isSimpleSearch()) {
				filter = applicazioniBD.newFilter(true);
				filter.setSimpleSearchString(listaApplicazioniDTO.getSimpleSearch());
			} else {
				filter = applicazioniBD.newFilter(false);
				filter.setSearchAbilitato(listaApplicazioniDTO.getAbilitato());
			}
//			filter.setListaIdApplicazioni(applicazioni.stream().collect(Collectors.toList()));
			filter.setOffset(listaApplicazioniDTO.getOffset());
			filter.setLimit(listaApplicazioniDTO.getLimit());
			filter.getFilterSortList().addAll(listaApplicazioniDTO.getFieldSortList());
			
			return new ListaAclDTOResponse(applicazioniBD.count(filter), applicazioniBD.findAll(filter));
			
		} finally {
			bd.closeConnection();
		}
	}
	
}
