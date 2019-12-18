package it.govpay.pagamento.v3.beans.deserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.openspcoop2.utils.jaxrs.JacksonJsonProvider;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.govpay.pagamento.v3.beans.NuovaPendenza;
import it.govpay.pagamento.v3.beans.RiferimentoAvviso;
import it.govpay.pagamento.v3.beans.RiferimentoPendenza;

public class CustomPendenzeDeserializer extends JsonDeserializer<Object> {
	
	private TimeZone timeZone = TimeZone.getDefault();
    private String timeZoneId = null;
    public String getTimeZoneId() {
            return this.timeZoneId;
    }
    public void setTimeZoneId(String timeZoneId) {
            this.timeZoneId = timeZoneId;
            this.timeZone = TimeZone.getTimeZone(timeZoneId);
    }

	@Override
	public List<Object> deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		
		ObjectMapper objectMapper = JacksonJsonProvider.getObjectMapper(true,this.timeZone);
		ObjectCodec oc = jsonParser.getCodec();
		JsonNode node = oc.readTree(jsonParser);
        Iterator<JsonNode> elements = node.elements();
        
        List<Object> pendenze = new ArrayList<Object>();
        for (; elements.hasNext();) {
        	JsonNode next = elements.next();
        	
        	if(next.size() == 2 && next.hasNonNull("idDominio") && next.hasNonNull("numeroAvviso")) {
    			pendenze.add(objectMapper.treeToValue(next, RiferimentoAvviso.class));
    			continue;
        	}
        	if(next.size() == 2 && next.hasNonNull("idA2A") && next.hasNonNull("idPendenza")) {
    			pendenze.add(objectMapper.treeToValue(next, RiferimentoPendenza.class));
    			continue;
        	}
        	
        	pendenze.add(objectMapper.treeToValue(next, NuovaPendenza.class));
        }
		return pendenze;
	}

}