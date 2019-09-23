package it.govpay.core.utils;

public class ExceptionUtils {

	public static boolean existsInnerException(Throwable e,Class<?> found){
		if(found.isAssignableFrom(e.getClass())){
			return true;
		}else{
			if(e.getCause()!=null){
				return existsInnerException(e.getCause(), found);
			}
			else{
				return false;
			}
		}
	}

	public static Throwable getInnerException(Throwable e,Class<?> found){
		if(found.isAssignableFrom(e.getClass())){
			return e;
		}else{
			if(e.getCause()!=null){
				return getInnerException(e.getCause(), found);
			}
			else{
				return null;
			}
		}
	}
}
