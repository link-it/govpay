package it.govpay.core.utils;

import org.openspcoop2.utils.service.context.IContext;

public class GpThreadLocal {
	
	public static void set(IContext ctx) {
		org.openspcoop2.utils.service.context.ContextThreadLocal.set(ctx);
	}

	public static void unset() {
		org.openspcoop2.utils.service.context.ContextThreadLocal.unset();
	}

	public static IContext get() {
		return org.openspcoop2.utils.service.context.ContextThreadLocal.get();
	}
}
