import { Component, Input, OnInit } from '@angular/core';

import * as moment from 'moment';
import { Dato } from '../../../../classes/view/dato';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';

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
    _dettaglio.push(new Dato({ label: Voce.ID_DOMINIO, value: this.json.idDominio }));
    _dettaglio.push(new Dato({ label: Voce.IUV, value: this.json.iuv }));
    _dettaglio.push(new Dato({ label: Voce.CCP, value: this.json.ccp }));
    _dettaglio.push(new Dato({ label: Voce.ID_PSP, value: this.json.idPsp }));
    _dettaglio.push(new Dato({ label: Voce.TIPO_VERSAMENTO, value: this.json.tipoVersamento }));
    _dettaglio.push(new Dato({ label: Voce.MODULO, value: this.json.componente }));
    _dettaglio.push(new Dato({ label: Voce.CATEGORIA_EVENTO, value: UtilService.TIPI_CATEGORIA_EVENTO[this.json.categoriaEvento] }));
    _dettaglio.push(new Dato({ label: Voce.TIPO_EVENTO, value: this.json.tipoEvento }));
    _dettaglio.push(new Dato({ label: Voce.FRUITORE, value: this.json.identificativoFruitore }));
    _dettaglio.push(new Dato({ label: Voce.EROGATORE, value: this.json.identificativoErogatore }));
    _dettaglio.push(new Dato({ label: Voce.ID_CANALE, value: this.json.idCanale }));
    _dettaglio.push(new Dato({ label: Voce.ID_STAZIONE, value: this.json.idStazione }));
    _dettaglio.push(new Dato({ label: Voce.PARAMETRI, value: this.json.parametri }));
    let _date = this.json.dataOraRichiesta?moment(this.json.dataOraRichiesta).format('DD/MM/YYYY [ore] HH:mm'):Voce.NON_PRESENTE;
    _dettaglio.push(new Dato({ label: Voce.DATA_RICHIESTA, value: _date }));
    _date = this.json.dataOraRisposta?moment(this.json.dataOraRisposta).format('DD/MM/YYYY [ore] HH:mm'):Voce.NON_PRESENTE;
    _dettaglio.push(new Dato({ label: Voce.DATA_RISPOSTA, value: _date }));
    _dettaglio.push(new Dato({ label: Voce.ESITO, value: this.json.esito }));

    this.informazioni = _dettaglio.slice(0);
  }

  refresh(mb: ModalBehavior) {}
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}


  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.tipoEvento:null });
  }
}
