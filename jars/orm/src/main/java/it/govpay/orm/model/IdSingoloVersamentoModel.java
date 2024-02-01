/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
package it.govpay.orm.model;

import it.govpay.orm.IdSingoloVersamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdSingoloVersamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdSingoloVersamentoModel extends AbstractModel<IdSingoloVersamento> {

	public IdSingoloVersamentoModel(){
	
		super();
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new Field("idVersamento",it.govpay.orm.IdVersamento.class,"id-singolo-versamento",IdSingoloVersamento.class));
		this.COD_SINGOLO_VERSAMENTO_ENTE = new Field("codSingoloVersamentoEnte",java.lang.String.class,"id-singolo-versamento",IdSingoloVersamento.class);
		this.INDICE_DATI = new Field("indiceDati",java.lang.Integer.class,"id-singolo-versamento",IdSingoloVersamento.class);
		this.ID_TRIBUTO = new it.govpay.orm.model.IdTributoModel(new Field("idTributo",it.govpay.orm.IdTributo.class,"id-singolo-versamento",IdSingoloVersamento.class));
		this.NOTE = new Field("note",java.lang.String.class,"id-singolo-versamento",IdSingoloVersamento.class);
		this.STATO_SINGOLO_VERSAMENTO = new Field("statoSingoloVersamento",java.lang.String.class,"id-singolo-versamento",IdSingoloVersamento.class);
		this.IMPORTO_SINGOLO_VERSAMENTO = new Field("importoSingoloVersamento",double.class,"id-singolo-versamento",IdSingoloVersamento.class);
	
	}
	
	public IdSingoloVersamentoModel(IField father){
	
		super(father);
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new ComplexField(father,"idVersamento",it.govpay.orm.IdVersamento.class,"id-singolo-versamento",IdSingoloVersamento.class));
		this.COD_SINGOLO_VERSAMENTO_ENTE = new ComplexField(father,"codSingoloVersamentoEnte",java.lang.String.class,"id-singolo-versamento",IdSingoloVersamento.class);
		this.INDICE_DATI = new ComplexField(father,"indiceDati",java.lang.Integer.class,"id-singolo-versamento",IdSingoloVersamento.class);
		this.ID_TRIBUTO = new it.govpay.orm.model.IdTributoModel(new ComplexField(father,"idTributo",it.govpay.orm.IdTributo.class,"id-singolo-versamento",IdSingoloVersamento.class));
		this.NOTE = new ComplexField(father,"note",java.lang.String.class,"id-singolo-versamento",IdSingoloVersamento.class);
		this.STATO_SINGOLO_VERSAMENTO = new ComplexField(father,"statoSingoloVersamento",java.lang.String.class,"id-singolo-versamento",IdSingoloVersamento.class);
		this.IMPORTO_SINGOLO_VERSAMENTO = new ComplexField(father,"importoSingoloVersamento",double.class,"id-singolo-versamento",IdSingoloVersamento.class);
	
	}
	
	

	public it.govpay.orm.model.IdVersamentoModel ID_VERSAMENTO = null;
	 
	public IField COD_SINGOLO_VERSAMENTO_ENTE = null;
	 
	public IField INDICE_DATI = null;
	 
	public it.govpay.orm.model.IdTributoModel ID_TRIBUTO = null;
	 
	public IField NOTE = null;
	 
	public IField STATO_SINGOLO_VERSAMENTO = null;
	 
	public IField IMPORTO_SINGOLO_VERSAMENTO = null;
	 

	@Override
	public Class<IdSingoloVersamento> getModeledClass(){
		return IdSingoloVersamento.class;
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
