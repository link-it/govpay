package it.govpay.pagamento.v2;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rs/v2")
public class JaxRsActivator extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(it.govpay.pagamento.v1.Avvisi.class);
        resources.add(it.govpay.pagamento.v1.Domini.class);
        resources.add(it.govpay.pagamento.v1.Pagamenti.class);
        resources.add(Pendenze.class);
        resources.add(it.govpay.pagamento.v1.Profilo.class);
        resources.add(it.govpay.pagamento.v1.Rpp.class);
        return resources;
    }
}
