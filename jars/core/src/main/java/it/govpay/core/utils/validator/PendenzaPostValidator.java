package it.govpay.core.utils.validator;

import java.math.BigDecimal;
import java.util.Date;

import org.openspcoop2.utils.json.ValidationException;

import it.govpay.core.beans.tracciati.Contabilita;
import it.govpay.core.beans.tracciati.Documento;
import it.govpay.core.beans.tracciati.PendenzaPost;
import it.govpay.core.beans.tracciati.QuotaContabilita;
import it.govpay.core.beans.tracciati.Soggetto;
import it.govpay.core.beans.tracciati.VincoloPagamento;
import it.govpay.core.beans.tracciati.VocePendenza;

public class PendenzaPostValidator  implements IValidable{

	private PendenzaPost pendenzaVerificata= null;
	private ValidatorFactory vf = null; 
	private ValidatoreIdentificativi validatoreId = null;

	public PendenzaPostValidator(PendenzaPost pendenzaVerificata){
		this.pendenzaVerificata = pendenzaVerificata;
		this.vf = ValidatorFactory.newInstance();
		this.validatoreId = ValidatoreIdentificativi.newInstance();
	}

	@Override
	public void validate() throws ValidationException {
		if(this.pendenzaVerificata != null) {

			validaIdPendenza(this.pendenzaVerificata.getIdPendenza());
			validaIdA2A(this.pendenzaVerificata.getIdA2A());
			validaIdDominio(this.pendenzaVerificata.getIdDominio());
			validaIdUnitaOperativa(this.pendenzaVerificata.getIdUnitaOperativa());
			validaIdTipoPendenza(this.pendenzaVerificata.getIdTipoPendenza());
			validaNomePendenza(this.pendenzaVerificata.getNome());
			validaCausale( this.pendenzaVerificata.getCausale());
			
			Soggetto soggetto = this.pendenzaVerificata.getSoggettoPagatore();
			if(soggetto == null)
				throw new ValidationException("Il campo soggettoPagatore non deve essere vuoto.");
			
			SoggettoPagatoreValidator soggettoPagatoreValidator = SoggettoPagatoreValidator.newInstance();
			
			soggettoPagatoreValidator.validaTipo("tipo", soggetto.getTipo() != null ? soggetto.getTipo().toString() : null);
			soggettoPagatoreValidator.validaIdentificativo("identificativo", soggetto.getIdentificativo());
			soggettoPagatoreValidator.validaAnagrafica("anagrafica", soggetto.getAnagrafica());
			soggettoPagatoreValidator.validaIndirizzo("indirizzo", soggetto.getIndirizzo());
			soggettoPagatoreValidator.validaCivico("civico", soggetto.getCivico());
			soggettoPagatoreValidator.validaCap("cap", soggetto.getCap());
			soggettoPagatoreValidator.validaLocalita("localita", soggetto.getLocalita());
			soggettoPagatoreValidator.validaProvincia("provincia", soggetto.getProvincia());
			soggettoPagatoreValidator.validaNazione("nazione", soggetto.getNazione());
			soggettoPagatoreValidator.validaEmail("email", soggetto.getEmail());
			soggettoPagatoreValidator.validaCellulare("cellulare", soggetto.getCellulare());
			
			validaImporto(this.pendenzaVerificata.getImporto());
			validaNumeroAvviso(this.pendenzaVerificata.getNumeroAvviso());
			validaDataValidita(this.pendenzaVerificata.getDataValidita()); 
			validaDataScadenza(this.pendenzaVerificata.getDataScadenza());
			validaAnnoRiferimento(this.pendenzaVerificata.getAnnoRiferimento());
			validaCartellaPagamento(this.pendenzaVerificata.getCartellaPagamento());
			validaDirezione(this.pendenzaVerificata.getDirezione());
			validaDivisione(this.pendenzaVerificata.getDivisione()); 
			
			new DocumentoValidator(this.pendenzaVerificata.getDocumento()).validate();
			
			if(this.pendenzaVerificata.getVoci() == null || this.pendenzaVerificata.getVoci().isEmpty())
				throw new ValidationException("Il campo voci non deve essere vuoto.");

			if(this.pendenzaVerificata.getVoci().size() < 1)
				throw new ValidationException("Il campo voci deve avere almeno 1 elemento.");

			if(this.pendenzaVerificata.getVoci().size() > 5)
				throw new ValidationException("Il campo voci deve avere massimo 5 elemento.");

			for (VocePendenza vocePendenza : this.pendenzaVerificata.getVoci()) {
				new VocePendenzaValidator(vocePendenza).validate();
			}
		}
	}

	public void validaIdA2A(String idA2A) throws ValidationException {
		this.validatoreId.validaIdApplicazione("idA2A", idA2A);
	}

	public void validaIdDominio(String idDominio) throws ValidationException {
		this.validatoreId.validaIdDominio("idDominio", idDominio);
	}

	public void validaIdUnitaOperativa(String idUnitaOperativa) throws ValidationException {
		this.validatoreId.validaIdUO("idUnitaOperativa", idUnitaOperativa, false);
	}
	
	public void validaIdTipoPendenza(String idTipoPendenza) throws ValidationException {
		this.validatoreId.validaIdTipoVersamento("idTipoPendenza", idTipoPendenza, false);
	}

	public void validaNomePendenza(String nome) throws ValidationException {
		ValidatoreUtils.validaNomePendenza(vf, "nome", nome);
	}

	public void validaCausale(String causale) throws ValidationException {
		ValidatoreUtils.validaCausale(vf, "causale", causale);
	}

	public void validaImporto(BigDecimal importo) throws ValidationException {
		ValidatoreUtils.validaImporto(vf, "importo", importo);
	}

	public void validaNumeroAvviso(String numeroAvviso) throws ValidationException {
		ValidatoreUtils.validaNumeroAvviso(vf, "numeroAvviso", numeroAvviso);
	}

	public void validaDataValidita(Date date) throws ValidationException {
		ValidatoreUtils.validaData(vf, "dataValidita", date);
	}

	public void validaDataScadenza(Date date) throws ValidationException {
		ValidatoreUtils.validaData(vf, "dataScadenza", date);
	}

	public void validaAnnoRiferimento(BigDecimal annoRiferimento) throws ValidationException {
		ValidatoreUtils.validaAnnoRiferimento(vf, "annoRiferimento", annoRiferimento);
	}

	public void validaCartellaPagamento(String cartellaPagamento) throws ValidationException {
		ValidatoreUtils.validaCartellaPagamento(vf, "cartellaPagamento", cartellaPagamento);
	}

	public void validaIdPendenza(String idPendenza) throws ValidationException {
		this.validatoreId.validaIdPendenza("idPendenza", idPendenza);
	}
	
	public void validaDirezione(String direzione) throws ValidationException {
		this.validatoreId.validaIdDirezione("direzione", direzione);
	}
	
	public void validaDivisione(String divisione) throws ValidationException {
		this.validatoreId.validaIdDivisione("divisione", divisione);
	}
	
	
	public class VocePendenzaValidator implements IValidable{

		private VocePendenza vocePendenza = null;


		public VocePendenzaValidator(VocePendenza vocePendenza) {
			this.vocePendenza = vocePendenza;
		}

		@Override
		public void validate() throws ValidationException {
			if(this.vocePendenza != null) {

				ValidatorFactory vf = ValidatorFactory.newInstance();
				ValidatoreIdentificativi vi = ValidatoreIdentificativi.newInstance();
				
				vi.validaIdVocePendenza("idVocePendenza", this.vocePendenza.getIdVocePendenza());
				ValidatoreUtils.validaImporto(vf, "importo", this.vocePendenza.getImporto());
				ValidatoreUtils.validaDescrizione(vf, "descrizione", this.vocePendenza.getDescrizione());
				ValidatoreUtils.validaDescrizioneCausaleRPT(vf, "descrizioneCausaleRPT", this.vocePendenza.getDescrizioneCausaleRPT());
				new ContabilitaValidator(this.vocePendenza.getContabilita()).validate();
				if(this.vocePendenza.getIdDominio() != null)
					vi.validaIdDominio("idDominio", this.vocePendenza.getIdDominio());

				if(this.vocePendenza.getCodEntrata() != null) {
					vi.validaIdEntrata("codEntrata", this.vocePendenza.getCodEntrata());
					try {
						vf.getValidator("tipoBollo", this.vocePendenza.getTipoBollo()).isNull();
						vf.getValidator("hashDocumento", this.vocePendenza.getHashDocumento()).isNull();
						vf.getValidator("provinciaResidenza", this.vocePendenza.getProvinciaResidenza()).isNull();
						vf.getValidator("ibanAccredito", this.vocePendenza.getIbanAccredito()).isNull();
						vf.getValidator("ibanAppoggio", this.vocePendenza.getIbanAppoggio()).isNull();
						vf.getValidator("tipoContabilita", this.vocePendenza.getTipoContabilita()).isNull();
						vf.getValidator("codiceContabilita", this.vocePendenza.getCodiceContabilita()).isNull();
					} catch (ValidationException ve) {
						throw new ValidationException("Valorizzato codEntrata. " + ve.getMessage());
					}

					return;
				}

				else if(this.vocePendenza.getTipoBollo() != null) {
					ValidatoreUtils.validaTipoBollo(vf, "tipoBollo", this.vocePendenza.getTipoBollo());
					ValidatoreUtils.validaHashDocumento(vf, "hashDocumento", this.vocePendenza.getHashDocumento());
					ValidatoreUtils.validaProvinciaResidenza(vf, "provinciaResidenza", this.vocePendenza.getProvinciaResidenza());
					
					try {
						vf.getValidator("ibanAccredito", this.vocePendenza.getIbanAccredito()).isNull();
						vf.getValidator("ibanAppoggio", this.vocePendenza.getIbanAppoggio()).isNull();
						vf.getValidator("tipoContabilita", this.vocePendenza.getTipoContabilita()).isNull();
						vf.getValidator("codiceContabilita", this.vocePendenza.getCodiceContabilita()).isNull();
					} catch (ValidationException ve) {
						throw new ValidationException("Valorizzato tipoBollo. " + ve.getMessage());
					}

					return;
				}


				else if(this.vocePendenza.getIbanAccredito() != null) {
					vi.validaIdIbanAccredito("ibanAccredito", this.vocePendenza.getIbanAccredito(), true);
					vi.validaIdIbanAccredito("ibanAppoggio", this.vocePendenza.getIbanAppoggio());

					ValidatoreUtils.validaTipoContabilita(vf, "tipoContabilita", this.vocePendenza.getTipoContabilita());
					ValidatoreUtils.validaCodiceContabilita(vf, "codiceContabilita", this.vocePendenza.getCodiceContabilita());
					
					try {
						vf.getValidator("hashDocumento", this.vocePendenza.getHashDocumento()).isNull();
						vf.getValidator("provinciaResidenza", this.vocePendenza.getProvinciaResidenza()).isNull();
					} catch (ValidationException ve) {
						throw new ValidationException("Valorizzato ibanAccredito. " + ve.getMessage());
					}
				}

				else {
					throw new ValidationException("Uno dei campi tra ibanAccredito, tipoBollo o codEntrata deve essere valorizzato");
				}
			}
		}
	}
	
	public class DocumentoValidator implements IValidable{
		
		private Documento documento = null;


		public DocumentoValidator(Documento documento) {
			this.documento = documento;
		}

		@Override
		public void validate() throws ValidationException {
			if(this.documento != null) {
				ValidatorFactory vf = ValidatorFactory.newInstance();
				ValidatoreIdentificativi vi = ValidatoreIdentificativi.newInstance();
				
				vi.validaIdDocumento("identificativo", this.documento.getIdentificativo());
				vf.getValidator("descrizione", this.documento.getDescrizione()).notNull().minLength(1).maxLength(255);
				if(this.documento.getRata() != null) {
					ValidatoreUtils.validaRata(vf, "rata", this.documento.getRata());
				} else if(this.documento.getSoglia() != null) {
					new VincoloPagamentoValidator(this.documento.getSoglia()).validate();
				}
			}
		}
	}
	
	public class VincoloPagamentoValidator implements IValidable{
		
		private VincoloPagamento vincoloPagamento = null;


		public VincoloPagamentoValidator(VincoloPagamento vincoloPagamento) {
			this.vincoloPagamento = vincoloPagamento;
		}
	
	  @Override
		public void validate() throws ValidationException {
		  	ValidatorFactory vf = ValidatorFactory.newInstance();
		  	
		  	ValidatoreUtils.validaSogliaGiorni(vf, "giorni", this.vincoloPagamento.getGiorni());
		  	ValidatoreUtils.validaSogliaTipo(vf, "tipo", this.vincoloPagamento.getTipo());
		}
	  
	}
	
	public class ContabilitaValidator implements IValidable{
		
		private Contabilita contabilita = null;


		public ContabilitaValidator(Contabilita contabilita) {
			this.contabilita = contabilita;
		}
		
		 @Override
			public void validate() throws ValidationException {
			  	ValidatorFactory vf = ValidatorFactory.newInstance();
			  	
			  	if(this.contabilita != null) {
			  		if(this.contabilita.getQuote() != null) {
			  			for (QuotaContabilita quota : this.contabilita.getQuote()) {
			  				vf.getValidator("capitolo", quota.getCapitolo()).notNull().minLength(1).maxLength(64);
			  				
			  				vf.getValidator("annoEsercizio", quota.getAnnoEsercizio()).notNull();
			  				ValidatoreUtils.validaAnnoRiferimento(vf, "annoEsercizio", quota.getAnnoEsercizio());

			  				vf.getValidator("accertamento", quota.getAccertamento()).minLength(1).maxLength(64);
			  				ValidatoreUtils.validaImporto(vf, "importo", quota.getImporto());

			  				vf.getValidator("titolo", quota.getTitolo()).minLength(1).maxLength(64);
			  				vf.getValidator("tipologia", quota.getTipologia()).minLength(1).maxLength(64);
			  				vf.getValidator("categoria", quota.getCategoria()).minLength(1).maxLength(64);
			  				vf.getValidator("articolo", quota.getArticolo()).minLength(1).maxLength(64);
						}
			  		}
			  	}
		 }
	}	
}
