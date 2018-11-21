package it.govpay.pagamento.v1;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rs/v1")
public class JaxRsActivator extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(Avvisi.class);
        resources.add(Domini.class);
        resources.add(Pagamenti.class);
        resources.add(Pendenze.class);
        resources.add(Profilo.class);
        resources.add(Rpp.class);
        return resources;
    }
}
