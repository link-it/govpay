package it.govpay.stampe.pdf.avvisoPagamento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class AvvisoPagamentoDatasource implements JRDataSource{

	private List<Map<String, Object>> parameters = new ArrayList<Map<String, Object>>();
	private int index = -1;
//	private Logger log = null;

	public AvvisoPagamentoDatasource(Map<String, Object> parameters, Logger log) {
		this.parameters.add(parameters);
//		this.log = log;
	}

	@Override
	public boolean next() throws JRException {
		this.index++;
		return this.index < this.parameters.size();
	}

	@Override
	public Object getFieldValue(JRField jrField) throws JRException {
		Map<String, Object> map = this.parameters.get(index);
		System.out.println("Aggiungo field ["+jrField.getName()+"] con valore ["+map.get(jrField.getName())+"]. ");
//		this.log.debug("Aggiungo field ["+jrField.getName()+"] con valore ["+map.get(jrField.getName())+"]. ");
		return map.get(jrField.getName());
	}
}
