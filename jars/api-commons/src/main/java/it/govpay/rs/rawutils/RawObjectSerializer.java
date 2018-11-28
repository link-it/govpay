package it.govpay.rs.rawutils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

public class RawObjectSerializer extends StdSerializer<RawObject> {

	private static final long serialVersionUID = 1L;

	public RawObjectSerializer() {
        super(RawObject.class);
    }

    @Override
    public void serialize(RawObject value, JsonGenerator generator, SerializerProvider provider) throws IOException {
        if (value.getValue() == null) {
            generator.writeNull();
        } else {
            generator.writeRawValue(value.getValue());
        }
    }
}
