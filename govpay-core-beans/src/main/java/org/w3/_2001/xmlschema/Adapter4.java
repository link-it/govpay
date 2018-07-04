
package org.w3._2001.xmlschema;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter4
    extends XmlAdapter<String, Date>
{


    public Date unmarshal(String value) {
        return (it.govpay.core.utils.adapter.DataTypeAdapter.parseDate(value));
    }

    public String marshal(Date value) {
        return (it.govpay.core.utils.adapter.DataTypeAdapter.printDate(value));
    }

}
