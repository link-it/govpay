import { Component, Input, OnInit } from '@angular/core';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { Dato } from '../../../../classes/view/dato';

import * as moment from 'moment';

@Component({
  selector: 'link-rpps-view',
  templateUrl: './rpps-view.component.html',
  styleUrls: ['./rpps-view.component.scss']
})
export class RppsViewComponent implements IModalDialog, OnInit {

  @Input() informazioni = [];
  @Input() extra = [];

  @Input() json: any;


  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioRpp();
  }

  protected dettaglioRpp() {
    let _url = UtilService.URL_RPPS+'/'+encodeURIComponent(this.json.idDominio)+'/'+encodeURIComponent(this.json.iuv)+'/'+encodeURIComponent(this.json.ccp);
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.json = _response.body;
        this.mapJsonDetail();
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        //console.log(error);
        this.us.alert(error.message);
        this.gps.updateSpinner(false);
      });
  }

  protected mapJsonDetail() {
    let _info = [];
    _info.push(new Dato({ label: 'Codice IUV', value: this.json.iuv }));
    _info.push(new Dato({ label: 'Codice di Pagamento', value: this.json.ccp }));
    _info.push(new Dato({ label: 'Scadenza', value: UtilService.defaultDisplay({ value: moment(this.json.dataRichiesta).format('DD/MM/YYYY') }) }));
    _info.push(new Dato({ label: 'Importo', value: this.us.currencyFormat(this.json.importo) }));
    this.informazioni = _info.slice(0);

    //Altre informazioni
    let _ai = [];
    _ai.push(new Dato({ label: 'Dominio', value: this.json.idDominio }));
    _ai.push(new Dato({ label: 'Modello di pagamento', value: UtilService.TIPI_MODELLI_PAGAMENTO[this.json.modelloPagamento.toString()] }));
    _ai.push(new Dato({ label: 'Stato', value: this.json.stato }));
    _ai.push(new Dato({ label: 'Dettaglio stato', value: this.json.dettaglioStato }));
    _ai.push(new Dato({ label: 'Data richiesta', value: UtilService.defaultDisplay({ value: moment(this.json.dataRichiesta).format('DD/MM/YYYY') }) }));
    _ai.push(new Dato({ label: 'Data ricevuta', value: UtilService.defaultDisplay({ value: moment(this.json.dataRicevuta).format('DD/MM/YYYY') }) }));
    this.extra = _ai.slice(0);

  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.iuv:null });
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}
  refresh(mb: ModalBehavior) {}

}
