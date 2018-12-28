package it.govpay.features;


import org.apache.cxf.Bus;
import org.apache.cxf.annotations.Provider;
import org.apache.cxf.annotations.Provider.Type;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

@NoJSR250Annotations
@Provider(value = Type.Feature)
public class ContextFeature extends AbstractFeature {
	private ContextInInterceptor in;
	private ContextOutInterceptor out;

	public ContextFeature() {
		in = new ContextInInterceptor();
		out = new ContextOutInterceptor();
	}

	@Override
	protected void initializeProvider(InterceptorProvider provider, Bus bus) {
		provider.getInInterceptors().add(in);
		provider.getInFaultInterceptors().add(in);

		provider.getOutInterceptors().add(out);
		provider.getOutFaultInterceptors().add(out);
	}
}
