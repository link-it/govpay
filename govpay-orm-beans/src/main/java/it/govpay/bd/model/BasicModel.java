/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.bd.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class BasicModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public BasicModel() {}

	public static boolean equals(Object a, Object b) {
		if(a==null)
			return b==null;
		
		return a.equals(b);
	}
	
	public static <T extends Comparable<? super T>> boolean equals(T a, T b) {
		if(a==null)
			return b==null;
		
		return a.compareTo(b) == 0;
	}
	
	public static <T extends Comparable<? super T>> boolean equals(List<T> a, List<T> b) {
		if(a==null || b==null)
			return a==null && b==null;
		
		if(a.size() != b.size())
			return false;
		
		Collections.sort(a);
		Collections.sort(b);
		
		for(int index = 0; index<a.size(); index++) {
			Object a1 = b.get(index);
			Object b1 = b.get(index);
			
			if(!a1.equals(b1)) 
				return false;
		}
		
		return true;
	}
	
	public static String diff(Object a, Object b) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if(a == null && b!= null) return "[NULL] [NOT NULL]";
		if(a != null && b== null) return "[NOT NULL] [NULL]";
		if(a == null && b== null) return null;
		
		Method[] methods = a.getClass().getDeclaredMethods();
		for(Method method : methods) {
			if((method.getName().startsWith("get") || method.getName().startsWith("is")) && method.getParameterTypes().length == 0) {
				if(method.getReturnType().isAssignableFrom(List.class)) {
					String diff = diff((List<?>) method.invoke(a), (List<?>) method.invoke(b));
					if(diff != null) return diff;
				} else {
					if(!equals(method.invoke(a), method.invoke(b))) {
						return method.getName() + "[" + method.invoke(a) + "] [" + method.invoke(b) + "]";
					}
				}
			}
		}
		return null;
	}

	public static <T extends Comparable<? super T>> String diff(List<T> a, List<T> b) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if(a == null && b!= null) return "[NULL] [NOT NULL]";
		if(a != null && b== null) return "[NOT NULL] [NULL]";
		if(a == null && b== null) return null;
		
		if(a.size() != b.size())
			return "[" + a.size() + " Entries] [" + b.size() + " Entries]";
		
		Collections.sort(a);
		Collections.sort(b);
		
		for(int index = 0; index<a.size(); index++) {
			Object a1 = b.get(index);
			Object b1 = b.get(index);
			
			String diff = diff(a1,b1);
			if(diff != null) return diff;
		}
		
		return null;
	}
}
