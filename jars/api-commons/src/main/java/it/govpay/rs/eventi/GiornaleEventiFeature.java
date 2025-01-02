/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.rs.eventi;


import org.apache.cxf.Bus;
import org.apache.cxf.annotations.Provider;
import org.apache.cxf.annotations.Provider.Type;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

@NoJSR250Annotations
@Provider(value = Type.Feature)
public class GiornaleEventiFeature extends AbstractFeature{

	private GiornaleEventiOutInterceptor out;
	private GiornaleEventiConfig giornaleEventiConfig = null;
	private GiornaleEventiCollectorOutInterceptor outCollector;

	public GiornaleEventiFeature() {
		this.out = new GiornaleEventiOutInterceptor();
		this.outCollector = new GiornaleEventiCollectorOutInterceptor();
	}

	@Override
	protected void initializeProvider(InterceptorProvider provider, Bus bus) {
		if(this.giornaleEventiConfig == null) {
			this.giornaleEventiConfig = new GiornaleEventiConfig();
			this.out.setGiornaleEventiConfig(this.giornaleEventiConfig);
			this.outCollector.setGiornaleEventiConfig(this.giornaleEventiConfig);
		}

			provider.getOutInterceptors().add(this.outCollector);
			provider.getOutFaultInterceptors().add(this.outCollector);
			provider.getOutInterceptors().add(this.out);
			provider.getOutFaultInterceptors().add(this.out);
	}

	public GiornaleEventiConfig getGiornaleEventiConfig() {
		return giornaleEventiConfig;
	}

	public void setGiornaleEventiConfig(GiornaleEventiConfig giornaleEventiConfig) {
		this.giornaleEventiConfig = giornaleEventiConfig;
		this.out.setGiornaleEventiConfig(giornaleEventiConfig);
		this.outCollector.setGiornaleEventiConfig(giornaleEventiConfig);
	}
}
