import { Component, Input, OnInit } from '@angular/core';

import * as moment from 'moment';
import { Dato } from '../../../../classes/view/dato';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { UtilService } from '../../../../services/util.service';

@Component({
  selector: 'link-giornale-eventi-view',
  templateUrl: './giornale-eventi-view.component.html',
  styleUrls: ['./giornale-eventi-view.component.scss']
})
export class GiornaleEventiViewComponent implements IModalDialog, OnInit {

  @Input() informazioni = [];

  @Input() json: any;

  constructor() { }

  ngOnInit() {
    this.dettaglioEvento();
  }

  protected dettaglioEvento() {
    let _dettaglio = [];
    _dettaglio.push(new Dato({ label: 'Dominio', value: this.json.idDominio }));
    _dettaglio.push(new Dato({ label: 'Versamento (IUV)', value: this.json.iuv }));
    _dettaglio.push(new Dato({ label: 'Codice di pagamento (CCP)', value: this.json.ccp }));
    _dettaglio.push(new Dato({ label: 'Identificativo del PSP', value: this.json.idPsp }));
    _dettaglio.push(new Dato({ label: 'Tipologia di versamento realizzato', value: this.json.tipoVersamento }));
    _dettaglio.push(new Dato({ label: 'Modulo interno che ha emesso l\'evento', value: this.json.componente }));
    _dettaglio.push(new Dato({ label: 'Categoria evento', value: UtilService.TIPI_CATEGORIA_EVENTO[this.json.categoriaEvento] }));
    _dettaglio.push(new Dato({ label: 'Tipo evento', value: this.json.tipoEvento }));
    _dettaglio.push(new Dato({ label: 'Fruitore', value: this.json.identificativoFruitore }));
    _dettaglio.push(new Dato({ label: 'Erogatore', value: this.json.identificativoErogatore }));
    _dettaglio.push(new Dato({ label: 'Canale', value: this.json.idCanale }));
    _dettaglio.push(new Dato({ label: 'Stazione', value: this.json.idStazione }));
    _dettaglio.push(new Dato({ label: 'Parametri', value: this.json.parametri }));
    let _date = UtilService.defaultDisplay({ value: moment(this.json.dataOraRichiesta).format('DD/MM/YYYY [ore] HH:mm') });
    _dettaglio.push(new Dato({ label: 'Data richiesta', value: _date }));
    _date = UtilService.defaultDisplay({ value: moment(this.json.dataOraRisposta).format('DD/MM/YYYY [ore] HH:mm') });
    _dettaglio.push(new Dato({ label: 'Data risposta', value: _date }));
    _dettaglio.push(new Dato({ label: 'Esito', value: this.json.esito }));

    this.informazioni = _dettaglio.slice(0);
  }

  refresh(mb: ModalBehavior) {}
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}


  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.tipoEvento:null });
  }
}
