package it.govpay.features;


import org.apache.cxf.Bus;
import org.apache.cxf.annotations.Provider;
import org.apache.cxf.annotations.Provider.Type;
import org.apache.cxf.common.injection.NoJSR250Annotations;
import org.apache.cxf.ext.logging.event.LogEventSender;
import org.apache.cxf.ext.logging.event.PrettyLoggingFilter;
import org.apache.cxf.ext.logging.slf4j.Slf4jEventSender;
import org.apache.cxf.ext.logging.slf4j.Slf4jVerboseEventSender;
import org.apache.cxf.interceptor.InterceptorProvider;

@NoJSR250Annotations
@Provider(value = Type.Feature)
public class LoggingFeature extends org.apache.cxf.ext.logging.LoggingFeature {
	private LoggingInInterceptor in;
	private LoggingOutInterceptor out;
	private PrettyLoggingFilter inPrettyFilter;
	private PrettyLoggingFilter outPrettyFilter;

	public LoggingFeature() {
		LogEventSender sender = new Slf4jVerboseEventSender();
		inPrettyFilter = new PrettyLoggingFilter(sender);
		outPrettyFilter = new PrettyLoggingFilter(sender);
		in = new LoggingInInterceptor(inPrettyFilter);
		out = new LoggingOutInterceptor(outPrettyFilter);
	}

	@Override
	protected void initializeProvider(InterceptorProvider provider, Bus bus) {
		provider.getInInterceptors().add(in);
		provider.getInFaultInterceptors().add(in);

		provider.getOutInterceptors().add(out);
		provider.getOutFaultInterceptors().add(out);
	}

	public void setLimit(int limit) {
		in.setLimit(limit);
		out.setLimit(limit);
	}

	public void setInMemThreshold(long inMemThreshold) {
		in.setInMemThreshold(inMemThreshold);
		out.setInMemThreshold(inMemThreshold);
	}

	public void setSender(LogEventSender sender) {
		this.inPrettyFilter.setNext(sender);
		this.outPrettyFilter.setNext(sender);
	}
	public void setInSender(LogEventSender s) {
		this.inPrettyFilter.setNext(s);
	}
	public void setOutSender(LogEventSender s) {
		this.outPrettyFilter.setNext(s);
	}

	public void setPrettyLogging(boolean prettyLogging) {
		this.inPrettyFilter.setPrettyLogging(prettyLogging);
		this.outPrettyFilter.setPrettyLogging(prettyLogging);
	}

	/**
	 * Log binary content?
	 * @param logBinary defaults to false
	 */
	public void setLogBinary(boolean logBinary) {
		in.setLogBinary(logBinary);
		out.setLogBinary(logBinary);
	}

	/**
	 * Log multipart content?
	 * @param logMultipart defaults to true
	 */
	public void setLogMultipart(boolean logMultipart) {
		in.setLogMultipart(logMultipart);
		out.setLogMultipart(logMultipart);
	}

	public void setVerbose(boolean verbose) {
		setSender(verbose ? new Slf4jVerboseEventSender() : new Slf4jEventSender());
	}
}
