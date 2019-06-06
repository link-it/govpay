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
