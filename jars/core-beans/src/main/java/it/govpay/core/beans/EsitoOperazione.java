package it.govpay.core.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for esitoOperazione.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="esitoOperazione"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="OK"/&gt;
 *     &lt;enumeration value="INTERNAL"/&gt;
 *     &lt;enumeration value="AUT_000"/&gt;
 *     &lt;enumeration value="AUT_001"/&gt;
 *     &lt;enumeration value="AUT_002"/&gt;
 *     &lt;enumeration value="APP_000"/&gt;
 *     &lt;enumeration value="APP_001"/&gt;
 *     &lt;enumeration value="APP_002"/&gt;
 *     &lt;enumeration value="DOM_000"/&gt;
 *     &lt;enumeration value="DOM_001"/&gt;
 *     &lt;enumeration value="DOM_002"/&gt;
 *     &lt;enumeration value="DOM_003"/&gt;
 *     &lt;enumeration value="NDP_000"/&gt;
 *     &lt;enumeration value="NDP_001"/&gt;
 *     &lt;enumeration value="PAG_000"/&gt;
 *     &lt;enumeration value="PAG_001"/&gt;
 *     &lt;enumeration value="PAG_002"/&gt;
 *     &lt;enumeration value="PAG_003"/&gt;
 *     &lt;enumeration value="PAG_004"/&gt;
 *     &lt;enumeration value="PAG_005"/&gt;
 *     &lt;enumeration value="PAG_006"/&gt;
 *     &lt;enumeration value="PAG_007"/&gt;
 *     &lt;enumeration value="PAG_008"/&gt;
 *     &lt;enumeration value="PAG_009"/&gt;
 *     &lt;enumeration value="PAG_010"/&gt;
 *     &lt;enumeration value="PAG_011"/&gt;
 *     &lt;enumeration value="PRT_000"/&gt;
 *     &lt;enumeration value="PRT_001"/&gt;
 *     &lt;enumeration value="PRT_002"/&gt;
 *     &lt;enumeration value="PRT_003"/&gt;
 *     &lt;enumeration value="PRT_004"/&gt;
 *     &lt;enumeration value="PRT_005"/&gt;
 *     &lt;enumeration value="PSP_000"/&gt;
 *     &lt;enumeration value="PSP_001"/&gt;
 *     &lt;enumeration value="RND_000"/&gt;
 *     &lt;enumeration value="RND_001"/&gt;
 *     &lt;enumeration value="STA_000"/&gt;
 *     &lt;enumeration value="STA_001"/&gt;
 *     &lt;enumeration value="TRB_000"/&gt;
 *     &lt;enumeration value="UOP_000"/&gt;
 *     &lt;enumeration value="UOP_001"/&gt;
 *     &lt;enumeration value="VER_000"/&gt;
 *     &lt;enumeration value="VER_001"/&gt;
 *     &lt;enumeration value="VER_002"/&gt;
 *     &lt;enumeration value="VER_003"/&gt;
 *     &lt;enumeration value="VER_004"/&gt;
 *     &lt;enumeration value="VER_005"/&gt;
 *     &lt;enumeration value="VER_006"/&gt;
 *     &lt;enumeration value="VER_007"/&gt;
 *     &lt;enumeration value="VER_008"/&gt;
 *     &lt;enumeration value="VER_009"/&gt;
 *     &lt;enumeration value="VER_010"/&gt;
 *     &lt;enumeration value="VER_011"/&gt;
 *     &lt;enumeration value="VER_012"/&gt;
 *     &lt;enumeration value="VER_013"/&gt;
 *     &lt;enumeration value="VER_014"/&gt;
 *     &lt;enumeration value="VER_015"/&gt;
 *     &lt;enumeration value="VER_016"/&gt;
 *     &lt;enumeration value="VER_017"/&gt;
 *     &lt;enumeration value="VER_018"/&gt;
 *     &lt;enumeration value="VER_019"/&gt;
 *     &lt;enumeration value="VER_020"/&gt;
 *     &lt;enumeration value="VER_021"/&gt;
 *     &lt;enumeration value="VER_022"/&gt;
 *     &lt;enumeration value="VER_023"/&gt;
 *     &lt;enumeration value="WISP_000"/&gt;
 *     &lt;enumeration value="WISP_001"/&gt;
 *     &lt;enumeration value="WISP_002"/&gt;
 *     &lt;enumeration value="WISP_003"/&gt;
 *     &lt;enumeration value="WISP_004"/&gt;
 *     &lt;enumeration value="APP_003"/&gt;
 *     &lt;enumeration value="APP_004"/&gt;
 *     &lt;enumeration value="APP_005"/&gt;
 *     &lt;enumeration value="VER_024"/&gt;
 *     &lt;enumeration value="VER_025"/&gt;
 *     &lt;enumeration value="VER_026"/&gt;
 *     &lt;enumeration value="VER_027"/&gt;
 *     &lt;enumeration value="VER_028"/&gt;
 *     &lt;enumeration value="VER_029"/&gt;
 *     &lt;enumeration value="VER_030"/&gt;
 *     &lt;enumeration value="CIT_001"/&gt;
 *     &lt;enumeration value="CIT_002"/&gt;
 *     &lt;enumeration value="CIT_003"/&gt;
 *     &lt;enumeration value="CIT_004"/&gt;
 *     &lt;enumeration value="PAG_014"/&gt;
 *     &lt;enumeration value="UAN_001"/&gt;
 *     &lt;enumeration value="UAN_002"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "esitoOperazione")
@XmlEnum
public enum EsitoOperazione {

    OK,

    /**
     * Errore non atteso
     * 
     */
    INTERNAL,

    /**
     * Principal non fornito
     * 
     */
    AUT_000,

    /**
     * Principal non associato ad alcuna Applicazione
     * 
     */
    AUT_001,

    /**
     * Principal non associato ad alcun Portale
     * 
     */
    AUT_002,

    /**
     * Applicazione inesistente
     * 
     */
    APP_000,

    /**
     * Applicazione disabilitata
     * 
     */
    APP_001,

    /**
     * Applicazione autenticata diversa dalla chiamante
     * 
     */
    APP_002,

    /**
     * Dominio inesistente
     * 
     */
    DOM_000,

    /**
     * Dominio disabilitato
     * 
     */
    DOM_001,

    /**
     * Dominio configurato per la generazione custom degli iuv
     * 
     */
    DOM_002,

    /**
     * Dominio configurato per la generazione centralizzata degli iuv
     * 
     */
    DOM_003,

    /**
     * Errore di comunicazione con il Nodo dei Pagamenti
     * 
     */
    NDP_000,

    /**
     * Ricevuto FAULT dal Nodo dei Pagamenti
     * 
     */
    NDP_001,

    /**
     * I versamenti di una richiesta di pagamento devono afferire alla stessa stazione intermediario
     * 
     */
    PAG_000,

    /**
     * Il canale indicato non supporta il pagamento di piu' versamenti
     * 
     */
    PAG_001,

    /**
     * Il canale indicato non puo' eseguire pagamenti ad iniziativa Ente
     * 
     */
    PAG_002,

    /**
     * Il canale indicato non supporta il pagamento di Marca da Bollo Telematica
     * 
     */
    PAG_003,

    /**
     * Il tipo di pagamento Addebito Diretto richiede di specificare un Iban di Addebito
     * 
     */
    PAG_004,

    /**
     * Il tipo di pagamento On-line Banking e-Payment (OBEP) consente il pagamento di versamenti con al piu' un singolo versamento
     * 
     */
    PAG_005,

    /**
     * Non è possibile pagare un versamento in stato diverso da NON_ESEGUITO
     * 
     */
    PAG_006,

    /**
     * Non è possibile pagare un versamento scaduto
     * 
     */
    PAG_007,

    /**
     * Transazione di pagamento inesistente
     * 
     */
    PAG_008,

    /**
     * Pagamento gia' stornato
     * 
     */
    PAG_009,

    /**
     * Richiesta di storno inesistente
     * 
     */
    PAG_010,
    
    /**
     * Nessun pagamento da stornare
     * 
     */
    PAG_011,
    /**
     * Pagamento con data validita decorsa
     * 
     */
    PAG_012,
    
    /**
     * Utilizzo del gateway senza URL di ritorno
     * 
     */
    PAG_013,

    /**
     * Portale inesistente
     * 
     */
    PRT_000,

    /**
     * Portale disabilitato
     * 
     */
    PRT_001,

    /**
     * Portale autenticato diverso dal chiamante
     * 
     */
    PRT_002,

    /**
     * Portale non autorizzato a pagare i versamenti dell'applicazione indicata
     * 
     */
    PRT_003,

    /**
     * Portale non autorizzato a visualizzare l'esito della transazione indicata
     * 
     */
    PRT_004,

    /**
     * Portale non autorizzato all'operazione richiesta
     * 
     */
    PRT_005,

    /**
     * Il canale non esiste
     * 
     */
    PSP_000,

    /**
     * Il canale e' disabilitato
     * 
     */
    PSP_001,

    /**
     * Il Flusso di Rendicontazione cercato non esiste.
     * 
     */
    RND_000,

    /**
     * Applicazione non abilitata all'acquisizione del flusso indicato.
     * 
     */
    RND_001,

    /**
     * Stazione inesistente
     * 
     */
    STA_000,

    /**
     * Stazione disabilitata
     * 
     */
    STA_001,

    /**
     * Tributo inesistente
     * 
     */
    TRB_000,

    /**
     * Unita Operativa inesistente
     * 
     */
    UOP_000,

    /**
     * Unita Operativa disabilitata
     * 
     */
    UOP_001,

    /**
     * Versamento non pagabile ad iniziativa PSP
     * 
     */
    VER_000,

    /**
     * Il versamento presenta singoli versamenti con codSingoloVersamento non univoci
     * 
     */
    VER_001,

    /**
     * La somma degli importi dei singoli versamenti non corrisponde all'importo totale del versamento
     * 
     */
    VER_002,

    /**
     * Non è possibile aggiornare un versamento in stato diverso da NON_ESEGUITO o ANNULLATO
     * 
     */
    VER_003,

    /**
     * Non è possibile aggiornare un versamento cambiando l'unità operativa beneficiaria
     * 
     */
    VER_004,

    /**
     * Non è possibile aggiornare un versamento cambiando il numero di singoli importi
     * 
     */
    VER_005,

    /**
     * Non è possibile aggiornare un versamento modificando i codSingoliVersamenti dei singoli versamenti
     * 
     */
    VER_006,

    /**
     * Non è possibile aggiornare un versamento modificando la tipologia di tributo dei singoli versamenti
     * 
     */
    VER_007,

    /**
     * Versamento inesistente
     * 
     */
    VER_008,

    /**
     * Non è possibile annullare un versamento in stato diverso da NON_ESEGUITO
     * 
     */
    VER_009,

    /**
     * L'aggiornamento del versamento dall'applicazione ha dato esito PAA_PAGAMENTO_SCADUTO
     * 
     */
    VER_010,

    /**
     * L'aggiornamento del versamento dall'applicazione ha dato esito PAA_PAGAMENTO_SCONOSCIUTO
     * 
     */
    VER_011,

    /**
     * L'aggiornamento del versamento dall'applicazione ha dato esito PAA_PAGAMENTO_DUPLICATO
     * 
     */
    VER_012,

    /**
     * L'aggiornamento del versamento dall'applicazione ha dato esito PAA_PAGAMENTO_ANNULLATO
     * 
     */
    VER_013,

    /**
     * L'aggiornamento del versamento dall'applicazione e' fallito
     * 
     */
    VER_014,

    /**
     * Aggiornamento non consentito se AggiornaSeEsiste impostato a false
     * 
     */
    VER_015,

    /**
     * Non è possibile notificare un pagamento per un versamento in stato diverso da NON_ESEGUITO o ANNULLATO
     * 
     */
    VER_016,

    /**
     * Iuv da caricare non conforme alle specifiche AgID
     * 
     */
    VER_017,

    /**
     * Iuv da caricare gia' associato ad un diverso Versamento
     * 
     */
    VER_018,

    /**
     * Applicazione non autorizzata all'autodeterminazione dei tributi
     * 
     */
    VER_019,

    /**
     * Iban di accredito non censito
     * 
     */
    VER_020,

    /**
     * Applicazione non autorizzata all'autodeterminazione dei tipi versamento per il dominio indicato
     * 
     */
    VER_021,

    /**
     * Applicazione non autorizzata alla gestione del tipo versamento indicato
     * 
     */
    VER_022,

    /**
     * Non e' possibile aggiornare un versamento modificando l'iban di accredito
     * 
     */
    VER_023,

    /**
     * Sessione di scelta sconosciuta al WISP
     * 
     */
    WISP_000,

    /**
     * Sessione di scelta scaduta per timeout al WISP
     * 
     */
    WISP_001,

    /**
     * Canale scelto non presente in anagrafica
     * 
     */
    WISP_002,

    /**
     * Il debitore non ha operato alcuna scelta sul WISP
     * 
     */
    WISP_003,

    /**
     * Il debitore ha scelto di pagare dopo tramite avviso di pagamento.
     * 
     */
    WISP_004,
	
	
	// Aggiunti nella versione 3.0.x
	
	  /**
     * Applicazione non autorizzato a pagare i versamenti dell'applicazione indicata
     * 
     */
    APP_003,

    /**
     * Applicazione non autorizzato a visualizzare l'esito della transazione indicata
     * 
     */
    APP_004,

    /**
     * Applicazione non autorizzata all'operazione richiesta
     * 
     */
    APP_005,
	
    /**
     * Non e' possibile aggiornare un versamento modificando il numero avviso
     * 
     */
    VER_024,
    
    /**
     * Non e' possibile inserire due versamenti con lo stesso numero avviso
     * 
     */
    VER_025,
    
    /**
     * Iuv da caricare non conforme alle specifiche AgID application code errato
     * 
     */
    VER_026,
    
    /**
     * Iuv da caricare non conforme alle specifiche AgID segregation code errato
     * 
     */
    VER_027,
    /**
     * Iuv da caricare non valido, formato del prefisso per uno IUV di tipo NUMERICO non valido
     * 
     */
    VER_028,
    /**
     * Iuv da caricare non valido, formato del prefisso per uno IUV di tipo ISO11694 non valido
     * 
     */
    VER_029,
    /**
     * Iuv generato non rispetta il pattern previsto per l'applicazione 
     * 
     */
    VER_030,
    /**
     * Utenza di tipo cittadino non autorizzata a effettuare dei pagamenti portale senza specificare il tributo da pagare. 
     * 
     */
    CIT_001,
    /**
     * Utenza di tipo cittadino non autorizzata a effettuare il pagamento di un tipo versamento non spontaneo.
     * 
     */
    CIT_002,
    /**
     * Utenza di tipo cittadino non autorizzata a effettuare dei pagamenti portale dove il debitore non e' lui. 
     * 
     */
    CIT_003,
    /**
     * Utenza di tipo cittadino non autorizzata a effettuare dei pagamenti portale dove il versante non e' lui.  
     * 
     */
    CIT_004,
    /**
     * Validazione semantica del versamento non valida: Pagamento in attesa risulta in corso all'Ente Creditore.  
     * 
     */
    PAG_014,

    /**
     * Applicazione non autorizzata a gestire lo Iuv per il dominio indicato
     * 
     */
    APP_006,
    /**
     * Utenza anonima non autorizzata a effettuare dei pagamenti portale senza specificare il tributo da pagare. 
     * 
     */
    UAN_001,
    /**
     * Utenza anonima non autorizzata a effettuare il pagamento di un tipo versamento non spontaneo.
     * 
     */
    UAN_002,

    /**
     * Tributo disabilitato
     * 
     */
    TRB_001,
    /**
     * Versamento multivoce non puo' essere caricato fornendo il numero avviso
     * 
     */
    VER_031,

    /**
     * Iban Accredito disabilitato 
     * 
     */
    VER_032,
    
    /**
     * Iban di accredito non censito
     * 
     */
    VER_033,
    
    /**
     * Iban Accredito disabilitato 
     * 
     */
    VER_034,

    /**
     * Errore trasformazione pendenza 
     * 
     */
    TRASFORMAZIONE,

    /**
     * Errore creazione factory validazione 
     * 
     */
    VAL_000,

    /**
     * Schema utilizzato per la validazione non valido. 
     * 
     */
    VAL_001,

    /**
     * Errore interno durante la validazione 
     * 
     */
    VAL_002,

    /**
     * Validazione del risultato della trasformazione non valido 
     * 
     */
    VAL_003,

    /**
     * Tipo Pendenza non censito
     * 
     */
    TVR_000,

    /**
     * Tipo Pendenza disabilitato
     * 
     */
    TVR_001,

    /**
     * Tipo Pendenza Dominio non censito
     * 
     */
    TVD_000,

    /**
     * Tipo Pendenza Dominio disabilitato
     * 
     */
    TVD_001,

    /**
     * Errore generazione oggetto promemoria avviso
     * 
     */
    PRM_001,

    /**
     * Errore generazione messggio promemoria avviso
     * 
     */
    PRM_002,

    /**
     * Errore generazione oggetto promemoria ricevuta
     * 
     */
    PRM_003,

    /**
     * Errore generazione messggio promemoria ricevuta
     * 
     */
    PRM_004;
	

    public String value() {
        return this.name();
    }

    public static EsitoOperazione fromValue(String v) {
        return valueOf(v);
    }

}