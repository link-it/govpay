package it.govpay.web.rs.dars.model;

import java.net.URI;

public class VoceDownload<T> extends Voce<T>{

	private URI downloadLink;
	
	public VoceDownload(String etichetta, T valore, URI riferimento) {
		this(etichetta, valore, false, riferimento);
	}
	
	public VoceDownload(String etichetta, T valore, boolean avanzata, URI riferimento) {
		super(etichetta, valore,avanzata);
		this.setDownloadLink(riferimento);
	}

	public URI getDownloadLink() {
		return downloadLink;
	}

	public void setDownloadLink(URI downloadLink) {
		this.downloadLink = downloadLink;
	}

 

}
