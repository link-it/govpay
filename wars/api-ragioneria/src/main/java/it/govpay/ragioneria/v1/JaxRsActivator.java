package it.govpay.ragioneria.v1;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rs/v1")
public class JaxRsActivator extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(Domini.class);
        resources.add(FlussiRendicontazione.class);
        resources.add(Incassi.class);
        resources.add(Profilo.class);
        resources.add(Riscossioni.class);
        return resources;
    }
}
