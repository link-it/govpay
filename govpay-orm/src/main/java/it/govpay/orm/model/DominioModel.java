/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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
package it.govpay.orm.model;

import it.govpay.orm.Dominio;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Dominio 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class DominioModel extends AbstractModel<Dominio> {

	public DominioModel(){
	
		super();
	
		this.ID_STAZIONE = new it.govpay.orm.model.IdStazioneModel(new Field("idStazione",it.govpay.orm.IdStazione.class,"Dominio",Dominio.class));
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"Dominio",Dominio.class);
		this.RAGIONE_SOCIALE = new Field("ragioneSociale",java.lang.String.class,"Dominio",Dominio.class);
		this.GLN = new Field("gln",java.lang.String.class,"Dominio",Dominio.class);
		this.PLUGIN_CLASS = new Field("pluginClass",java.lang.String.class,"Dominio",Dominio.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"Dominio",Dominio.class);
		this.DISPONIBILITA = new it.govpay.orm.model.DisponibilitaModel(new Field("Disponibilita",it.govpay.orm.Disponibilita.class,"Dominio",Dominio.class));
	
	}
	
	public DominioModel(IField father){
	
		super(father);
	
		this.ID_STAZIONE = new it.govpay.orm.model.IdStazioneModel(new ComplexField(father,"idStazione",it.govpay.orm.IdStazione.class,"Dominio",Dominio.class));
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"Dominio",Dominio.class);
		this.RAGIONE_SOCIALE = new ComplexField(father,"ragioneSociale",java.lang.String.class,"Dominio",Dominio.class);
		this.GLN = new ComplexField(father,"gln",java.lang.String.class,"Dominio",Dominio.class);
		this.PLUGIN_CLASS = new ComplexField(father,"pluginClass",java.lang.String.class,"Dominio",Dominio.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Dominio",Dominio.class);
		this.DISPONIBILITA = new it.govpay.orm.model.DisponibilitaModel(new ComplexField(father,"Disponibilita",it.govpay.orm.Disponibilita.class,"Dominio",Dominio.class));
	
	}
	
	

	public it.govpay.orm.model.IdStazioneModel ID_STAZIONE = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField RAGIONE_SOCIALE = null;
	 
	public IField GLN = null;
	 
	public IField PLUGIN_CLASS = null;
	 
	public IField ABILITATO = null;
	 
	public it.govpay.orm.model.DisponibilitaModel DISPONIBILITA = null;
	 

	@Override
	public Class<Dominio> getModeledClass(){
		return Dominio.class;
	}
	
	@Override
	public String toString(){
		if(this.getModeledClass()!=null){
			return this.getModeledClass().getName();
		}else{
			return "N.D.";
		}
	}

}