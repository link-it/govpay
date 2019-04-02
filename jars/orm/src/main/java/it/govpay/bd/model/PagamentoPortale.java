package it.govpay.bd.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.bd.viste.model.VersamentoIncasso;
import it.govpay.bd.viste.model.converter.VersamentoIncassoConverter;
import it.govpay.model.BasicModel;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.orm.IdVersamento;

public class PagamentoPortale extends BasicModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum VersioneInterfacciaWISP {
		WISP_1_3("1.3"),  
		WISP_2_0("2.0");
		
		private String codifica; 
		
		private VersioneInterfacciaWISP(String codifica) {
			this.codifica = codifica;
		}
		
		public String getCodifica() {
			return this.codifica;
		}
		
		public static VersioneInterfacciaWISP toEnum(String codifica) throws ServiceException {
			for(VersioneInterfacciaWISP v : VersioneInterfacciaWISP.values()){
				if(v.getCodifica().equals(codifica))
					return v;
			}
			
			throw new ServiceException("Codifica inesistente per VersioneInterfacciaWISP. Valore fornito [" + codifica + "] valori possibili " + ArrayUtils.toString(VersioneInterfacciaWISP.values()));
		}
	}

	public enum STATO { 
		IN_CORSO, 
		ANNULLATO, 
		FALLITO, 
		ESEGUITO, 
		NON_ESEGUITO, 
		ESEGUITO_PARZIALE 
	}

	public enum CODICE_STATO { 
		PAGAMENTO_IN_CORSO_AL_PSP, 
		PAGAMENTO_IN_ATTESA_DI_ESITO, 
		PAGAMENTO_ESEGUITO,
		PAGAMENTO_NON_ESEGUITO,
		PAGAMENTO_PARZIALMENTE_ESEGUITO,
		PAGAMENTO_FALLITO
	}

	private VersioneInterfacciaWISP versioneInterfacciaWISP = VersioneInterfacciaWISP.WISP_2_0;
	private String nome = null;
	private String versanteIdentificativo = null;
	private String idSessione = null;
	private String idSessionePortale = null;
	private String idSessionePsp = null;
	private List<IdVersamento> idVersamento = null;
	private STATO stato = null;
	private CODICE_STATO codiceStato = null;
	private String descrizioneStato = null;
	private String pspRedirectUrl = null;
	private String pspEsito = null;
	private String jsonRequest = null;

	private String wispIdDominio = null;
	private String wispKeyPA = null;
	private String wispKeyWisp = null;

	private String wispHtml =null;

	private Date dataRichiesta = null;
	private Long id;
	private String urlRitorno = null;

	private String codPsp = null;
	private String tipoVersamento = null;
	private String codCanale = null;
	
	private BigDecimal importo = null; 
	private String multiBeneficiario = null;
	private String principal = null;
	private TIPO_UTENZA tipoUtenza = null;
	private Long idApplicazione = null;

	private int tipo;
	private boolean ack;
	
	public String getIdSessione() {
		return this.idSessione;
	}
	public void setIdSessione(String idSessione) {
		this.idSessione = idSessione;
	}
	public String getIdSessionePortale() {
		return this.idSessionePortale;
	}
	public void setIdSessionePortale(String idSessionePortale) {
		this.idSessionePortale = idSessionePortale;
	}
	public String getIdSessionePsp() {
		return this.idSessionePsp;
	}
	public void setIdSessionePsp(String idSessionePsp) {
		this.idSessionePsp = idSessionePsp;
	}
	public STATO getStato() {
		return this.stato;
	}
	public void setStato(STATO stato) {
		this.stato = stato;
	}
	public String getPspRedirectUrl() {
		return this.pspRedirectUrl;
	}
	public void setPspRedirectUrl(String pspRedirectUrl) {
		this.pspRedirectUrl = pspRedirectUrl;
	}
	public String getJsonRequest() {
		return this.jsonRequest;
	}
	public void setJsonRequest(String jsonRequest) {
		this.jsonRequest = jsonRequest;
	}
	public String getWispIdDominio() {
		return this.wispIdDominio;
	}
	public List<IdVersamento> getIdVersamento() {
		return this.idVersamento;
	}
	public void setIdVersamento(List<IdVersamento> idVersamento) {
		this.idVersamento = idVersamento;
	}
	public void setWispIdDominio(String wispIdDominio) {
		this.wispIdDominio = wispIdDominio;
	}
	public String getWispHtml() {
		return this.wispHtml;
	}
	public void setWispHtml(String wispHtml) {
		this.wispHtml = wispHtml;
	}
	public Date getDataRichiesta() {
		return this.dataRichiesta;
	}
	public void setDataRichiesta(Date dataRichiesta) {
		this.dataRichiesta = dataRichiesta;
	}
	@Override
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUrlRitorno() {
		return this.urlRitorno;
	}
	public void setUrlRitorno(String urlRitorno) {
		this.urlRitorno = urlRitorno;
	}
	public String getPspEsito() {
		return this.pspEsito;
	}
	public void setPspEsito(String pspEsito) {
		this.pspEsito = pspEsito;
	}
	public String getWispKeyPA() {
		return this.wispKeyPA;
	}
	public void setWispKeyPA(String wispKeyPA) {
		this.wispKeyPA = wispKeyPA;
	}
	public String getWispKeyWisp() {
		return this.wispKeyWisp;
	}
	public void setWispKeyWisp(String wispKeyWisp) {
		this.wispKeyWisp = wispKeyWisp;
	}
	public String getCodPsp() {
		return this.codPsp;
	}
	public void setCodPsp(String codPsp) {
		this.codPsp = codPsp;
	}
	public String getTipoVersamento() {
		return this.tipoVersamento;
	}
	public void setTipoVersamento(String tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}
	public String getCodCanale() {
		return this.codCanale;
	}
	public void setCodCanale(String codCanale) {
		this.codCanale = codCanale;
	}
	public String getNome() {
		return this.nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getVersanteIdentificativo() {
		return this.versanteIdentificativo;
	}
	public void setVersanteIdentificativo(String versanteIdentificativo) {
		this.versanteIdentificativo = versanteIdentificativo;
	}
	public VersioneInterfacciaWISP getVersioneInterfacciaWISP() {
		return this.versioneInterfacciaWISP;
	}
	public void setVersioneInterfacciaWISP(VersioneInterfacciaWISP versioneInterfacciaWISP) {
		this.versioneInterfacciaWISP = versioneInterfacciaWISP;
	}


	// business
	private transient List<VersamentoIncasso> versamenti;

	public List<VersamentoIncasso> getVersamenti(BasicBD bd) throws ServiceException {
		if(this.versamenti != null)
			return this.versamenti;

		if(this.idVersamento != null && this.idVersamento.size() > 0) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			VersamentoFilter filter = versamentiBD.newFilter();
			List<Long> ids = new ArrayList<>();
			for (IdVersamento idVs : this.idVersamento) {
				ids.add(idVs.getId());
			}
			filter.setIdVersamento(ids);
			List<Versamento> findAll = versamentiBD.findAll(filter );
			
			if(findAll != null) {
				this.versamenti = new ArrayList<>();
				
				for (Versamento versamento : findAll) {
					VersamentoIncasso versamentoIncasso = VersamentoIncassoConverter.fromVersamento(versamento);
					
					versamentoIncasso.getApplicazione(versamentiBD);
					versamentoIncasso.getDominio(versamentiBD);
					versamentoIncasso.getUo(versamentiBD);
					List<SingoloVersamento> singoliVersamenti = versamentoIncasso.getSingoliVersamenti(versamentiBD);
					for (SingoloVersamento singoloVersamento : singoliVersamenti) {
						singoloVersamento.getCodContabilita(bd);
						singoloVersamento.getIbanAccredito(bd);
						singoloVersamento.getTipoContabilita(bd);
						singoloVersamento.getTributo(bd);
					}
					
					this.versamenti.add(versamentoIncasso);
				}
			}
		}
		return this.versamenti;
	}
	
	private transient Applicazione applicazione;
	
	public Applicazione getApplicazione(BasicBD bd) throws ServiceException {
		if(this.applicazione == null) {
			this.applicazione = AnagraficaManager.getApplicazione(bd, this.getIdApplicazione());
		} 
		return this.applicazione;
	}
	
	public void setApplicazione(String codApplicazione, BasicBD bd) throws ServiceException, NotFoundException {
		this.applicazione = AnagraficaManager.getApplicazione(bd, codApplicazione);
		this.setIdApplicazione(this.applicazione.getId());
	}
	
	
	public CODICE_STATO getCodiceStato() {
		return this.codiceStato;
	}
	public void setCodiceStato(CODICE_STATO codiceStato) {
		this.codiceStato = codiceStato;
	}
	public String getDescrizioneStato() {
		return this.descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	public BigDecimal getImporto() {
		return this.importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
	public String getMultiBeneficiario() {
		return this.multiBeneficiario;
	}
	public void setMultiBeneficiario(String multiBeneficiario) {
		this.multiBeneficiario = multiBeneficiario;
	}
	public boolean isAck() {
		return this.ack;
	}
	public void setAck(boolean ack) {
		this.ack = ack;
	}
	public int getTipo() {
		return this.tipo;
	}
	public void setTipo(int tipo) {
		this.tipo = tipo;
	}
	public String getPrincipal() {
		return principal;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	public TIPO_UTENZA getTipoUtenza() {
		return tipoUtenza;
	}
	public void setTipoUtenza(TIPO_UTENZA tipoUtenza) {
		this.tipoUtenza = tipoUtenza;
	}
	public Long getIdApplicazione() {
		return idApplicazione;
	}
	public void setIdApplicazione(Long idApplicazione) {
		this.idApplicazione = idApplicazione;
	}

}
