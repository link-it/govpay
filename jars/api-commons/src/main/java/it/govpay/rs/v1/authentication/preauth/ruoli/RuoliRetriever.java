package it.govpay.rs.v1.authentication.preauth.ruoli;

import java.util.Collections;
import java.util.Set;

import org.openspcoop2.utils.jaxrs.impl.authentication.preauth.role.AbstractRoleRetriever;

import it.govpay.core.cache.AclCache;

public class RuoliRetriever extends AbstractRoleRetriever  {

	@Override
	protected Set<String> getElencoRuoliCustomAbilitati() {
		return Collections.unmodifiableSet(AclCache.getInstance().getChiaviRuoli());
	}

}
