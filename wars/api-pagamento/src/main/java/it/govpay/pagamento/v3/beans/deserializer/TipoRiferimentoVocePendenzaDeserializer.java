package it.govpay.pagamento.v3.beans.deserializer;

import java.io.IOException;
import java.util.TimeZone;

import org.openspcoop2.utils.jaxrs.JacksonJsonProvider;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.govpay.pagamento.v3.beans.Bollo;
import it.govpay.pagamento.v3.beans.Entrata;
import it.govpay.pagamento.v3.beans.RiferimentoEntrata;

public class TipoRiferimentoVocePendenzaDeserializer extends JsonDeserializer<Object> {
	
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
	public Object deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		
		ObjectMapper objectMapper = JacksonJsonProvider.getObjectMapper(true,this.timeZone);
		
		ObjectCodec oc = jsonParser.getCodec();
		
		JsonNode node = oc.readTree(jsonParser);
		
		if(node.size() == 3 && node.hasNonNull("tipoBollo") && node.hasNonNull("hashDocumento") && node.hasNonNull("provinciaResidenza")) {
			return objectMapper.treeToValue(node,  Bollo.class);
		} else if(node.size() == 1 && node.hasNonNull("codEntrata")) {
			return objectMapper.treeToValue(node,  RiferimentoEntrata.class);
    	}
		else {
			return objectMapper.treeToValue(node,  Entrata.class);
		}
//
//		
//		
//        Iterator<JsonNode> elements = node.elements();
//        
//        List<Object> pendenze = new ArrayList<Object>();
//        for (; elements.hasNext();) {
//        	JsonNode next = elements.next();
//        	
//        	
//        	if(next.size() == 3 && next.hasNonNull("tipoBollo") && next.hasNonNull("hashDocumento") && next.hasNonNull("provinciaResidenza")) {
//    			pendenze.add(objectMapper.treeToValue(next, Bollo.class));
//    			continue;
//        	}
//        	if(next.size() == 1 && next.hasNonNull("codEntrata")) {
//    			pendenze.add(objectMapper.treeToValue(next, RiferimentoEntrata.class));
//    			continue;
//        	}
        	
//        	pendenze.add(objectMapper.treeToValue(next, Entrata.class));
//        	pendenze.add(objectMapper.treeToValue(next, NuovaVocePendenza.class));
//        }
//        
//        
//        
//		return pendenze;
	}

}