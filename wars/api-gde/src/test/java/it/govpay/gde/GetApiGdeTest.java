package it.govpay.gde;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import it.govpay.gde.model.NuovoEventoModel;

public class GetApiGdeTest extends BaseApiGdeTest {

	@Test
	public void testGetOK() throws Exception {
		NuovoEventoModel nuovoEvento = buildNuovoEventoOK();

		mvc.perform(post("/eventi")
				.content(this.mapToJson(nuovoEvento))
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isCreated());

		MvcResult mvcResult = mvc.perform(get("/eventi"))
				.andExpect(status().isOk())
				.andExpect(content()
						.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$._embedded.eventi[0].categoriaEvento", is(nuovoEvento.getCategoriaEvento().getValue())))
				.andExpect(jsonPath("$._embedded.eventi[0].ruolo", is(nuovoEvento.getRuolo().getValue())))
				.andExpect(jsonPath("$._embedded.eventi[0].componente", is(nuovoEvento.getComponente().getValue())))
				.andExpect(jsonPath("$._embedded.eventi[0].durataEvento", is(nuovoEvento.getDurataEvento()), long.class))
				.andExpect(jsonPath("$._embedded.eventi[0].tipoEvento", is(nuovoEvento.getTipoEvento())))
				.andExpect(jsonPath("$._embedded.eventi[0].sottotipoEvento", is(nuovoEvento.getSottotipoEvento())))
				.andExpect(jsonPath("$._embedded.eventi[0].esito", is(nuovoEvento.getEsito().getValue()))).andReturn();

		Long idEvento = leggiIdUltimoEvento(mvcResult); 

		mvc.perform(get("/eventi/" + idEvento))
		.andExpect(status().isOk())
		.andExpect(content()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.id", is(idEvento), long.class))
		.andExpect(jsonPath("$.categoriaEvento", is(nuovoEvento.getCategoriaEvento().getValue())))
		.andExpect(jsonPath("$.ruolo", is(nuovoEvento.getRuolo().getValue())))
		.andExpect(jsonPath("$.componente", is(nuovoEvento.getComponente().getValue())))
		.andExpect(jsonPath("$.durataEvento", is(nuovoEvento.getDurataEvento()), long.class))
		.andExpect(jsonPath("$.tipoEvento", is(nuovoEvento.getTipoEvento())))
		.andExpect(jsonPath("$.sottotipoEvento", is(nuovoEvento.getSottotipoEvento())))
		.andExpect(jsonPath("$.esito", is(nuovoEvento.getEsito().getValue()))).andReturn();
	}

	@Test
	public void testGetNotFound() throws Exception {

		Long idEvento = 100000l;
		mvc.perform(get("/eventi/" + idEvento))
		.andExpect(status().isNotFound())
		.andExpect(jsonPath("$.title", is("Not Found")))
		.andExpect(jsonPath("$.status", is(404)))
		.andExpect(jsonPath("$.detail", is("La risorsa /eventi/"+idEvento+" non esiste.")));
	}

	@Test
	public void testGetIdNonValido() throws Exception {
		Long idEvento = -1l;
		mvc.perform(get("/eventi/" + idEvento))
		.andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.title", is("Bad Request")))
		.andExpect(jsonPath("$.status", is(400)))
		.andExpect(jsonPath("$.detail", is("getEventoById.id: Id dell'evento deve essere un valore > 0.")));
	}
}
