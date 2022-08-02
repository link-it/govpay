package it.govpay.gde.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.ValueMapping;
import org.mapstruct.ValueMappings;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.govpay.gde.entity.DatiPagoPAEntity;
import it.govpay.gde.entity.EventoEntity;
import it.govpay.gde.entity.converter.DatiPagoPAConverter;
import it.govpay.gde.exception.InternalException;
import it.govpay.gde.model.CategoriaEvento;
import it.govpay.gde.model.DatiPagoPAModel;
import it.govpay.gde.model.NuovoEventoModel;
import it.govpay.gde.model.RuoloEvento;

@Mapper(componentModel = "spring")
public abstract class NuovoEventoMapper {

	private final static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	@Mappings({
		// Mapping tra oggetto e byte[]/String gestiti tramite metodo custom
		 @Mapping(source = "parametriRichiesta", target = "parametriRichiesta", qualifiedByName = "parametriRichiestaToByteArray"),
		 @Mapping(source = "parametriRisposta", target = "parametriRisposta", qualifiedByName = "parametriRispostaToByteArray"),
		 @Mapping(source = "datiPagoPA", target = "datiPagoPA", qualifiedByName = "datiPagoPAToString"),
		 
		 // Magging tra proprieta con nome diverso
		 @Mapping(source = "ruolo", target = "ruoloEvento"),
		 @Mapping(source = "esito", target = "esitoEvento"),
		 @Mapping(source = "dataEvento", target = "data"),
		 @Mapping(source = "durataEvento", target = "intervallo"),
		 @Mapping(source = "idDominio", target = "codDominio"),
		 @Mapping(source = "idA2A", target = "codApplicazione"),
		 @Mapping(source = "idPendenza", target = "codVersamentoEnte"),
		 @Mapping(source = "idPagamento", target = "idSessione"),
		 @Mapping(source = "idRiconciliazione", target = "idIncasso")
	})
	public abstract EventoEntity nuovoEventoModelToEventoEntity(NuovoEventoModel model);
	
	
	@Mappings({
		 @Mapping(source = "idPsp", target = "codPsp"),
		 @Mapping(source = "idCanale", target = "codCanale"),
		 @Mapping(source = "idIntermediarioPsp", target = "codIntermediarioPsp"),
		 @Mapping(source = "idDominio", target = "codDominio"),
		 @Mapping(source = "idIntermediario", target = "codIntermediario"),
		 @Mapping(source = "idStazione", target = "codStazione"),
		 @Mapping(source = "idRiconciliazione", target = "trn"),
		 @Mapping(source = "idFlusso", target = "codFlusso")
	})
	public abstract DatiPagoPAEntity datiPagoPAModelToDatiPagoPAEntity(DatiPagoPAModel model);
	
	@ValueMappings({
        @ValueMapping(target = "B", source = "INTERNO"),
        @ValueMapping(target = "I", source = "INTERFACCIA"),
        @ValueMapping(target = "U", source = "UTENTE")
    })
	public abstract EventoEntity.CategoriaEvento categoriaEventoModelToCategoriaEventoEntity(CategoriaEvento model);
	
	
	@ValueMappings({
        @ValueMapping(target = "C", source = "CLIENT"),
        @ValueMapping(target = "S", source = "SERVER")
    })
	public abstract EventoEntity.RuoloEvento ruoloEventoModelToRuoloEventoEntity(RuoloEvento model);
	
	@Named("datiPagoPAToString") 
    public String datiPagoPAToString(DatiPagoPAModel model) { 
		if(model == null)
			return null;
		
		DatiPagoPAEntity datiPagoPAEntity = datiPagoPAModelToDatiPagoPAEntity(model);
		DatiPagoPAConverter converter = new DatiPagoPAConverter();
		return converter.convertToDatabaseColumn(datiPagoPAEntity);
    }
	
	@Named("parametriRichiestaToByteArray") 
    public byte[] parametriRichiestaToByteArray(Object parametriRichiesta) {
		if(parametriRichiesta == null) 
			return null;
		try {
			return NuovoEventoMapper.objectMapper.writeValueAsBytes(parametriRichiesta);
		} catch (JsonProcessingException e) {
			throw new InternalException(e);
		}
    }
	
	@Named("parametriRispostaToByteArray") 
    public byte[] parametriRispostaToByteArray(Object parametriRisposta) {
		if(parametriRisposta == null) 
			return null;
		try {
			return NuovoEventoMapper.objectMapper.writeValueAsBytes(parametriRisposta);
		} catch (JsonProcessingException e) {
			throw new InternalException(e);
		}
    }
}
