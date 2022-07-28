package it.govpay.gde.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import it.govpay.gde.assemblers.EventoIndexModelAssembler;
import it.govpay.gde.assemblers.EventoModelAssembler;
import it.govpay.gde.entity.EventoEntity;
import it.govpay.gde.entity.EventoIndexEntity;
import it.govpay.gde.exception.InternalException;
import it.govpay.gde.exception.ResourceNotFoundException;
import it.govpay.gde.mapper.NuovoEventoMapper;
import it.govpay.gde.model.CategoriaEvento;
import it.govpay.gde.model.ComponenteEvento;
import it.govpay.gde.model.EsitoEvento;
import it.govpay.gde.model.EventoIndexModel;
import it.govpay.gde.model.EventoModel;
import it.govpay.gde.model.NuovoEventoModel;
import it.govpay.gde.model.ProblemModel;
import it.govpay.gde.model.RuoloEvento;
import it.govpay.gde.repository.EventoIndexRepository;
import it.govpay.gde.repository.EventoIndexRepositoryImpl.SearchParam;
import it.govpay.gde.repository.EventoRepository;

@RestController
@Validated
public class GdeController {
	
	private Logger logger = LoggerFactory.getLogger(GdeController.class);

	@Autowired
	private EventoIndexRepository eventoIndexRepository;

	@Autowired
	private EventoRepository eventoRepository;

	@Autowired
	private EventoModelAssembler eventoAssembler;

	@Autowired
	private EventoIndexModelAssembler eventoIndexAssembler;

	@Autowired
	private PagedResourcesAssembler<EventoIndexEntity> pagedEventoAssembler;
	
	@Autowired
	private NuovoEventoMapper nuovoEventoMapper;

	@Operation(summary = "Ricerca eventi", description = "Ricerca eventi", tags = { "eventi" })
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Lista eventi", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PagedModel.class, subTypes = {EventoIndexModel.class}))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class)))
	})
	@GetMapping(path = "/eventi", produces = { MediaType.APPLICATION_JSON_VALUE} , name = "findEventi")
	public ResponseEntity<PagedModel<EventoIndexModel>> findEventi(
			@Parameter(description="informazioni di paginazione e ordinamento") 
			Pageable pageable,
			@Parameter(description="Inizio della finestra temporale di osservazione", name="dataDa") 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Optional<LocalDateTime> dataDa,
			@Parameter(description="Fine della finestra temporale di osservazione", name="dataA") 
			@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Optional<LocalDateTime> dataA,
			@Parameter(description="Identificativo del dominio beneficiario", name="idDominio") 
			@RequestParam(required = false) Optional<String> idDominio,
			@Parameter(description="Identificativo univoco di versamento", name="iuv") 
			@RequestParam(required = false) Optional<String> iuv,
			@Parameter(description="Codice contesto pagamento/ID ricevuta", name="ccp") 
			@RequestParam(required = false) Optional<String> ccp,
			@Parameter(description="Identificativo del gestionale proprietario della pendenza", name="idA2A") 
			@RequestParam(required = false) Optional<String> idA2A,
			@Parameter(description="Identificativo della pendenza nel gestionale proprietario", name="idPendenza") 
			@RequestParam(required = false) Optional<String> idPendenza,
			@Parameter(description="Identificativo della richiesta di pagamento", name="idPagamento") 
			@RequestParam(required = false) Optional<String> idPagamento,
			@Parameter(description="Filtro per categoria evento", name="categoria") 
			@RequestParam(required = false) Optional<CategoriaEvento> categoriaEvento,
			@Parameter(description="Filtro per esito evento", name="esito") 
			@RequestParam(required = false) Optional<EsitoEvento> esitoEvento,
			@Parameter(description="Filtro per ruolo evento", name="ruolo") 
			@RequestParam(required = false) Optional<RuoloEvento> ruoloEvento,
			@Parameter(description="Filtro per sottotipo evento", name="sottotipo") 
			@RequestParam(required = false) Optional<String> sottotipoEvento,
			@Parameter(description="Filtro per tipo evento", name="tipo") 
			@RequestParam(required = false) Optional<String> tipoEvento,
			@Parameter(description="Filtro per componente evento", name="componente") 
			@RequestParam(required = false) Optional<ComponenteEvento> componenteEvento,
			@Parameter(description="Filtro per severita' errore", name="severitaDa") 
			@RequestParam(required = false) Optional<Integer> severitaDa,
			@Parameter(description="Filtro per severita' errore", name="severitaA") 
			@RequestParam(required = false) Optional<Integer> severitaA
			){
		Map<SearchParam, Object> filters = new HashMap<>();

		if(dataDa.isPresent()) {
			filters.put(SearchParam.dataDa, dataDa.get());
		}
		if(dataA.isPresent()) {
			filters.put(SearchParam.dataA, dataA.get());
		}
		if(idDominio.isPresent()) {
			filters.put(SearchParam.idDominio, idDominio.get());
		}
		if(iuv.isPresent()) {
			filters.put(SearchParam.iuv, iuv.get());
		}
		if(ccp.isPresent()) {
			filters.put(SearchParam.ccp, ccp.get());
		}
		if(idA2A.isPresent()) {
			filters.put(SearchParam.idA2A, idA2A.get());
		}
		if(idPendenza.isPresent()) {
			filters.put(SearchParam.idPendenza, idPendenza.get());
		}
		if(idPagamento.isPresent()) {
			filters.put(SearchParam.idPagamento, idPagamento.get());
		}
		if(severitaDa.isPresent()) {
			filters.put(SearchParam.severitaDa, severitaDa.get());
		}
		if(severitaA.isPresent()) {
			filters.put(SearchParam.severitaA, severitaA.get());
		}
		if(tipoEvento.isPresent()) {
			filters.put(SearchParam.tipoEvento, tipoEvento.get());
		}
		if(sottotipoEvento.isPresent()) {
			filters.put(SearchParam.sottotipoEvento, sottotipoEvento.get());
		}
		if(componenteEvento.isPresent()) {
			filters.put(SearchParam.componenteEvento, componenteEvento.get());
		}
		if(categoriaEvento.isPresent()) {
			switch (categoriaEvento.get()) {
			case INTERFACCIA:
				filters.put(SearchParam.categoriaEvento, it.govpay.gde.entity.EventoEntity.CategoriaEvento.I);
				break;
			case INTERNO:
				filters.put(SearchParam.categoriaEvento, it.govpay.gde.entity.EventoEntity.CategoriaEvento.B);
				break;
			case UTENTE:
				filters.put(SearchParam.categoriaEvento, it.govpay.gde.entity.EventoEntity.CategoriaEvento.U);
				break;
			}
		}
		if(esitoEvento.isPresent()) {
			switch (esitoEvento.get()) {
			case FAIL:
				filters.put(SearchParam.esitoEvento, it.govpay.gde.entity.EventoEntity.EsitoEvento.FAIL);
				break;
			case KO:
				filters.put(SearchParam.esitoEvento, it.govpay.gde.entity.EventoEntity.EsitoEvento.KO);
				break;
			case OK:
				filters.put(SearchParam.esitoEvento, it.govpay.gde.entity.EventoEntity.EsitoEvento.OK);
				break;
			}
		}
		if(ruoloEvento.isPresent()) {
			switch (ruoloEvento.get()) {
			case CLIENT:
				filters.put(SearchParam.ruoloEvento, it.govpay.gde.entity.EventoEntity.RuoloEvento.C);
				break;
			case SERVER:
				filters.put(SearchParam.ruoloEvento, it.govpay.gde.entity.EventoEntity.RuoloEvento.S);
				break;
			}
		}

		Page<EventoIndexEntity> findAll = this.eventoIndexRepository.findAll(pageable, filters);

		PagedModel<EventoIndexModel> collModel = this.pagedEventoAssembler.toModel(findAll, this.eventoIndexAssembler);

		return new ResponseEntity<>(collModel,HttpStatus.OK);
	}

	@Operation(summary = "Dettaglio di un evento", description = "Dettaglio di un evento", tags = { "eventi" })
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Dettaglio Evento", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = EventoModel.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class))),
			@ApiResponse(responseCode = "404", description = "Not Found", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class)))
	})
	@GetMapping(path = "/eventi/{id}", produces = { MediaType.APPLICATION_JSON_VALUE} , name = "getEvento")
	public ResponseEntity<EventoModel> getEventoById(
			@Parameter(description="Id dell'evento da leggere.", required=true)
			@PathVariable("id") @Positive Long id
			) {
		return this.eventoRepository.findById(id)
				.map(this.eventoAssembler::toModel)
				.map(ResponseEntity::ok) 
				.orElseThrow(() -> new ResourceNotFoundException());
	}

	@Operation(summary = "Salvataggio di un nuovo evento", description = "Salvataggio di un nuovo evento", tags = { "eventi" })
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "201", description = "Evento salvato con successo"),
			@ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class))),
			@ApiResponse(responseCode = "403", description = "Forbidden", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class))),
			@ApiResponse(responseCode = "422", description = "Unprocessable Entity", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ProblemModel.class)))
	})
	@PostMapping(path = "/eventi", consumes = { MediaType.APPLICATION_JSON_VALUE} , name = "addEvento")
	public ResponseEntity<?> addEvento(
			@Parameter(description="Evento da salvare.", required=true, schema=@Schema(implementation = NuovoEventoModel.class))
			@Valid
			@RequestBody NuovoEventoModel evento){
		this.logger.debug("AAAAAA Salvataggio evento: " + evento.toString());
		try {
			EventoEntity entity = this.nuovoEventoMapper.nuovoEventoModelToEventoEntity(evento);
			
			this.eventoRepository.save(entity);

			this.logger.debug("AAAAAA Salvataggio evento completato.");
			return new ResponseEntity<>(HttpStatus.CREATED);
		}catch (Exception e) {
			this.logger.error("AAAAAA Salvataggio evento completato con errore: " +e.getMessage(), e);
			throw new InternalException(e);
		}
	}
}
