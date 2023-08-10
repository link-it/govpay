package it.govpay.core.utils.serialization;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

public class BeanDeserializerModifierForIgnorables extends BeanDeserializerModifier {

    private List<String> ignorables;

    public BeanDeserializerModifierForIgnorables(List<String> properties) {
    	if(properties!=null)
    		this.ignorables = properties;
    	else
    		this.ignorables = new ArrayList<>();
    }

    @Override
    public BeanDeserializerBuilder updateBuilder(
            DeserializationConfig config, BeanDescription beanDesc,
            BeanDeserializerBuilder builder) {

        for(String ignorable : this.ignorables) {
            builder.addIgnorable(ignorable);                
        }

        return builder;
    }

    @Override
    public List<BeanPropertyDefinition> updateProperties(
            DeserializationConfig config, BeanDescription beanDesc,
            List<BeanPropertyDefinition> propDefs) {

        List<BeanPropertyDefinition> newPropDefs = new ArrayList<>();
        for(BeanPropertyDefinition propDef : propDefs) {
            if(!this.ignorables.contains(propDef.getName())) {
                newPropDefs.add(propDef);
            }
        }
        return newPropDefs;
    }
}
