package it.govpay.web.rs.dars.model.statistiche;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Colori {

	public final static String CODE_ERROR = "CD4A50";
	public final static String CODE_OK = "95B964";
	public final static String CODE_TOTALE = "3B83B7";
	
	public final static String CSS_COLOR_ERROR = "#"+CODE_ERROR;
	public final static String CSS_COLOR_OK = "#"+CODE_OK;
	public final static String CSS_COLOR_TOTALE = "#"+CODE_TOTALE;
	
	public final static Color COLOR_ERROR = Color.decode(CSS_COLOR_ERROR);
	public final static Color COLOR_OK = Color.decode(CSS_COLOR_OK);
	public final static Color COLOR_TOTALE = Color.decode(CSS_COLOR_TOTALE);

	public static final String [] coloriTransazioni = {
			getRgbString(Colori.COLOR_OK),
			getRgbString(Colori.COLOR_ERROR),
			getRgbString(Colori.COLOR_TOTALE)
	};

	public static List<String> getColoriTransazioni(){
		List<String> lst = new ArrayList<String>();

		lst.addAll(Arrays.asList(coloriTransazioni));

		return lst;
	}

	public static String getRgbString(Color c) {
		StringBuffer sb = new StringBuffer();
		sb.append("rgb(");
		sb.append(c.getRed());
		sb.append(",");
		sb.append(c.getGreen());
		sb.append(",");
		sb.append(c.getBlue());
//		sb.append(",");
//		sb.append(c.getAlpha());
		sb.append(")");
		return sb.toString();

	}
}
