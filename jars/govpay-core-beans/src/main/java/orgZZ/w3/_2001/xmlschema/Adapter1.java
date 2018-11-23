
package orgZZ.w3._2001.xmlschema;

import java.math.BigDecimal;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, BigDecimal>
{


    @Override
	public BigDecimal unmarshal(String value) {
        return (it.govpay.core.utils.adapter.DataTypeAdapter.parseImporto(value));
    }

    @Override
	public String marshal(BigDecimal value) {
        return (it.govpay.core.utils.adapter.DataTypeAdapter.printImporto(value));
    }

}
