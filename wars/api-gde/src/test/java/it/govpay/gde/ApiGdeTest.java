package it.govpay.gde;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import it.govpay.gde.model.NuovoEventoModel;
import it.govpay.gde.model.ProblemModel;

public class ApiGdeTest extends BaseApiGdeTest {

	@Test
	public void test1() throws Exception {
		mvc.perform(get("/eventi")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
		.andExpect(jsonPath("$.page.totalElements", is(0)));
	}

	@Test
	public void test2() throws Exception {
		MvcResult mvcResult = mvc.perform(get("/eventi/10000")
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andExpect(content()
				.contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andReturn();
		
		ProblemModel problem = this.mapFromJson(mvcResult.getResponse().getContentAsString() , ProblemModel.class);
		assertEquals(mvcResult.getResponse().getStatus(), problem.getStatus());
	}
	
	@Test
	public void test3() throws Exception {
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
		.andExpect(jsonPath("$.page.totalElements", is(1)))
		.andExpect(jsonPath("$.page.size", is(25)))
		.andExpect(jsonPath("$.page.totalPages", is(1)))
		.andExpect(jsonPath("$.page.number", is(0)))
		.andExpect(jsonPath("$._embedded.eventi", hasSize(1)))
		.andExpect(jsonPath("$._embedded.eventi[0].categoriaEvento", is(nuovoEvento.getCategoriaEvento().getValue())))
		.andExpect(jsonPath("$._embedded.eventi[0].ruolo", is(nuovoEvento.getRuolo().getValue())))
		.andExpect(jsonPath("$._embedded.eventi[0].componente", is(nuovoEvento.getComponente().getValue())))
		.andExpect(jsonPath("$._embedded.eventi[0].durataEvento", is(nuovoEvento.getDurataEvento()), long.class))
		.andExpect(jsonPath("$._embedded.eventi[0].tipoEvento", is(nuovoEvento.getTipoEvento())))
		.andExpect(jsonPath("$._embedded.eventi[0].sottotipoEvento", is(nuovoEvento.getSottotipoEvento())))
		.andExpect(jsonPath("$._embedded.eventi[0].esito", is(nuovoEvento.getEsito().getValue())));
		
	}
}
