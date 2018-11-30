package it.govpay.rs.v1.authentication.preauth.ruoli;

import java.util.Collections;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.core.authority.mapping.MappableAttributesRetriever;

import it.govpay.core.cache.AclCache;

public class RuoliRetriever implements ResourceLoaderAware,
MappableAttributesRetriever, InitializingBean {
	
	@Override
	public void afterPropertiesSet() throws Exception {
	}

	@Override
	public Set<String> getMappableAttributes() {
		return Collections.unmodifiableSet(AclCache.getInstance().getChiaviRuoli());
	}

	@Override
	public void setResourceLoader(ResourceLoader arg0) {
	}

}
