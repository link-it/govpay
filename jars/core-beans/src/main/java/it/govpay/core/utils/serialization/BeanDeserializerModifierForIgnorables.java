/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.core.utils.serialization;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.BeanDeserializerBuilder;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;

public class BeanDeserializerModifierForIgnorables extends BeanDeserializerModifier {

    private static final long serialVersionUID = 1L;
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
