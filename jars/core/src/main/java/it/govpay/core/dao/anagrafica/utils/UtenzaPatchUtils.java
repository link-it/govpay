/**
 * 
 */
package it.govpay.core.dao.anagrafica.utils;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AclBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.anagrafica.ApplicazioniBD;
import it.govpay.bd.anagrafica.UtenzeBD;
import it.govpay.bd.model.Acl;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Utenza;
import it.govpay.core.dao.commons.Dominio;
import it.govpay.core.dao.commons.Dominio.Uo;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.validator.ValidatoreIdentificativi;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.IdUnitaOperativa;
import it.govpay.model.PatchOp;
import it.govpay.model.Utenza.TIPO_UTENZA;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 03 lug 2018 $
 * 
 */
public class UtenzaPatchUtils {

	public static final String PATH_TIPI_PENDENZA = "/tipiPendenza";
	public static final String PATH_DOMINI = "/domini";
	public static final String PATH_ACL = "/acl";
	public static final String PATH_XX_NON_VALIDO = "Path ''{0}'' non valido";
	public static final String PATH_NON_VALIDO = "Il campo path non e' valido.";
	public static final String VALUE_NON_VALIDO_PER_IL_PATH_XX = "Value non valido per il path ''{0}''";
	public static final String VALUE_NON_VALIDO_PER_IL_PATH = "Il campo value non e' valido per il path indicato.";
	public static final String OP_XX_NON_VALIDO_PER_IL_PATH_YY = "Op ''{0}'' non valido per il path ''{1}''";
	public static final String ACL_NON_VALIDA_SERVIZIO_XX_NON_GESTITO = "ACL non valida: servizio `{0}` non gestito.";
	public static final String VALUE_NON_VALIDO_PER_IL_PATH_XX_DI_UTENZA = "Value non valido per il path ''{0}'' di un'utenza di tipo ''{1}''";
	public static final String ACL_NON_VALIDA_ATTESA_LISTA_DI_STRINGHE_NEL_CAMPO_AUTORIZZAZIONI = "ACL non valida: attesa lista di stringhe nel campo `autorizzazioni`";
	public static final String ACL_NON_VALIDA_ATTESO_CAMPO_AUTORIZZAZIONI = "ACL non valida: atteso campo `autorizzazioni`";
	public static final String AUTORIZZAZIONI_KEY = "autorizzazioni";
	public static final String SERVIZIO_KEY = "servizio";
	public static final String ACL_NON_VALIDA_ATTESA_STRINGA_COME_VALORE_DI_SERVIZIO = "ACL non valida: attesa stringa come valore di `servizio`";
	public static final String ACL_NON_VALIDA_ATTESO_CAMPO_SERVIZIO = "ACL non valida: atteso campo `servizio`";
	
	public static final String PRINCIPAL_NOTA_KEY = "principal";
	public static final String AUTORE_NOTA_KEY = "autore";
	public static final String TIPO_NOTA_KEY = "tipo";
	public static final String DATA_NOTA_KEY = "data";
	public static final String OGGETTO_NOTA_KEY = "oggetto";
	public static final String TESTO_NOTA_KEY = "testo";
	
	public static final String DOMINI_STAR = "*";
	public static final String TIPI_PENDENZA_STAR = "*";
	public static final String AUTODETERMINAZIONE_TIPI_PENDENZA = "autodeterminazione";

	
	public static Utenza patchUtenza(PatchOp op, Utenza utenza, BasicBD bd) throws ServiceException, NotFoundException, ValidationException {

		if(PATH_ACL.equals(op.getPath())) {
			patchACL(op, utenza, bd);
		} else if(PATH_DOMINI.equals(op.getPath())) {
			patchDominio(op, utenza, bd);
		} else if(PATH_TIPI_PENDENZA.equals(op.getPath())) {
			patchTipoPendenza(op, utenza, bd);
		} else {
			throw new ValidationException(MessageFormat.format(PATH_XX_NON_VALIDO, op.getPath()));
		}

		return utenza;
	}

	private static void patchTipoPendenza(PatchOp op, Utenza utenza, BasicBD bd)
			throws ValidationException, ServiceException, NotFoundException {
		if(!(op.getValue() instanceof String)) throw new ValidationException(MessageFormat.format(VALUE_NON_VALIDO_PER_IL_PATH_XX, op.getPath()));
		String tipoVersamento = (String) op.getValue();
		
		if(tipoVersamento.equals(TIPI_PENDENZA_STAR)) {
			switch(op.getOp()) {
			case ADD: utenza.setAutorizzazioneTipiVersamentoStar(true);
			break;
			case DELETE: utenza.setAutorizzazioneTipiVersamentoStar(false);
			break;
			default: throw new ValidationException(MessageFormat.format(OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp().name(), op.getPath()));
			}
			utenza.getIdTipiVersamento().clear();
		} else if(tipoVersamento.equals(AUTODETERMINAZIONE_TIPI_PENDENZA)) {
			if(utenza.getTipoUtenza().equals(TIPO_UTENZA.APPLICAZIONE)) {
				Applicazione applicazioneByPrincipal = AnagraficaManager.getApplicazioneByPrincipal(bd, utenza.getPrincipalOriginale());
				switch(op.getOp()) {
				case ADD: applicazioneByPrincipal.setTrusted(true);
				break;
				case DELETE: applicazioneByPrincipal.setTrusted(false);
				break;
				default: throw new ValidationException(MessageFormat.format(OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp().name(), op.getPath()));
				}
				
				ApplicazioniBD applicazioniBD = new ApplicazioniBD(bd);
				applicazioniBD.updateApplicazioneTrusted(applicazioneByPrincipal.getId(), applicazioneByPrincipal.getCodApplicazione(), applicazioneByPrincipal.isTrusted());
			} else {
				throw new ValidationException(MessageFormat.format(VALUE_NON_VALIDO_PER_IL_PATH_XX_DI_UTENZA, op.getValue(), utenza.getTipoUtenza().name()));
			}
		} else {
			ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
			validatoreId.validaIdTipoVersamento("tipiPendenza", tipoVersamento);
			try {
				AnagraficaManager.getTipoVersamento(bd, tipoVersamento).getId();
			} catch (NotFoundException e) {
				throw new ValidationException(MessageFormat.format(VALUE_NON_VALIDO_PER_IL_PATH_XX, op.getPath()));
			}
			Long idTipoVersamento = AnagraficaManager.getTipoVersamento(bd, tipoVersamento).getId();
			switch(op.getOp()) {
			case ADD: utenza.getIdTipiVersamento().add(idTipoVersamento); 
			break;
			case DELETE: utenza.getIdTipiVersamento().remove(idTipoVersamento);
			break;
			default: throw new ValidationException(MessageFormat.format(OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp().name(), op.getPath()));
			}
		}
		UtenzeBD utenzaBD = new UtenzeBD(bd);
		utenzaBD.updateUtenza(utenza);

		utenza.setTipiVersamento(null);
		utenza.getTipiVersamento(bd);
	}

	private static void patchDominio(PatchOp op, Utenza utenza, BasicBD bd)
			throws ValidationException, ServiceException, NotFoundException {
		
		if(!(op.getValue() instanceof String || op.getValue() instanceof Dominio)) throw new ValidationException(MessageFormat.format(VALUE_NON_VALIDO_PER_IL_PATH_XX, op.getPath()));
		
		// la patch puo' essere un oggetto complesso o un iddominio
		if(op.getValue() instanceof String) {
			String dominio = (String) op.getValue();

			if(dominio.equals(DOMINI_STAR)) {
				switch(op.getOp()) {
				case ADD: utenza.setAutorizzazioneDominiStar(true);
				break;
				case DELETE: utenza.setAutorizzazioneDominiStar(false);
				break;
				default: throw new ValidationException(MessageFormat.format(OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp().name(), op.getPath()));
				}
				utenza.getIdDominiUo().clear();
			} else {
				ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
				validatoreId.validaIdDominio("domini", dominio);
				try {
					AnagraficaManager.getDominio(bd, dominio).getId();
				} catch (NotFoundException e) {
					throw new ValidationException(MessageFormat.format(VALUE_NON_VALIDO_PER_IL_PATH_XX, op.getPath()));
				}
				Long idDominio = AnagraficaManager.getDominio(bd, dominio).getId();
				switch(op.getOp()) {
				case ADD: 
					IdUnitaOperativa idUnitaOperativa = new IdUnitaOperativa();
					idUnitaOperativa.setIdDominio(idDominio);
					utenza.getIdDominiUo().add(idUnitaOperativa ); 
				break;
				case DELETE: utenza.removeIdDominiUo(idDominio);
				break;
				default: throw new ValidationException(MessageFormat.format(OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp().name(), op.getPath()));
				}
			}
		} else if(op.getValue() instanceof Dominio) {
			Dominio dominio = (Dominio) op.getValue();

			if(dominio.getCodDominio().equals(DOMINI_STAR)) {
				switch(op.getOp()) {
				case ADD: utenza.setAutorizzazioneDominiStar(true);
				break;
				case DELETE: utenza.setAutorizzazioneDominiStar(false);
				break;
				default: throw new ValidationException(MessageFormat.format(OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp().name(), op.getPath()));
				}
				utenza.getIdDominiUo().clear();
			} else {
				ValidatoreIdentificativi validatoreId = ValidatoreIdentificativi.newInstance();
				validatoreId.validaIdDominio("domini", dominio.getCodDominio());
				try {
					AnagraficaManager.getDominio(bd, dominio.getCodDominio()).getId();
				} catch (NotFoundException e) {
					throw new ValidationException(MessageFormat.format(VALUE_NON_VALIDO_PER_IL_PATH_XX, op.getPath()));
				}
				Long idDominio = AnagraficaManager.getDominio(bd, dominio.getCodDominio()).getId();
				
				if(dominio.getUo() != null && !dominio.getUo().isEmpty()) {
					for (Uo uo : dominio.getUo()) {
						validatoreId.validaIdUO("uo", uo.getCodUo());
						try {
							AnagraficaManager.getUnitaOperativa(bd, idDominio, uo.getCodUo());
						} catch (NotFoundException e) {
							throw new ValidationException(MessageFormat.format(VALUE_NON_VALIDO_PER_IL_PATH_XX, op.getPath()));
						}
						Long idUo = AnagraficaManager.getUnitaOperativa(bd, idDominio, uo.getCodUo()).getId();
						switch(op.getOp()) {
						case ADD: 
							IdUnitaOperativa idUnitaOperativa = new IdUnitaOperativa();
							idUnitaOperativa.setIdDominio(idDominio);
							idUnitaOperativa.setIdUnita(idUo);
							utenza.getIdDominiUo().add(idUnitaOperativa); 
						break;
						case DELETE: utenza.removeIdDominiUo(idDominio,idUo);
						break;
						default: throw new ValidationException(MessageFormat.format(OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp().name(), op.getPath()));
						}
					}
				} else {
					switch(op.getOp()) {
					case ADD: 
						IdUnitaOperativa idUnitaOperativa = new IdUnitaOperativa();
						idUnitaOperativa.setIdDominio(idDominio);
						utenza.getIdDominiUo().add(idUnitaOperativa); 
					break;
					case DELETE: utenza.removeIdDominiUo(idDominio,null);
					break;
					default: throw new ValidationException(MessageFormat.format(OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp().name(), op.getPath()));
					}
				}
			}
		} 
		
		
		
		
		UtenzeBD utenzaBD = new UtenzeBD(bd);
		utenzaBD.updateUtenza(utenza);

		utenza.setDominiUo(null);
		utenza.getDominiUo(bd);
	}

	public static void patchRuolo(PatchOp op, String idRuolo, List<Acl> lstAclAttualiRuolo, BasicBD bd)
			throws ValidationException, ServiceException, NotFoundException {
		LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) op.getValue();
		Acl acl = new Acl();
		acl.setRuolo(idRuolo); 
		setServizioAcl(map, acl);
		setAutorizzazioniAcl(map, acl);
		
		boolean found = false;
		for(Acl aclPresente : lstAclAttualiRuolo) {
			if(aclPresente.getServizio().equals(acl.getServizio()) && (aclPresente.getRuolo() != null && acl.getRuolo().equals(aclPresente.getRuolo()))) {
				found = true;
				break;
			}
		}
		
		AclBD aclBD = new AclBD(bd);
		switch(op.getOp()) {
		case ADD: 
			if(!found)
				aclBD.insertAcl(acl);
			else
				aclBD.updateAcl(acl);
			break;
//		case REPLACE:
//			aclBD.updateAcl(acl);
//			break;
		case DELETE: 
			aclBD.deleteAcl(acl);
			break;
		default: throw new ValidationException(MessageFormat.format(OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp().name(), op.getPath()));
		}
	}

	private static void patchACL(PatchOp op, Utenza utenza, BasicBD bd)
			throws ValidationException, ServiceException, NotFoundException {
		LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) op.getValue();
		Acl acl = new Acl();
		setServizioAcl(map, acl);
		setAutorizzazioniAcl(map, acl);
		
		AclBD aclBD = new AclBD(bd);
		acl.setIdUtenza(utenza.getId()); 
		
		boolean found = false;
		List<Acl> acls = utenza.getAcls();
		for(Acl aclPresente : acls) {
			if(aclPresente.getServizio().equals(acl.getServizio()) && (aclPresente.getIdUtenza() != null && acl.getIdUtenza().equals(aclPresente.getIdUtenza()))) {
				found = true;
				break;
			}
		}
		
		switch(op.getOp()) {
		case ADD: 
			if(!found)
				aclBD.insertAcl(acl);
			else
				aclBD.updateAcl(acl);
			break;
		case DELETE: 
			if(found)
				aclBD.deleteAcl(acl);
			else
				throw new ValidationException(MessageFormat.format(OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp().name(), op.getPath()));
			break;
		default: throw new ValidationException(MessageFormat.format(OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp().name(), op.getPath()));
		}
	}

	private static void setAutorizzazioniAcl(LinkedHashMap<?, ?> map, Acl acl) throws ValidationException {
		if(map.get(AUTORIZZAZIONI_KEY) == null) throw new ValidationException(ACL_NON_VALIDA_ATTESO_CAMPO_AUTORIZZAZIONI);
		if(!(map.get(AUTORIZZAZIONI_KEY) instanceof List<?>)) throw new ValidationException(ACL_NON_VALIDA_ATTESA_LISTA_DI_STRINGHE_NEL_CAMPO_AUTORIZZAZIONI);

		List<?> lstAuth = (List<?>) map.get(AUTORIZZAZIONI_KEY);
		Set<Diritti> listaDiritti = new HashSet<>();

		for(Object obj: lstAuth) {
			try {
				if(!(obj instanceof String)) throw new ValidationException(ACL_NON_VALIDA_ATTESA_LISTA_DI_STRINGHE_NEL_CAMPO_AUTORIZZAZIONI);
				Diritti diritto = Diritti.toEnum((String)obj);
				if(!listaDiritti.contains(diritto))
					listaDiritti.add(diritto);
			} catch (ServiceException se) {
				throw new ValidationException(MessageFormat.format(ACL_NON_VALIDA_SERVIZIO_XX_NON_GESTITO, map.get(SERVIZIO_KEY)));
			}
		}
		acl.setListaDiritti(listaDiritti);
	}

	private static void setServizioAcl(LinkedHashMap<?, ?> map, Acl acl) throws ValidationException {
		try {
			if(map.get(SERVIZIO_KEY) == null) throw new ValidationException(ACL_NON_VALIDA_ATTESO_CAMPO_SERVIZIO);
			if(!(map.get(SERVIZIO_KEY) instanceof String)) throw new ValidationException(ACL_NON_VALIDA_ATTESA_STRINGA_COME_VALORE_DI_SERVIZIO);
			acl.setServizio(Servizio.toEnum((String) map.get(SERVIZIO_KEY)));
		} catch (ServiceException se) {
			throw new ValidationException(MessageFormat.format(ACL_NON_VALIDA_SERVIZIO_XX_NON_GESTITO, map.get(SERVIZIO_KEY)));
		}
		
	}

	public static String getDettaglioAsString(Object obj) throws IOException {
		if(obj != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi());
			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return serializer.getObject(obj); 
		}
		return null;
	}
}
