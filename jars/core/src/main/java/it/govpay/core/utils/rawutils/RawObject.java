package it.govpay.core.utils.rawutils;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = RawObjectSerializer.class)
@JsonDeserialize(using = RawObjectDeserializer.class)
public class RawObject {

    public final String value;

    public RawObject(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}