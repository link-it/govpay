package it.govpay.pagopa.v2.beans.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import it.govpay.pagopa.v2.beans.Connettore;
import it.govpay.pagopa.v2.beans.Versionabile.Versione;
import it.govpay.pagopa.v2.entity.ConnettoreEntity;

@Mapper(componentModel = "spring")
public abstract class ConnettoreMapper {
	
	public Connettore getConnettore(List<ConnettoreEntity> listConnettore) {
		if(listConnettore == null || listConnettore.size() == 0) return null;
			
		Map<String, String> map = listConnettore.stream().collect(Collectors.toMap(ConnettoreEntity::getCodProprieta, ConnettoreEntity::getValore));

		map.put("idConnettore", listConnettore.get(0).getCodConnettore());
		return connettoreMapToConnettoreBean(map);
	}
	
	
	@Mappings({
		// Mapping versione gestito tramite metodo custom
		 @Mapping(source = "VERSIONE", target = "versione", qualifiedByName = "toVersionableVersione"),
		 
		 // Magging tra proprieta con nome diverso
		 @Mapping(source = "TIPOAUTENTICAZIONE", target = "tipoAutenticazione"),
		 @Mapping(source = "TIPOSSL", target = "tipoSsl"),
		 @Mapping(source = "SSLKSTYPE", target = "sslKsType"),
		 @Mapping(source = "SSLKSLOCATION", target = "sslKsLocation"),
		 @Mapping(source = "SSLKSPASSWD", target = "sslKsPasswd"),
		 @Mapping(source = "SSLPKEYPASSWD", target = "sslPKeyPasswd"),
		 @Mapping(source = "SSLTSTYPE", target = "sslTsType"),
		 @Mapping(source = "SSLTSLOCATION", target = "sslTsLocation"),
		 @Mapping(source = "SSLTSPASSWD", target = "sslTsPasswd"),
		 @Mapping(source = "SSLTYPE", target = "sslType"),
		 @Mapping(source = "HTTPUSER", target = "httpUser"),
		 @Mapping(source = "HTTPPASSW", target = "httpPassw"),
		 @Mapping(source = "URL", target = "url"),
		 @Mapping(source = "URLAVVISI", target = "urlServiziAvvisatura"),
		 @Mapping(source = "AZIONEINURL", target = "azioneInUrl")
		 
	})
	public abstract Connettore connettoreMapToConnettoreBean(Map<String,String> map);
	
	
	@Named("toVersionableVersione") 
    public Versione toVersionableVersione(String versione) { 
		if(versione == null)
			return null;
		
		return Versione.toEnum(versione);
    }
}
