package it.govpay.rs.v1.exception;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import it.govpay.rs.v1.beans.FaultBean;
import it.govpay.rs.v1.beans.FaultBean.CategoriaEnum;

/***
 * CodiceEccezione
 *  
 * @author pintori
 *
 */
public class CodiceEccezione {
	
	public static final CodiceEccezione RICHIESTA_NON_VALIDA = new CodiceEccezione(400, CategoriaEnum.RICHIESTA, "Richiesta non correttamente formata");
	public static final CodiceEccezione AUTENTICAZIONE = new CodiceEccezione(401, CategoriaEnum.AUTORIZZAZIONE, "Autenticazione richiesta.");
	public static final CodiceEccezione AUTORIZZAZIONE = new CodiceEccezione(403, CategoriaEnum.AUTORIZZAZIONE, "Richiesta non autorizzata.");
	public static final CodiceEccezione NOT_FOUND = new CodiceEccezione(404, CategoriaEnum.OPERAZIONE, "Risorsa non trovata");
	public static final CodiceEccezione CONFLITTO = new CodiceEccezione(409, CategoriaEnum.OPERAZIONE, "Risorsa già presente");
	public static final CodiceEccezione PAYLOAD_TROPPO_GRANDE = new CodiceEccezione(413, CategoriaEnum.RICHIESTA, "Body della richiesta troppo grande");
	public static final CodiceEccezione SINTASSI = new CodiceEccezione(422, CategoriaEnum.RICHIESTA, "Richiesta non rispetta la sintassi prevista");
	public static final CodiceEccezione AUTORIZZAZIONE_PA = new CodiceEccezione(460, CategoriaEnum.PAGOPA,"Ente non censito sul sistema");
	public static final CodiceEccezione ERRORE_INTERNO = new CodiceEccezione(500, CategoriaEnum.INTERNO,"Errore interno");


	private CategoriaEnum categoria;
	private final String descrizione;
	private final int code;

	CodiceEccezione(int code, CategoriaEnum categoria, String descrizione)
	{
		this.code = code;
		this.descrizione = descrizione;
		this.categoria = categoria;
	}

	public int getCode() {
		return this.code;
	}
	public String getDescrizione()
	{
		return this.descrizione;
	}

	public CategoriaEnum getCategoria() {
		return categoria;
	}

	@Override
	public String toString() {
		return this.descrizione;
	}
	public FaultBean toFaultBean() {
		FaultBean faultBean = new FaultBean();
		faultBean.setCategoria(this.categoria);
		faultBean.setCodice(this.code+"");
		faultBean.setDescrizione(this.descrizione);
		return faultBean;
	}
	public FaultBean toFaultBean(String dettaglio) {
		FaultBean faultBean = this.toFaultBean();
		faultBean.setDettaglio(dettaglio);
		return faultBean;
	}
	public FaultBean toFaultBean(Exception e) {
		FaultBean faultBean = this.toFaultBean();
		faultBean.setDettaglio(e.getMessage());
		return faultBean;
	}

	public ResponseBuilder toFaultResponseBuilder() {
		return this.toFaultResponseBuilder(true);
	}
	public ResponseBuilder toFaultResponseBuilder(boolean addFaultBean) {
		FaultBean faultBean = this.toFaultBean();
		ResponseBuilder rb = Response.status(this.code);
		if(addFaultBean) {
			return setFaultBeanAsEntity(rb, faultBean);
		}
		return rb;
	}
	public ResponseBuilder toFaultResponseBuilder(String dettaglio) {
		FaultBean faultBean = this.toFaultBean(dettaglio);
		return setFaultBeanAsEntity(Response.status(this.code), faultBean).type(MediaType.APPLICATION_JSON);
	}
	public ResponseBuilder toFaultResponseBuilder(Exception e) {
		FaultBean faultBean = this.toFaultBean(e);
		return setFaultBeanAsEntity(Response.status(this.code), faultBean).type(MediaType.APPLICATION_JSON);
	}
	
	public ResponseBuilder setFaultBeanAsEntity(ResponseBuilder rb, FaultBean faultBean) {
		return rb.entity(faultBean);
	}

	public Response toFaultResponse() {
		return this.toFaultResponse(true);
	}
	public Response toFaultResponse(boolean addFaultBean) {
		return this.toFaultResponseBuilder(addFaultBean).build();
	}
	public Response toFaultResponse(String dettaglio) {
		return this.toFaultResponseBuilder(dettaglio).build();
	}
	public Response toFaultResponse(Exception e) {
		return this.toFaultResponseBuilder(e).build();
	}
	public javax.ws.rs.WebApplicationException toException(ResponseBuilder responseBuilder){
		return new javax.ws.rs.WebApplicationException(responseBuilder.build());
	}
	public javax.ws.rs.WebApplicationException toException(ResponseBuilder responseBuilder, Map<String, String> headers){
		if(headers!=null && !headers.isEmpty()) {
			headers.keySet().stream().forEach(k -> {
				responseBuilder.header(k, headers.get(k));
			});
		}
		return new javax.ws.rs.WebApplicationException(responseBuilder.build());
	}
	public javax.ws.rs.WebApplicationException toException(Response response){
		// Aggiunta eccezione nel costruttore, in modo che cxf chiami la classe WebApplicationExceptionMapper
		return new javax.ws.rs.WebApplicationException(new Exception(response.getEntity().toString()),response);
	}
	public javax.ws.rs.WebApplicationException toException(){
		return this.toException(true);
	}
	public javax.ws.rs.WebApplicationException toException(boolean addFaultBean){
		Response r = this.toFaultResponse(addFaultBean);
		return this.toException(r);
	}
	public javax.ws.rs.WebApplicationException toException(String dettaglio){
		Response r = this.toFaultResponse(dettaglio);
		return this.toException(r);
	}
	public javax.ws.rs.WebApplicationException toException(Exception e){
		Response r = this.toFaultResponse(e);
		return this.toException(r);
	}
	public void throwException(ResponseBuilder responseBuilder) throws javax.ws.rs.WebApplicationException{
		throw this.toException(responseBuilder);
	}
	public void throwException(Response response) throws javax.ws.rs.WebApplicationException{
		throw this.toException(response);
	}
	public void throwException() throws javax.ws.rs.WebApplicationException{
		throw this.toException();
	}
	public void throwException(boolean addFaultBean) throws javax.ws.rs.WebApplicationException{
		throw toException(addFaultBean);
	}
	public void throwException(String dettaglio) throws javax.ws.rs.WebApplicationException{
		throw toException(dettaglio);
	}
	public void throwException(Exception e) throws javax.ws.rs.WebApplicationException{
		throw toException(e);
	}

}
