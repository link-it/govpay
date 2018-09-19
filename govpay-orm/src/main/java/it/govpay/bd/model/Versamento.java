/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.bd.model.Applicazione;
import it.govpay.model.Iuv;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;

public class Versamento extends it.govpay.model.Versamento {

	private static final long serialVersionUID = 1L;
	// BUSINESS
	
	private transient List<SingoloVersamento> singoliVersamenti;
	private transient List<Rpt> rpts;
	private transient Applicazione applicazione;
	private transient Dominio dominio;
	private transient UnitaOperativa uo;
	private transient Iuv iuv;
	private List<Nota> note;
	
	public void addSingoloVersamento(it.govpay.bd.model.SingoloVersamento singoloVersamento) throws ServiceException {
		if(this.singoliVersamenti == null) {
			this.singoliVersamenti = new ArrayList<>();
		}
		
		this.singoliVersamenti.add(singoloVersamento);
		
		if(singoloVersamento.getTipoBollo() != null) {
			this.setBolloTelematico(true);
		}
	}
	
	public List<it.govpay.bd.model.SingoloVersamento> getSingoliVersamenti(BasicBD bd) throws ServiceException {
		if(this.singoliVersamenti == null && this.getId() != null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			this.singoliVersamenti = versamentiBD.getSingoliVersamenti(this.getId());
		}
		
		if(this.singoliVersamenti != null)
			Collections.sort(this.singoliVersamenti);
		
		return this.singoliVersamenti;
	}

	public Applicazione getApplicazione(BasicBD bd) throws ServiceException {
		if(this.applicazione == null) {
			this.applicazione = AnagraficaManager.getApplicazione(bd, this.getIdApplicazione());
		} 
		return this.applicazione;
	}
	
	public void setApplicazione(String codApplicazione, BasicBD bd) throws ServiceException, NotFoundException {
		this.applicazione = AnagraficaManager.getApplicazione(bd, codApplicazione);
		this.setIdApplicazione(this.applicazione.getId());
	}

	public UnitaOperativa getUo(BasicBD bd) throws ServiceException {
		if(this.getIdUo() != null && this.uo == null) {
			this.uo = AnagraficaManager.getUnitaOperativa(bd, this.getIdUo());
		}
		return this.uo;
	}

	public Dominio getDominio(BasicBD bd) throws ServiceException {
		if(this.dominio == null) {
			this.dominio = AnagraficaManager.getDominio(bd, this.getIdDominio());
		} 
		return this.dominio;
	}
	
	public UnitaOperativa setUo(long idDominio, String codUo, BasicBD bd) throws ServiceException, NotFoundException {
		this.uo = AnagraficaManager.getUnitaOperativa(bd, idDominio, codUo);
		this.setIdUo(this.uo.getId());
		return this.uo;
	}
	
	public List<Rpt> getRpt(BasicBD bd) throws ServiceException {
		if(this.rpts == null) {
			RptBD rptBD = new RptBD(bd);
			RptFilter filter = rptBD.newFilter();
			filter.setIdVersamento(this.getId());
			this.rpts = rptBD.findAll(filter);
		}
		return this.rpts;
	}
	
	public Iuv getIuv(BasicBD bd) throws ServiceException {
		if(this.iuv == null) {
			IuvBD iuvBD = new IuvBD(bd);
			try {
				this.iuv = iuvBD.getIuv(this.getIdApplicazione(), this.getCodVersamentoEnte(), TipoIUV.NUMERICO);
			} catch (NotFoundException e) {
				// Iuv non assegnato.
			}
		}
		return this.iuv;
	}
	
	public List<Nota> getNote() {
		if(this.note == null) this.note = new ArrayList<>();
		return this.note;
	}

	public String getNoteString() throws IOException {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormat());
		ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
		ListaNote listaNote = new ListaNote();
		listaNote.setLista(this.note);
		return serializer.getObject(listaNote);
	}

	public void setNote(String note) throws IOException {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormat());
		IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
		ListaNote listaNote = (ListaNote) deserializer.getObject(note, ListaNote.class);
		this.note = listaNote.getLista();
	}

	public void setNote(List<Nota> note) {
		this.note = note;
	}
}
