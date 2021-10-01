import { AfterViewInit, Component, Input } from '@angular/core';

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
export class RiscossioniViewComponent implements IModalDialog, AfterViewInit {

  @Input() informazioni = [];

  @Input() json: any;


  constructor(private gps: GovpayService, private us: UtilService) { }

  ngAfterViewInit() {
    setTimeout(() => {
      this.dettaglioEvento();
    });
  }

  protected dettaglioEvento() {
    let _url = UtilService.URL_RISCOSSIONI+'/'+this.json.idDominio+'/'+UtilService.EncodeURIComponent(this.json.iuv)+'/'+UtilService.EncodeURIComponent(this.json.iur)+'/'+UtilService.EncodeURIComponent(this.json.indice.toString());
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
    if(this.json.iuv) {
      _dettaglio.push(new Dato({ label: Voce.IUV, value: this.json.iuv }));
    }
    if(this.json.iur) {
      _dettaglio.push(new Dato({ label: Voce.IUR, value: this.json.iur }));
    }
    _dettaglio.push(new Dato({ label: Voce.IMPORTO, value: this.us.currencyFormat(this.json.importo) }));
    let _date = this.json.data?moment(this.json.data).format('DD/MM/YYYY [ore] HH:mm'):Voce.NON_PRESENTE;
    _dettaglio.push(new Dato({ label: Voce.DATA_ESECUZIONE_RISCOSSIONE, value: _date }));
    if(this.json.idVocePendenza) {
      _dettaglio.push(new Dato({ label: Voce.ID_PENDENZA, value: this.json.idVocePendenza }));
    }
    _dettaglio.push(new Dato({ label: Voce.INDICE_PENDENZA, value: this.json.indice }));
    if(this.json.idDominio) {
      _dettaglio.push(new Dato({ label: Voce.ID_DOMINIO, value: this.json.idDominio }));
    }
    _dettaglio.push(new Dato({ label: Voce.COMMISSIONI, value: this.us.currencyFormat(this.json.commissioni) }));
    _dettaglio.push(new Dato({ label: Voce.STATO, value: UtilService.STATI_RISCOSSIONE[this.json.stato] }));
    _dettaglio.push(new Dato({ label: Voce.TIPO_RISCOSSIONE, value: UtilService.TIPI_RISCOSSIONE[this.json.tipo] }));
    if(this.json.incasso) {
      _dettaglio.push(new Dato({ label: Voce.INCASSO, value: UtilService.defaultDisplay({ value:this.json.incasso }) }));
    }
    //_dettaglio.push(new Dato({ label: 'Url pendenza', value: this.json.pendenza }));
    //_dettaglio.push(new Dato({ label: 'Url rpp', value: this.json.rpp }));
    if(this.json.allegato && this.json.allegato.contenuto && this.json.allegato.contenuto.TipoBollo) {
      _dettaglio.push(new Dato({ label: Voce.TIPO_ALLEGATO, value: this.json.allegato.tipo }));
      _dettaglio.push(new Dato({ label: Voce.ID_BOLLO, value: this.json.allegato.contenuto.IUBD }));
    }
    this.informazioni = _dettaglio.slice(0);
  }

  refresh(mb: ModalBehavior) {}
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}


  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.iur:null });
  }

}
