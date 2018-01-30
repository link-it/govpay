
package it.govpay.rs.v1.beans;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.UriBuilder;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.annotate.JsonValue;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.utils.SimpleDateFormatUtils;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@JsonPropertyOrder({
    "id",
    "idSessionePortale",
    "idSessionePsp",
    "nome",
    "stato",
    "pspRedirectUrl",
    "dataRichiestaPagamento",
    "datiAddebito",
    "dataEsecuzionePagamento",
    "credenzialiPagatore",
    "soggettoVersante",
    "autenticazioneSoggetto",
    "canale",
    "pendenze",
    "rpts"
})
public class PagamentoPortale extends JSONSerializable {

    /**
     * Identificativo del pagamento assegnato da GovPay
     * (Required)
     * 
     */
    @JsonProperty("id")
    private String id;
    /**
     * Identificativo del pagamento assegnato dal portale chiamante
     * 
     */
    @JsonProperty("idSessionePortale")
    private String idSessionePortale;
    /**
     * Identificativo del pagamento assegnato dal psp utilizzato
     * 
     */
    @JsonProperty("idSessionePsp")
    private String idSessionePsp;
    /**
     * Nome del pagamento da visualizzare all'utente.
     * 
     */
    @JsonProperty("nome")
    private String nome;
    /**
     * Stato del pagamento
     * (Required)
     * 
     */
    @JsonProperty("stato")
    private PagamentoPortale.Stato stato;
    /**
     * Url di redirect al psp inviata al versante per perfezionare il pagamento, se previsto dal modello
     * 
     */
    @JsonProperty("pspRedirectUrl")
    private String pspRedirectUrl;
    /**
     * Data e ora di avvio del pagamento
     * (Required)
     * 
     */
    @JsonProperty("dataRichiestaPagamento")
    private String dataRichiestaPagamento;
    /**
     * Dati necessari alla realizzazione dei pagamenti per Addebito Diretto, se previsto dal profilo del versante.
     * 
     */
    @JsonProperty("datiAddebito")
    private DatiAddebito datiAddebito;
    /**
     * data in cui si richiede che venga effettuato il pagamento, se diversa dalla data corrente.
     * 
     */
    @JsonProperty("dataEsecuzionePagamento")
    private String dataEsecuzionePagamento;
    /**
     * Eventuali credenziali richieste dal PSP necessarie per completare l'operazione (ad esempio un codice bilaterale utilizzabile una sola volta).
     * 
     */
    @JsonProperty("credenzialiPagatore")
    private String credenzialiPagatore;
    @JsonProperty("soggettoVersante")
    private SoggettoVersante soggettoVersante;
    /**
     * modalita' di autenticazione del soggetto versante
     * 
     */
    @JsonProperty("autenticazioneSoggetto")
    private PagamentoPortale.AutenticazioneSoggetto autenticazioneSoggetto;
    /**
     * Url per il canale di pagamento utilizzato
     * 
     */
    @JsonProperty("canale")
    private String canale;
    /**
     * Url per le pendenze oggetto del Pagamento
     * 
     */
    @JsonProperty("pendenze")
    private String pendenze;
    /**
     * Url oer le richieste di pagamento oggetto del Pagamento
     * 
     */
    @JsonProperty("rpts")
    private String rpts;

    /**
     * Identificativo del pagamento assegnato da GovPay
     * (Required)
     * 
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * Identificativo del pagamento assegnato da GovPay
     * (Required)
     * 
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Identificativo del pagamento assegnato dal portale chiamante
     * 
     */
    @JsonProperty("idSessionePortale")
    public String getIdSessionePortale() {
        return idSessionePortale;
    }

    /**
     * Identificativo del pagamento assegnato dal portale chiamante
     * 
     */
    @JsonProperty("idSessionePortale")
    public void setIdSessionePortale(String idSessionePortale) {
        this.idSessionePortale = idSessionePortale;
    }

    /**
     * Identificativo del pagamento assegnato dal psp utilizzato
     * 
     */
    @JsonProperty("idSessionePsp")
    public String getIdSessionePsp() {
        return idSessionePsp;
    }

    /**
     * Identificativo del pagamento assegnato dal psp utilizzato
     * 
     */
    @JsonProperty("idSessionePsp")
    public void setIdSessionePsp(String idSessionePsp) {
        this.idSessionePsp = idSessionePsp;
    }

    /**
     * Nome del pagamento da visualizzare all'utente.
     * 
     */
    @JsonProperty("nome")
    public String getNome() {
        return nome;
    }

    /**
     * Nome del pagamento da visualizzare all'utente.
     * 
     */
    @JsonProperty("nome")
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Stato del pagamento
     * (Required)
     * 
     */
    @JsonProperty("stato")
    public PagamentoPortale.Stato getStato() {
        return stato;
    }

    /**
     * Stato del pagamento
     * (Required)
     * 
     */
    @JsonProperty("stato")
    public void setStato(PagamentoPortale.Stato stato) {
        this.stato = stato;
    }

    /**
     * Url di redirect al psp inviata al versante per perfezionare il pagamento, se previsto dal modello
     * 
     */
    @JsonProperty("pspRedirectUrl")
    public String getPspRedirectUrl() {
        return pspRedirectUrl;
    }

    /**
     * Url di redirect al psp inviata al versante per perfezionare il pagamento, se previsto dal modello
     * 
     */
    @JsonProperty("pspRedirectUrl")
    public void setPspRedirectUrl(String pspRedirectUrl) {
        this.pspRedirectUrl = pspRedirectUrl;
    }

    /**
     * Data e ora di avvio del pagamento
     * (Required)
     * 
     */
    @JsonProperty("dataRichiestaPagamento")
    public String getDataRichiestaPagamento() {
        return dataRichiestaPagamento;
    }

    /**
     * Data e ora di avvio del pagamento
     * (Required)
     * 
     */
    @JsonProperty("dataRichiestaPagamento")
    public void setDataRichiestaPagamento(String dataRichiestaPagamento) {
        this.dataRichiestaPagamento = dataRichiestaPagamento;
    }

    /**
     * Dati necessari alla realizzazione dei pagamenti per Addebito Diretto, se previsto dal profilo del versante.
     * 
     */
    @JsonProperty("datiAddebito")
    public DatiAddebito getDatiAddebito() {
        return datiAddebito;
    }

    /**
     * Dati necessari alla realizzazione dei pagamenti per Addebito Diretto, se previsto dal profilo del versante.
     * 
     */
    @JsonProperty("datiAddebito")
    public void setDatiAddebito(DatiAddebito datiAddebito) {
        this.datiAddebito = datiAddebito;
    }

    /**
     * data in cui si richiede che venga effettuato il pagamento, se diversa dalla data corrente.
     * 
     */
    @JsonProperty("dataEsecuzionePagamento")
    public String getDataEsecuzionePagamento() {
        return dataEsecuzionePagamento;
    }

    /**
     * data in cui si richiede che venga effettuato il pagamento, se diversa dalla data corrente.
     * 
     */
    @JsonProperty("dataEsecuzionePagamento")
    public void setDataEsecuzionePagamento(String dataEsecuzionePagamento) {
        this.dataEsecuzionePagamento = dataEsecuzionePagamento;
    }

    /**
     * Eventuali credenziali richieste dal PSP necessarie per completare l'operazione (ad esempio un codice bilaterale utilizzabile una sola volta).
     * 
     */
    @JsonProperty("credenzialiPagatore")
    public String getCredenzialiPagatore() {
        return credenzialiPagatore;
    }

    /**
     * Eventuali credenziali richieste dal PSP necessarie per completare l'operazione (ad esempio un codice bilaterale utilizzabile una sola volta).
     * 
     */
    @JsonProperty("credenzialiPagatore")
    public void setCredenzialiPagatore(String credenzialiPagatore) {
        this.credenzialiPagatore = credenzialiPagatore;
    }

    @JsonProperty("soggettoVersante")
    public SoggettoVersante getSoggettoVersante() {
        return soggettoVersante;
    }

    @JsonProperty("soggettoVersante")
    public void setSoggettoVersante(SoggettoVersante soggettoVersante) {
        this.soggettoVersante = soggettoVersante;
    }

    /**
     * modalita' di autenticazione del soggetto versante
     * 
     */
    @JsonProperty("autenticazioneSoggetto")
    public PagamentoPortale.AutenticazioneSoggetto getAutenticazioneSoggetto() {
        return autenticazioneSoggetto;
    }

    /**
     * modalita' di autenticazione del soggetto versante
     * 
     */
    @JsonProperty("autenticazioneSoggetto")
    public void setAutenticazioneSoggetto(PagamentoPortale.AutenticazioneSoggetto autenticazioneSoggetto) {
        this.autenticazioneSoggetto = autenticazioneSoggetto;
    }

    /**
     * Url per il canale di pagamento utilizzato
     * 
     */
    @JsonProperty("canale")
    public String getCanale() {
        return canale;
    }

    /**
     * Url per il canale di pagamento utilizzato
     * 
     */
    @JsonProperty("canale")
    public void setCanale(String canale) {
        this.canale = canale;
    }

    /**
     * Url per le pendenze oggetto del Pagamento
     * 
     */
    @JsonProperty("pendenze")
    public String getPendenze() {
        return pendenze;
    }

    /**
     * Url per le pendenze oggetto del Pagamento
     * 
     */
    @JsonProperty("pendenze")
    public void setPendenze(String pendenze) {
        this.pendenze = pendenze;
    }

    /**
     * Url oer le richieste di pagamento oggetto del Pagamento
     * 
     */
    @JsonProperty("rpts")
    public String getRpts() {
        return rpts;
    }

    /**
     * Url oer le richieste di pagamento oggetto del Pagamento
     * 
     */
    @JsonProperty("rpts")
    public void setRpts(String rpts) {
        this.rpts = rpts;
    }

    public enum AutenticazioneSoggetto {

        CNS("CNS"),
        USR("USR"),
        OTH("OTH"),
        N_A("N/A");
        private final String value;
        private final static Map<String, PagamentoPortale.AutenticazioneSoggetto> CONSTANTS = new HashMap<String, PagamentoPortale.AutenticazioneSoggetto>();

        static {
            for (PagamentoPortale.AutenticazioneSoggetto c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private AutenticazioneSoggetto(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static PagamentoPortale.AutenticazioneSoggetto fromValue(String value) {
            PagamentoPortale.AutenticazioneSoggetto constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum Stato {

        DA_REDIRIGERE_AL_WISP("Da redirigere al WISP"),
        SELEZIONE_WISP_IN_CORSO("Selezione WISP in corso"),
        SELEZIONE_WISP_FALLITA("Selezione WISP fallita"),
        SELEZIONE_WISP_TIMEOUT("Selezione WISP timeout"),
        SELEZIONE_WISP_ANNULLATA("Selezione WISP annullata"),
        PAGAMENTO_IN_CORSO_AL_PSP("Pagamento in corso al PSP"),
        PAGAMENTO_IN_ATTESA_DI_ESITO("Pagamento in attesa di esito"),
        PAGAMENTO_ESEGUITO("Pagamento eseguito"),
        PAGAMENTO_NON_ESEGUITO("Pagamento non eseguito"),
        PAGAMENTO_PARZIALMENTE_ESEGUITO("Pagamento parzialmente eseguito"),
        PAGAMENTO_IN_ERRORE("Pagamento in errore");
        private final String value;
        private final static Map<String, PagamentoPortale.Stato> CONSTANTS = new HashMap<String, PagamentoPortale.Stato>();

        static {
            for (PagamentoPortale.Stato c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        private Stato(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        @JsonValue
        public String value() {
            return this.value;
        }

        @JsonCreator
        public static PagamentoPortale.Stato fromValue(String value) {
            PagamentoPortale.Stato constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }
    
	private static JsonConfig jsonConfig = new JsonConfig();

	static {
		jsonConfig.setRootClass(PagamentoPortale.class);
	}
	public PagamentoPortale() {}
	
	@Override
	public String getJsonIdFilter() {
		return "pagamentiPortale";
	}
	
	public static PagamentoPortale parse(String json) {
		JSONObject jsonObject = JSONObject.fromObject( json );  
		return (PagamentoPortale) JSONObject.toBean( jsonObject, jsonConfig );
	}
	
	
	public PagamentoPortale(it.govpay.bd.model.PagamentoPortale pagamentoPortale, UriBuilder uriBuilder) throws ServiceException {
		
		JSONObject jsonObjectPagamentiPortaleRequest = JSONObject.fromObject( pagamentoPortale.getJsonRequest() );  

		this.id = pagamentoPortale.getIdSessione();
		this.idSessionePortale = pagamentoPortale.getIdSessionePortale();
		this.idSessionePsp = pagamentoPortale.getIdSessionePsp();
		this.nome = pagamentoPortale.getNome();
		this.stato = Stato.fromValue(pagamentoPortale.getStato().toString());
		this.pspRedirectUrl = pagamentoPortale.getPspRedirectUrl();
		this.dataRichiestaPagamento = SimpleDateFormatUtils.newSimpleDateFormat().format(pagamentoPortale.getDataRichiesta());
		this.datiAddebito = DatiAddebito.parse(jsonObjectPagamentiPortaleRequest.getString("datiAddebito"));

		this.dataEsecuzionePagamento = jsonObjectPagamentiPortaleRequest.getString("dataEsecuzionePagamento");

		this.credenzialiPagatore =jsonObjectPagamentiPortaleRequest.getString("credenzialiPagatore");
		this.soggettoVersante = SoggettoVersante.parse(jsonObjectPagamentiPortaleRequest.getString("soggettoVersante"));
		this.autenticazioneSoggetto = AutenticazioneSoggetto.fromValue(jsonObjectPagamentiPortaleRequest.getString("autenticazioneSoggetto"));
		this.canale = uriBuilder.clone().path("psp").path(pagamentoPortale.getCodPsp()).path("canali").path(pagamentoPortale.getCodCanale()).toString();
		this.pendenze = uriBuilder.clone().path("pendenze").queryParam("idPagamento", pagamentoPortale.getIdSessione()).toString();
		this.rpts = uriBuilder.clone().path("rpts").queryParam("idPagamento", pagamentoPortale.getIdSessione()).toString();

	}


}
