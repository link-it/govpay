package it.govpay.pagopa.v2.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import it.govpay.gde.GdeInvoker;

@EnableWs
@Configuration
public class WsConfig extends WsConfigurerAdapter{

	@Value("${it.govpay.giornaleEventi.url}")
	private String urlGde;

	@Bean
	public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean<MessageDispatcherServlet>(servlet, "/ws/*");
	}

	@Bean(name = "paForNodeWsdl")
	public Wsdl11Definition defaultWsdl11Definition(XsdSchema paForNodeSchema) 
	{
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("wsdl/paForNode.wsdl")); //your wsdl location
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema paForNodeSchema() 
	{
		return new SimpleXsdSchema(new ClassPathResource("xsd/paForNode.xsd"));
	}

	@Bean
	public GdeInvoker gdeInvoker() {
		return new GdeInvoker(this.urlGde);
	}
}
