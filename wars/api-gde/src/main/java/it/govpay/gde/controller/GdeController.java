package it.govpay.gde.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;
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
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

	@Autowired
	private JpaTransactionManager txManager;

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
			@PageableDefault(size = 25, sort = { "data"}, direction = Direction.DESC)
			Pageable pageable,
			@Parameter(description="Inizio della finestra temporale di osservazione", name="dataDa") 
			@RequestParam(required = false, name = "dataDa") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  Optional<LocalDateTime> dataDa,
			@Parameter(description="Fine della finestra temporale di osservazione", name="dataA") 
			@RequestParam(required = false, name = "dataA") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  Optional<LocalDateTime> dataA,
			@Parameter(description="Identificativo del dominio beneficiario", name="idDominio") 
			@RequestParam(required = false, name = "idDominio") Optional<String> idDominio,
			@Parameter(description="Identificativo univoco di versamento", name="iuv") 
			@RequestParam(required = false, name = "iuv") Optional<String> iuv,
			@Parameter(description="Codice contesto pagamento/ID ricevuta", name="ccp") 
			@RequestParam(required = false, name = "ccp") Optional<String> ccp,
			@Parameter(description="Identificativo del gestionale proprietario della pendenza", name="idA2A") 
			@RequestParam(required = false, name = "idA2A") Optional<String> idA2A,
			@Parameter(description="Identificativo della pendenza nel gestionale proprietario", name="idPendenza") 
			@RequestParam(required = false, name = "idPendenza") Optional<String> idPendenza,
			@Parameter(description="Identificativo della richiesta di pagamento", name="idPagamento") 
			@RequestParam(required = false, name = "idPagamento") Optional<String> idPagamento,
			@Parameter(description="Filtro per categoria evento", name="categoriaEvento") 
			@RequestParam(required = false, name = "categoriaEvento") Optional<CategoriaEvento> categoriaEvento,
			@Parameter(description="Filtro per esito evento", name="esito") 
			@RequestParam(required = false, name = "esito") Optional<EsitoEvento> esitoEvento,
			@Parameter(description="Filtro per ruolo evento", name="ruolo") 
			@RequestParam(required = false, name = "ruolo") Optional<RuoloEvento> ruoloEvento,
			@Parameter(description="Filtro per sottotipo evento", name="sottotipoEvento") 
			@RequestParam(required = false, name = "sottotipoEvento") Optional<String> sottotipoEvento,
			@Parameter(description="Filtro per tipo evento", name="tipoEvento") 
			@RequestParam(required = false, name = "tipoEvento") Optional<String> tipoEvento,
			@Parameter(description="Filtro per componente evento", name="componente") 
			@RequestParam(required = false, name = "componente") Optional<ComponenteEvento> componenteEvento,
			@Parameter(description="Filtro per severita' errore", name="severitaDa") 
			@RequestParam(required = false, name = "severitaDa") Optional<
			@Min(value = 0, message = "Il parametro severitaDa deve contenere un valore >= 0.") 
			@Max(value = 5, message = "Il parametro severitaDa deve contenere un valore <= 5.") Integer> severitaDa,
			@Parameter(description="Filtro per severita' errore", name="severitaA") 
			@RequestParam(required = false, name = "severitaA") Optional<
			@Min(value = 0, message = "Il parametro severitaA deve contenere un valore >= 0.") 
			@Max(value = 5, message = "Il parametro severitaA deve contenere un valore <= 5.") Integer> severitaA
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
			this.logger.debug("Filtro Componente evento: " + componenteEvento.get());
			filters.put(SearchParam.componenteEvento, componenteEvento.get().toString());
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

		this.logger.debug("Lettura eventi...");
		try {

			return this.runTransaction(() -> {
				Page<EventoIndexEntity> findAll = this.eventoIndexRepository.findAll(pageable, filters);

				PagedModel<EventoIndexModel> collModel = this.pagedEventoAssembler.toModel(findAll, this.eventoIndexAssembler);

				return new ResponseEntity<>(collModel,HttpStatus.OK);
			});
		}catch (RuntimeException e) {
			this.logger.error("Lettura eventi completata con errore: " +e.getMessage(), e);
			throw e;
		}catch (Throwable e) {
			this.logger.error("Lettura eventi completata con errore: " +e.getMessage(), e);
			throw new InternalException(e);
		}
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
			@PathVariable("id") @Positive(message = "Id dell'evento deve essere un valore > 0.") Long id
			) {
		this.logger.debug("Lettura evento: " + id);
		try {

			return this.runTransaction(() -> {
				return this.eventoRepository.findById(id)
						.map(this.eventoAssembler::toModel)
						.map(ResponseEntity::ok) 
						.orElseThrow(() -> new ResourceNotFoundException());
			});
		}catch (RuntimeException e) {
			this.logger.error("Lettura evento completata con errore: " +e.getMessage(), e);
			throw e;
		}catch (Throwable e) {
			this.logger.error("Lettura evento completata con errore: " +e.getMessage(), e);
			throw new InternalException(e);
		}
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
	public ResponseEntity<Void> addEvento(
			@Parameter(description="Evento da salvare.", required=true, schema=@Schema(implementation = NuovoEventoModel.class))
			@Valid
			@RequestBody NuovoEventoModel evento){
		this.logger.debug("Salvataggio evento: " + evento.toString());
		try {
			EventoEntity entity = this.nuovoEventoMapper.nuovoEventoModelToEventoEntity(evento);
			
			this.runTransaction(() -> {
				this.eventoRepository.save(entity);
				this.logger.debug("Salvataggio evento completato.");
			});

			return new ResponseEntity<Void>(HttpStatus.CREATED);
		}catch (RuntimeException e) {
			this.logger.error("Salvataggio evento completato con errore: " +e.getMessage(), e);
			throw e;
		}catch (Throwable e) {
			this.logger.error("Salvataggio evento completato con errore: " +e.getMessage(), e);
			throw new InternalException(e);
		}
	}

	public void runTransaction(Runnable runnable) {

		TransactionStatus transaction = null;
		try {
			TransactionTemplate template = new TransactionTemplate(this.txManager);
			transaction = this.txManager.getTransaction(template);
			runnable.run();
			this.txManager.commit(transaction);
		} catch (Exception e) {
			if (transaction != null && !transaction.isCompleted()) {
				this.txManager.rollback(transaction);
			}
			throw new RuntimeException(e);
		}
	}

	public <T> T runTransaction(Supplier<T> supplier) {

		TransactionStatus transaction = null;
		try {
			TransactionTemplate template = new TransactionTemplate(this.txManager);
			transaction = this.txManager.getTransaction(template);
			T ret = supplier.get();
			this.txManager.commit(transaction);
			return ret;

		} catch (Exception e) {
			if (transaction != null && !transaction.isCompleted()) {
				this.txManager.rollback(transaction);
			}
			throw new RuntimeException(e);
		}
	}

}
