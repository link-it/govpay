package it.govpay.core.utils.rawutils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class RawObjectDeserializer extends StdDeserializer<RawObject> {

	private static final long serialVersionUID = 1L;

	public RawObjectDeserializer() {
        super(RawObject.class);
    }

    @Override
    public RawObject deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        return new RawObject(parser.getCodec().readTree(parser).toString());
    }
}