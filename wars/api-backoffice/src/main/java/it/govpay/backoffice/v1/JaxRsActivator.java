package it.govpay.backoffice.v1;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/rs/v1")
public class JaxRsActivator extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(Applicazioni.class);
        resources.add(Avvisi.class);
        resources.add(Check.class);
        resources.add(Domini.class);
        resources.add(Entrate.class);
        resources.add(Enumerazioni.class);
        resources.add(Eventi.class);
        resources.add(FlussiRendicontazione.class);
        resources.add(Incassi.class);
        resources.add(Info.class);
        resources.add(Intermediari.class);
        resources.add(Operatori.class);
        resources.add(Operazioni.class);
        resources.add(Pagamenti.class);
        resources.add(Pendenze.class);
        resources.add(Profilo.class);
        resources.add(Reportistiche.class);
        resources.add(Riscossioni.class);
        resources.add(Rpp.class);
        resources.add(Ruoli.class);
        resources.add(Tracciati.class);
        return resources;
    }
}
