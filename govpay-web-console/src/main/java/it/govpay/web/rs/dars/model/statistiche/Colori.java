package it.govpay.web.rs.dars.model.statistiche;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Colori {

	public static final Color colorBluRgb = Color.BLUE;
	public static final Color colorRedRgb = Color.RED;
	public static final Color colorGreenRgb = Color.GREEN;
	public static final Color colorBlackRgb = Color.BLACK;

	public static final String [] coloriTransazioni = {
			getRgbString(Colori.colorBluRgb),
			getRgbString(Colori.colorGreenRgb),
			getRgbString(Colori.colorRedRgb),
			getRgbString(Colori.colorBlackRgb)
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
