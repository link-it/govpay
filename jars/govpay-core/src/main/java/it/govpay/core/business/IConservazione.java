package it.govpay.core.business;

import java.util.List;

import it.govpay.model.Rpt.StatoConservazione;

public interface IConservazione {
	
	public class EsitoConservazione {
		private String codDominio; 
		private String iuv; 
		private String ccp;
		private StatoConservazione statoConservazione;
		private String descrizioneStato;
		
		private EsitoConservazione(String codDominio, String iuv, String ccp, StatoConservazione statoConservazione, String descrizioneStato) {
			this.codDominio = codDominio;
			this.iuv = iuv;
			this.ccp = ccp;
			this.statoConservazione = statoConservazione;
			this.descrizioneStato = descrizioneStato;
		}
		
		public static EsitoConservazione newInstanceOK(String codDominio, String iuv, String ccp) {
			return new EsitoConservazione(codDominio, iuv, ccp, StatoConservazione.OK, null);
		}
		
		public static EsitoConservazione newInstanceERROR(String codDominio, String iuv, String ccp, String descrizione) {
			return new EsitoConservazione(codDominio, iuv, ccp, StatoConservazione.ERRORE, descrizione);
		}
		
		public String getCodDominio() {
			return codDominio;
		}
		public String getIuv() {
			return iuv;
		}
		public String getCcp() {
			return ccp;
		}
		public StatoConservazione getStatoConservazione() {
			return statoConservazione;
		}
		public String getDescrizioneStato() {
			return descrizioneStato;
		}
	}

	public void sendConservazione(String codDominio, String iuv, String ccp, String idMessaggio, byte[] rt) throws Exception;
	public List<EsitoConservazione> getEsitiConservazione();
	public void notificaAcquisizioneEsito(EsitoConservazione esito);
}
