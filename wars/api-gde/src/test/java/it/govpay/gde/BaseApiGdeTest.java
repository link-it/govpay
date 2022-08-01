package it.govpay.gde;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import it.govpay.gde.model.CategoriaEvento;
import it.govpay.gde.model.ComponenteEvento;
import it.govpay.gde.model.DatiPagoPAModel;
import it.govpay.gde.model.EsitoEvento;
import it.govpay.gde.model.EventoIndexModel;
import it.govpay.gde.model.NuovoEventoModel;
import it.govpay.gde.model.RuoloEvento;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public abstract class BaseApiGdeTest {
	
	public static final String STRING_256 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
	public static final String PATTERN_ID_DOMINIO_ERRATO = "123456 8901";
	public static final String PATTERN_ID_A2A_ERRATO = "aaaaaa_a 123456 8901";
	public static final String PATTERN_ID_PENDENZA_ERRATO = "aaaaa_aa 123456 8901";
	
	@Autowired
	protected MockMvc mvc;
	
	protected ObjectMapper objectMapper = null;
	
	public static DateFormat createDefaultDateFormat() {
		SimpleDateFormat sdf = new SimpleDateFormat(DateFormatUtils.ISO_8601_EXTENDED_DATETIME_FORMAT.getPattern());
		sdf.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
		sdf.setLenient(false);
        return sdf;
    }
	
	public BaseApiGdeTest() {
		objectMapper = new ObjectMapper();
		
		objectMapper.setDateFormat(createDefaultDateFormat());
		objectMapper.registerModule(new JavaTimeModule());

		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.registerModule(new Jackson2HalModule());
	}

	protected String mapToJson(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}
	protected <T> T mapFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		return objectMapper.readValue(json, clazz);
	}
	
	protected <T> PagedModel<T> pagedModelFromJson(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		return objectMapper.readValue(json, new TypeReference<PagedModel<T>>() {});
	}
	
	protected Map<String, Object> mapFromJson(String json)
			throws JsonParseException, JsonMappingException, IOException {

		return objectMapper.readValue(json, new TypeReference<HashMap<String,Object>>() {});
	}
	
	protected NuovoEventoModel buildNuovoEventoOK() {
		NuovoEventoModel nuovoEvento = new NuovoEventoModel();
		nuovoEvento.setCategoriaEvento(CategoriaEvento.INTERNO);
		nuovoEvento.setRuolo(RuoloEvento.CLIENT);
		nuovoEvento.setComponente(ComponenteEvento.GOVPAY);
		nuovoEvento.setDataEvento(LocalDateTime.now());
		nuovoEvento.setDurataEvento(0l);
		nuovoEvento.setTipoEvento("tipoEvento");
		nuovoEvento.setSottotipoEvento("sottotipoEvento");
		nuovoEvento.setEsito(EsitoEvento.OK);
		
		return nuovoEvento;
	}

	protected DatiPagoPAModel creaDatiPagoPAOk() {
		DatiPagoPAModel datiPagoPA = new DatiPagoPAModel();
		datiPagoPA.setIdCanale("codCanale");
		datiPagoPA.setIdDominio("12345678901");
		datiPagoPA.setIdFlusso("idFr_1234567890");
		datiPagoPA.setIdIntermediario("11111111113");
		datiPagoPA.setIdIntermediarioPsp("11111111113");
		datiPagoPA.setIdPsp("codPsp");
		datiPagoPA.setIdRiconciliazione("trn_1234567890");
		datiPagoPA.setIdStazione("11111111113_01");
		datiPagoPA.setIdTracciato(BigDecimal.ONE);
		datiPagoPA.setModelloPagamento("0");
		datiPagoPA.setSct("sct_1234567890");
		datiPagoPA.setTipoVersamento("CP");
		return datiPagoPA;
	}
	
	protected Long leggiIdUltimoEvento(MvcResult mvcResult)
			throws JsonParseException, JsonMappingException, IOException, UnsupportedEncodingException {
		PagedModel<EventoIndexModel> mapFromJson  = this.pagedModelFromJson(mvcResult.getResponse().getContentAsString(), EventoIndexModel.class); 

		List<EventoIndexModel> eventi = new ArrayList<>( mapFromJson.getContent());
		
		@SuppressWarnings("unchecked")
		Map<String, Object> mapEvento = (Map<String, Object>) eventi.get(0);
		Long idEvento = Long.valueOf(mapEvento.get("id").toString());
		return idEvento;
	}
}
