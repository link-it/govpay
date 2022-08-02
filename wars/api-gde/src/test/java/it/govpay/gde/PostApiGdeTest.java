package it.govpay.gde;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import it.govpay.gde.model.DatiPagoPAModel;
import it.govpay.gde.model.NuovoEventoModel;

public class PostApiGdeTest extends BaseApiGdeTest {

	@Test
	public void testOK() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());
		
		mvc.perform(get("/eventi")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$._embedded.eventi[0].categoriaEvento", is(nuovoEvento.getCategoriaEvento().getValue())))
		.andExpect(jsonPath("$._embedded.eventi[0].ruolo", is(nuovoEvento.getRuolo().getValue())))
		.andExpect(jsonPath("$._embedded.eventi[0].componente", is(nuovoEvento.getComponente().getValue())))
		.andExpect(jsonPath("$._embedded.eventi[0].durataEvento", is(nuovoEvento.getDurataEvento()), long.class))
		.andExpect(jsonPath("$._embedded.eventi[0].tipoEvento", is(nuovoEvento.getTipoEvento())))
		.andExpect(jsonPath("$._embedded.eventi[0].sottotipoEvento", is(nuovoEvento.getSottotipoEvento())))
		.andExpect(jsonPath("$._embedded.eventi[0].esito", is(nuovoEvento.getEsito().getValue())));
		
	}
	
	@Test
	public void testDatiPagoPAOK() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		
		DatiPagoPAModel datiPagoPA = creaDatiPagoPAOk();
		nuovoEvento.setDatiPagoPA(datiPagoPA );
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());
		
		mvc.perform(get("/eventi")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$._embedded.eventi[0].categoriaEvento", is(nuovoEvento.getCategoriaEvento().getValue())))
		.andExpect(jsonPath("$._embedded.eventi[0].ruolo", is(nuovoEvento.getRuolo().getValue())))
		.andExpect(jsonPath("$._embedded.eventi[0].componente", is(nuovoEvento.getComponente().getValue())))
		.andExpect(jsonPath("$._embedded.eventi[0].durataEvento", is(nuovoEvento.getDurataEvento()), long.class))
		.andExpect(jsonPath("$._embedded.eventi[0].tipoEvento", is(nuovoEvento.getTipoEvento())))
		.andExpect(jsonPath("$._embedded.eventi[0].sottotipoEvento", is(nuovoEvento.getSottotipoEvento())))
		.andExpect(jsonPath("$._embedded.eventi[0].esito", is(nuovoEvento.getEsito().getValue())))
		.andExpect(jsonPath("$._embedded.eventi[0].datiPagoPA.idCanale", is(nuovoEvento.getDatiPagoPA().getIdCanale())))
		.andExpect(jsonPath("$._embedded.eventi[0].datiPagoPA.idDominio", is(nuovoEvento.getDatiPagoPA().getIdDominio())))
		.andExpect(jsonPath("$._embedded.eventi[0].datiPagoPA.idFlusso", is(nuovoEvento.getDatiPagoPA().getIdFlusso())))
		.andExpect(jsonPath("$._embedded.eventi[0].datiPagoPA.idIntermediario", is(nuovoEvento.getDatiPagoPA().getIdIntermediario())))
		.andExpect(jsonPath("$._embedded.eventi[0].datiPagoPA.idIntermediarioPsp", is(nuovoEvento.getDatiPagoPA().getIdIntermediarioPsp())))
		.andExpect(jsonPath("$._embedded.eventi[0].datiPagoPA.idPsp", is(nuovoEvento.getDatiPagoPA().getIdPsp())))
		.andExpect(jsonPath("$._embedded.eventi[0].datiPagoPA.idRiconciliazione", is(nuovoEvento.getDatiPagoPA().getIdRiconciliazione())))
		.andExpect(jsonPath("$._embedded.eventi[0].datiPagoPA.idStazione", is(nuovoEvento.getDatiPagoPA().getIdStazione())))
		.andExpect(jsonPath("$._embedded.eventi[0].datiPagoPA.idTracciato", is(nuovoEvento.getDatiPagoPA().getIdTracciato()), long.class))
		.andExpect(jsonPath("$._embedded.eventi[0].datiPagoPA.idModelloPagamento", is(nuovoEvento.getDatiPagoPA().getModelloPagamento())))
		.andExpect(jsonPath("$._embedded.eventi[0].datiPagoPA.idSct", is(nuovoEvento.getDatiPagoPA().getSct())))
		.andExpect(jsonPath("$._embedded.eventi[0].datiPagoPA.idTipoVersamento", is(nuovoEvento.getDatiPagoPA().getTipoVersamento())))
		
		;
		
	}
	
	@Test
	public void testLunghezzaTipoEvento() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setTipoEvento(STRING_256);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il valore presente nel campo tipoEvento non rispetta la lunghezza massima di 255 caratteri.")));
		
	}
	
	@Test
	public void testLunghezzaSottotipoEvento() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setSottotipoEvento(STRING_256);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il valore presente nel campo sottotipoEvento non rispetta la lunghezza massima di 255 caratteri.")));
	}
	
	@Test
	public void testLunghezzaSottotipoEsito() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setSottotipoEsito(STRING_256);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il valore presente nel campo sottotipoEsito non rispetta la lunghezza massima di 255 caratteri.")));
	}
	
	@Test
	public void testLunghezzaIuv() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setIuv(STRING_256);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il valore presente nel campo iuv non rispetta la lunghezza massima di 35 caratteri.")));
	}
	
	@Test
	public void testLunghezzaCcp() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setCcp(STRING_256);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il valore presente nel campo ccp non rispetta la lunghezza massima di 35 caratteri.")));
	}
	
	@Test
	public void testLunghezzaIdPagamento() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setIdPagamento(STRING_256);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il valore presente nel campo idPagamento non rispetta la lunghezza massima di 35 caratteri.")));
	}
	
	@Test
	public void testLunghezzaIdDominio() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setIdDominio(STRING_256);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il valore presente nel campo idDominio non rispetta il pattern previsto.")));
	}
	
	@Test
	public void testLunghezzaIdA2A() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setIdA2A(STRING_256);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il valore presente nel campo idA2A non rispetta il pattern previsto.")));
	}
	
	@Test
	public void testLunghezzaIdPendenza() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setIdPendenza(STRING_256);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il valore presente nel campo idPendenza non rispetta il pattern previsto.")));
	}
	
	@Test
	public void testPatternIdDominio() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setIdDominio(PATTERN_ID_DOMINIO_ERRATO);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il valore presente nel campo idDominio non rispetta il pattern previsto.")));
	}
	
	@Test
	public void testPatternIdA2A() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setIdA2A(PATTERN_ID_A2A_ERRATO);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il valore presente nel campo idA2A non rispetta il pattern previsto.")));
	}
	
	@Test
	public void testPatternIdPendenza() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setIdPendenza(PATTERN_ID_PENDENZA_ERRATO);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il valore presente nel campo idPendenza non rispetta il pattern previsto.")));
	}
	
	@Test
	public void testDurataEventoMinZero() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setDurataEvento(-1l);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il campo durataEvento deve contenere un valore >= 0.")));
	}
	
	@Test
	public void testSeveritaMinZero() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setSeverita(-1);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il campo severita deve contenere un valore >= 0.")));
	}
	
	@Test
	public void testSeveritaMagCinque() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setSeverita(6);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il campo severita deve contenere un valore <= 5.")));
	}
	
	@Test
	public void testIdTracciatoZero() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setIdTracciato(0l);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il campo idTracciato deve contenere un valore > 0.")));
	}
	
	@Test
	public void testIdFrZero() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setIdFr(0l);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il campo idFr deve contenere un valore > 0.")));
	}
	
	@Test
	public void testIdRiconciliazioneZero() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();
		nuovoEvento.setIdRiconciliazione(0l);
		
		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("Il campo idRiconciliazione deve contenere un valore > 0.")));
	}
}
