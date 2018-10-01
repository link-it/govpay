package it.govpay.core.utils;

public class GpThreadLocal {
	
	public static final ThreadLocal<GpContext> userThreadLocal = new ThreadLocal<>();

	public static void set(GpContext ctx) {
		userThreadLocal.set(ctx);
	}

	public static void unset() {
		userThreadLocal.remove();
	}

	public static GpContext get() {
		return userThreadLocal.get();
	}
}
