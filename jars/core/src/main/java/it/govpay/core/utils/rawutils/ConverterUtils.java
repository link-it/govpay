/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.rawutils;

import java.util.Arrays;

import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.JsonJacksonSerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentV2Response;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTV2Request;
import it.govpay.bd.model.Rpt;
import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.MessaggiPagoPAUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.pagopa.beans.utils.JaxbUtils;

public class ConverterUtils {

	private static ObjectMapper mapper;
	static {
		mapper = new ObjectMapper();
		mapper.registerModule(new JaxbAnnotationModule());
		mapper.registerModule(new DateModule());
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
	}

	public static String getRptJson(Rpt rpt) throws IOException {
		return getRptJson(rpt, false);
	}

	public static String getRptJson(Rpt rpt, boolean convertiMessaggioPagoPAV2InPagoPAV1) throws IOException {
		if(rpt.getXmlRpt() == null)
			return null;

		try {
			switch (rpt.getVersione()) {
			case SANP_230:
				CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
				return toJSON(ctRpt);
			case SANP_240:
				PaGetPaymentRes paGetPaymentRes_RPT = JaxbUtils.toPaGetPaymentRes_RPT(rpt.getXmlRpt(), false);
				
				if(convertiMessaggioPagoPAV2InPagoPAV1) {
					CtRichiestaPagamentoTelematico ctRpt2 = MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentRes_RPT, rpt);
					return toJSON(ctRpt2);
				}
				return toJSON(paGetPaymentRes_RPT.getData());
			case SANP_321_V2:
				PaGetPaymentV2Response paGetPaymentV2Response = JaxbUtils.toPaGetPaymentV2Response_RPT(rpt.getXmlRpt(), false);
				
				if(convertiMessaggioPagoPAV2InPagoPAV1) {
					CtRichiestaPagamentoTelematico ctRpt2 = MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentV2Response, rpt);
					return toJSON(ctRpt2);
				}
				
				return toJSON(paGetPaymentV2Response.getData());
			}
			
			CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
			return toJSON(ctRpt);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static String getRptJson(CtRichiestaPagamentoTelematico ctRpt) throws IOException {
		return toJSON(ctRpt);
	}
	
	public static String getRptJson(PaGetPaymentRes paGetPaymentRes_RPT) throws IOException {
		if(paGetPaymentRes_RPT == null) return null;
		return toJSON(paGetPaymentRes_RPT.getData());
	}
	
	public static String getRptJson(PaGetPaymentV2Response paGetPaymentResV2Response) throws IOException {
		if(paGetPaymentResV2Response == null) return null;
		return toJSON(paGetPaymentResV2Response.getData());
	}
	
	public static String getRtJson(Rpt rpt) throws IOException {
		return getRtJson(rpt, false);
	}
	
	public static String getRtJson(Rpt rpt, boolean convertiMessaggioPagoPAV2InPagoPAV1) throws IOException {
		if(rpt.getXmlRt() == null)
			return null;


		try {
			switch (rpt.getVersione()) {
			case SANP_230:
				CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
				return toJSON(ctRt);
			case SANP_240:
				PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
				
				if(convertiMessaggioPagoPAV2InPagoPAV1) {
					CtRicevutaTelematica ctRt2 = MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTReq_RT, rpt);
					return toJSON(ctRt2);
				}
				
				return toJSON(paSendRTReq_RT.getReceipt());
			case SANP_321_V2:
				PaSendRTV2Request paSendRTRtv2Request = JaxbUtils.toPaSendRTV2Request_RT(rpt.getXmlRt(), false);
				
				if(convertiMessaggioPagoPAV2InPagoPAV1) {
					CtRicevutaTelematica ctRt2 = MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTRtv2Request, rpt);
					return toJSON(ctRt2);
				}
				
				return toJSON(paSendRTRtv2Request.getReceipt());
			}
			
			CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
			return toJSON(ctRt);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static String getRtJson(CtRicevutaTelematica ctRt ) throws IOException {
		return toJSON(ctRt);
	}
	
	public static String getRtJson(PaSendRTReq paSendRTReq_RT ) throws IOException {
		if(paSendRTReq_RT == null) return null;
		return toJSON(paSendRTReq_RT.getReceipt());
	}
	
	public static String getRtJson(PaSendRTV2Request paSendRTReq_RT ) throws IOException {
		if(paSendRTReq_RT == null) return null;
		return toJSON(paSendRTReq_RT.getReceipt());
	}
	
	public static String toJSON(Object obj) throws IOException {
		if(obj == null)
			return null;
		
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new IOException(e);
		}
	}
	
	public static String toJSON(Object obj, String fields, SerializationConfig serializationConfig) throws IOException {
		try {
			if(fields != null && !fields.isEmpty()) {
				serializationConfig.setIncludes(Arrays.asList(fields.split(",")));
				serializationConfig.setExcludes(null); 
			}
			ISerializer serializer = new JsonJacksonSerializer(serializationConfig);
			return serializer.getObject(obj);
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new IOException("Errore nella serializzazione della risposta.", e);
		}
	}
	
	public static <T> T parse(String jsonString, Class<T> t) throws IOException  {
		return JSONSerializable.parse(jsonString, t);
	}
	
	public static <T> T parse(String jsonString, Class<T> t, it.govpay.core.utils.serialization.SerializationConfig serializationConfig) throws IOException  {
		return JSONSerializable.parse(jsonString, t, serializationConfig);
	}
}
