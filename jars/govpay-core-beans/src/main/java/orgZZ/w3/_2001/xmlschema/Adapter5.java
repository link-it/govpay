
package orgZZ.w3._2001.xmlschema;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter5
    extends XmlAdapter<String, Integer>
{


    @Override
	public Integer unmarshal(String value) {
        return (it.govpay.core.utils.adapter.DataTypeAdapter.parseYear(value));
    }

    @Override
	public String marshal(Integer value) {
        return (it.govpay.core.utils.adapter.DataTypeAdapter.printYear(value));
    }

}
