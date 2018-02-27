package it.govpay.rs.v1.beans;

public class Evento extends it.govpay.rs.v1.beans.base.Evento {

	public Evento(it.govpay.model.Evento evento) {
		if(evento.getCategoriaEvento() != null) {
			switch (evento.getCategoriaEvento()) { 
			case INTERFACCIA:
				this.setCategoriaEvento(CategoriaEventoEnum.INTERFACCIA);
				break;
			case INTERNO:
			default:
				this.setCategoriaEvento(CategoriaEventoEnum.INTERNO);
				break;
			}
		}
		this.setCcp(evento.getCcp());
		this.setComponente(evento.getComponente());
		this.setIdCanale(evento.getCodCanale());
		this.setIdDominio(evento.getCodDominio());
		this.setIdentificativoErogatore(evento.getErogatore());
		this.setIdentificativoFruitore(evento.getFruitore());
		this.setIdPsp(evento.getCodPsp());
		this.setIdStazione(evento.getCodStazione());
		this.setIuv(evento.getIuv());
//		this.setSottoTipoEvento(evento.getSottotipoEvento());
		if(evento.getTipoEvento() != null) {
			this.setTipoEvento(evento.getTipoEvento().name());
		}

		if(evento.getTipoVersamento() != null) {
			this.setTipoVersamento(evento.getTipoVersamento().name());
		}
	}
}
