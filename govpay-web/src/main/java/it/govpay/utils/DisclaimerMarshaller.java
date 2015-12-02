/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.utils;

import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
public class DisclaimerMarshaller extends Marshaller.Listener {

    private String disclaimer;
	private XMLStreamWriter xsw;
    private boolean done;
    
    public  DisclaimerMarshaller(XMLStreamWriter xsw, String disclaimer) {
        this.xsw = xsw;
        this.disclaimer = disclaimer;
        this.done = false;
    }

    @Override
    public void beforeMarshal(Object source)  {
        try {
        	if(!done)
        		xsw.writeComment("\t" + this.disclaimer +"\t");
        	
        	done = true;
        } catch(XMLStreamException e) {}
    }

}