package it.govpay.web.legacy.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.Authentication;

import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpContext;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.commons.MetaInfo;
import it.govpay.servizi.commons.MetaInfo.IuvProp;

public class Utils {
	
	
	public static void isAuthorized(Authentication authentication, List<TIPO_UTENZA> tipoUtenza, List<Servizio> servizi, List<Diritti> listaDiritti) throws NotAuthorizedException {
		if(!AuthorizationManager.isAuthorized(authentication, tipoUtenza, servizi, listaDiritti)) {
			throw AuthorizationManager.toNotAuthorizedException(authentication);
		}
	}
	
	public static void loadMetaInfo(GpContext ctx, MetaInfo metaInfo) {
		if(metaInfo != null){
			Map<String, String> iuvProps = new HashMap<String, String>();
			for(IuvProp prop : metaInfo.getIuvProp()){
				iuvProps.put(prop.getNome(), prop.getValue());
			}
			ctx.getPagamentoCtx().setIuvProps(iuvProps);
		}
	}
	
	public static EsitoOperazione fromEsitoOperazioneGovpay(it.govpay.core.beans.EsitoOperazione esito) {
		if(esito == null)
			return EsitoOperazione.INTERNAL;
		
		EsitoOperazione toRet = EsitoOperazione.OK;
		
		switch (esito) {
		case APP_000:
			return EsitoOperazione.APP_000;
		case APP_001:
			return EsitoOperazione.APP_001;
		case APP_002:
			return EsitoOperazione.APP_002;
		case APP_003:
			return EsitoOperazione.APP_003;
		case AUT_000:
			return EsitoOperazione.AUT_000;
		case AUT_001:
			return EsitoOperazione.AUT_001;
		case AUT_002:
			return EsitoOperazione.AUT_002;
		case DOM_000:
			return EsitoOperazione.DOM_000;
		case DOM_001:
			return EsitoOperazione.DOM_001;
		case DOM_002:
			return EsitoOperazione.DOM_002;
		case DOM_003:
			return EsitoOperazione.DOM_003;
		case INTERNAL:
			return EsitoOperazione.INTERNAL;
		case NDP_000:
			return EsitoOperazione.NDP_000;
		case NDP_001:
			return EsitoOperazione.NDP_001;
		case OK:
			return EsitoOperazione.OK;
		case PAG_000:
			return EsitoOperazione.PAG_000;
		case PAG_001:
			return EsitoOperazione.PAG_001;
		case PAG_002:
			return EsitoOperazione.PAG_002;
		case PAG_003:
			return EsitoOperazione.PAG_003;
		case PAG_004:
			return EsitoOperazione.PAG_004;
		case PAG_005:
			return EsitoOperazione.PAG_005;
		case PAG_006:
			return EsitoOperazione.PAG_006;
		case PAG_007:
			return EsitoOperazione.PAG_007;
		case PAG_008:
			return EsitoOperazione.PAG_008;
		case PAG_009:
			return EsitoOperazione.PAG_009;
		case PAG_010:
			return EsitoOperazione.PAG_010;
		case PAG_011:
			return EsitoOperazione.PAG_011;
		case PAG_012:
			return EsitoOperazione.PAG_012;
		case PRT_000:
			return EsitoOperazione.PRT_000;
		case PRT_001:
			return EsitoOperazione.PRT_001;
		case PRT_002:
			return EsitoOperazione.PRT_002;
		case PRT_003:
			return EsitoOperazione.PRT_003;
		case PRT_004:
			return EsitoOperazione.PRT_004;
		case PRT_005:
			return EsitoOperazione.PRT_005;
		case PSP_000:
			return EsitoOperazione.PSP_000;
		case PSP_001:
			return EsitoOperazione.PSP_001;
		case RND_000:
			return EsitoOperazione.RND_000;
		case RND_001:
			return EsitoOperazione.RND_001;
		case STA_000:
			return EsitoOperazione.STA_000;
		case STA_001:
			return EsitoOperazione.STA_001;
		case TRB_000:
			return EsitoOperazione.TRB_000;
		case UOP_000:
			return EsitoOperazione.UOP_000;
		case UOP_001:
			return EsitoOperazione.UOP_001;
		case VER_000:
			return EsitoOperazione.VER_000;
		case VER_001:
			return EsitoOperazione.VER_001;
		case VER_002:
			return EsitoOperazione.VER_002;
		case VER_003:
			return EsitoOperazione.VER_003;
		case VER_004:
			return EsitoOperazione.VER_004;
		case VER_005:
			return EsitoOperazione.VER_005;
		case VER_006:
			return EsitoOperazione.VER_006;
		case VER_007:
			return EsitoOperazione.VER_007;
		case VER_008:
			return EsitoOperazione.VER_008;
		case VER_009:
			return EsitoOperazione.VER_009;
		case VER_010:
			return EsitoOperazione.VER_010;
		case VER_011:
			return EsitoOperazione.VER_011;
		case VER_012:
			return EsitoOperazione.VER_012;
		case VER_013:
			return EsitoOperazione.VER_013;
		case VER_014:
			return EsitoOperazione.VER_014;
		case VER_015:
			return EsitoOperazione.VER_015;
		case VER_016:
			return EsitoOperazione.VER_016;
		case VER_017:
			return EsitoOperazione.VER_017;
		case VER_018:
			return EsitoOperazione.VER_018;
		case VER_019:
			return EsitoOperazione.VER_019;
		case VER_020:
			return EsitoOperazione.VER_020;
		case VER_021:
			return EsitoOperazione.VER_021;
		case VER_022:
			return EsitoOperazione.VER_022;
		case VER_023:
			return EsitoOperazione.VER_023;
		case WISP_000:
			return EsitoOperazione.WISP_000;
		case WISP_001:
			return EsitoOperazione.WISP_001;
		case WISP_002:
			return EsitoOperazione.WISP_002;
		case WISP_003:
			return EsitoOperazione.WISP_003;
		case WISP_004:
			return EsitoOperazione.WISP_004;
			// casi aggiunti nelle nuove API
		case APP_004:
		case APP_005:
		case APP_006:
		case CIT_001:
		case CIT_002:
		case CIT_003:
		case CIT_004:
		case PAG_013:
		case PAG_014:
		case PRM_001:
		case PRM_002:
		case PRM_003:
		case PRM_004:
		case PRM_005:
		case PRM_006:
		case TRASFORMAZIONE:
		case TRB_001:
		case TVD_000:
		case TVD_001:
		case TVR_000:
		case TVR_001:
		case UAN_001:
		case UAN_002:
		case VAL_000:
		case VAL_001:
		case VAL_002:
		case VAL_003:
		case VER_024:
		case VER_025:
		case VER_026:
		case VER_027:
		case VER_028:
		case VER_029:
		case VER_030:
		case VER_031:
		case VER_032:
		case VER_033:
		case VER_034:
		case VER_035:
		case VER_036:
		case VER_037:
		case VER_038:
			return EsitoOperazione.INTERNAL;
		}
		
		return toRet;
	}
}
