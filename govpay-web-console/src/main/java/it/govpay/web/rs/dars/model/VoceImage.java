package it.govpay.web.rs.dars.model;

public class VoceImage<T> extends Voce<T> {
	
	private int width = 0;
	private int height = 0; 

	public VoceImage(String etichetta, T valore) {
		super(etichetta, valore,false,TipoVoce.IMAGE);
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	
}
