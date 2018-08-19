import { Component, Input, OnInit } from '@angular/core';

import * as moment from 'moment';
import { Dato } from '../../../../classes/view/dato';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { GovpayService } from '../../../../services/govpay.service';

@Component({
  selector: 'link-riscossioni-view',
  templateUrl: './riscossioni-view.component.html',
  styleUrls: ['./riscossioni-view.component.scss']
})
export class RiscossioniViewComponent implements IModalDialog, OnInit {

  @Input() informazioni = [];

  @Input() json: any;


  constructor(private gps: GovpayService, private us: UtilService) { }

  ngOnInit() {
    this.dettaglioEvento();
  }

  protected dettaglioEvento() {
    let _url = UtilService.URL_RISCOSSIONI+'/'+this.json.idDominio+'/'+encodeURIComponent(this.json.iuv)+'/'+encodeURIComponent(this.json.iur)+'/'+encodeURIComponent(this.json.indice);
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.json = _response.body;
        this.mapJsonDetail();
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected mapJsonDetail() {
    //Riepilogo
    let _dettaglio = [];
    _dettaglio.push(new Dato({ label: Voce.IUV, value: this.json.iuv }));
    _dettaglio.push(new Dato({ label: Voce.IUR, value: this.json.iur }));
    _dettaglio.push(new Dato({ label: Voce.IMPORTO, value: this.us.currencyFormat(this.json.importo) }));
    let _date = UtilService.defaultDisplay({ value: moment(this.json.data).format('DD/MM/YYYY [ore] HH:mm') });
    _dettaglio.push(new Dato({ label: Voce.DATA_ESECUZIONE_RISCOSSIONE, value: _date }));
    _dettaglio.push(new Dato({ label: Voce.ID_PENDENZA, value: this.json.idVocePendenza }));
    _dettaglio.push(new Dato({ label: Voce.INDICE_PENDENZA, value: this.json.indice }));
    _dettaglio.push(new Dato({ label: Voce.ID_DOMINIO, value: this.json.idDominio }));
    _dettaglio.push(new Dato({ label: Voce.COMMISSIONI, value: this.us.currencyFormat(this.json.commissioni) }));
    _dettaglio.push(new Dato({ label: Voce.STATO, value: UtilService.STATI_RISCOSSIONE[this.json.stato] }));
    _dettaglio.push(new Dato({ label: Voce.TIPO_RISCOSSIONE, value: UtilService.TIPI_RISCOSSIONE[this.json.tipo] }));
    _dettaglio.push(new Dato({ label: Voce.INCASSO, value: UtilService.defaultDisplay({ value:this.json.incasso }) }));
    //_dettaglio.push(new Dato({ label: 'Url pendenza', value: this.json.pendenza }));
    //_dettaglio.push(new Dato({ label: 'Url rpp', value: this.json.rpp }));
    if(this.json.allegato) {
      _dettaglio.push(new Dato({ label: Voce.TIPO_ALLEGATO, value: this.json.allegato.tipo }));
      _dettaglio.push(new Dato({ label: Voce.CONTENUTO_ALLEGATO, value: this.json.allegato.testo }));
    }
    this.informazioni = _dettaglio.slice(0);
  }

  refresh(mb: ModalBehavior) {}
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}


  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.iur:null });
  }

}
