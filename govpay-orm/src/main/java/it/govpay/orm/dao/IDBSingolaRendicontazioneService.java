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
package it.govpay.orm.dao;

import it.govpay.orm.SingolaRendicontazione;
import org.openspcoop2.generic_project.dao.IDBServiceWithId;
import it.govpay.orm.IdSingolaRendicontazione;

/**     
 * Service can be used both for research that will make persistent objects on the backend of type it.govpay.orm.SingolaRendicontazione 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */

public interface IDBSingolaRendicontazioneService extends ISingolaRendicontazioneService,IDBServiceWithId<SingolaRendicontazione, IdSingolaRendicontazione> {

}
