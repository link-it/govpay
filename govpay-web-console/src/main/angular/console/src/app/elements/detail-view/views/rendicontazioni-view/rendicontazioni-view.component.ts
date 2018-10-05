import { Component, Input, OnInit } from '@angular/core';

import * as moment from 'moment';
import { Dato } from '../../../../classes/view/dato';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { Standard } from '../../../../classes/view/standard';
import { Parameters } from '../../../../classes/parameters';
import { GovpayService } from '../../../../services/govpay.service';
import { Riepilogo } from '../../../../classes/view/riepilogo';

@Component({
  selector: 'link-rendicontazioni-view',
  templateUrl: './rendicontazioni-view.component.html',
  styleUrls: [ './rendicontazioni-view.component.scss' ]
})
export class RendicontazioniViewComponent implements IModalDialog, OnInit {

  @Input() segnalazioni = [];
  @Input() rendicontazioni = [];

  @Input() json: any;

  protected info: Riepilogo;

  constructor(private gps: GovpayService, private us: UtilService) {
  }

  ngOnInit() {
    this.dettaglioEvento();
  }

  protected dettaglioEvento() {
    let _url = UtilService.URL_RENDICONTAZIONI+'/'+encodeURIComponent(this.json.idFlusso);
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
    let _dvi = this.json.dataFlusso?moment(this.json.dataFlusso).format('DD/MM/YYYY [ore] HH:mm'):Voce.NON_PRESENTE;
    let _dr = this.json.dataRegolamento?moment(this.json.dataRegolamento).format('DD/MM/YYYY [ore] HH:mm'):Voce.NON_PRESENTE;
    this.info = new Riepilogo({
      titolo: new Dato({ label: Voce.DATA_VALUTA_INCASSO, value: _dvi }),
      sottotitolo: new Dato({ label: Voce.TRN, value: this.json.trn }),
      importo: this.us.currencyFormat(this.json.importoTotale),
      extraInfo: [
        { label: Voce.ID_FLUSSO+': ', value: this.json.idFlusso },
        { label: Voce.BIC_RIVERSAMENTO+': ', value: UtilService.defaultDisplay({ value: this.json.bicRiversamento }) },
        { label: Voce.ID_PSP+': ', value: this.json.idPsp },
        { label: Voce.ID_DOMINIO+': ', value: this.json.idDominio },
        { label: Voce.NUMERO_PAY+': ', value: this.json.numeroPagamenti },
        { label: Voce.DATA_REGOLAMENTO+': ', value: _dr }
      ]
    });
    let _warn = [];
    if(this.json.segnalazioni) {
      this.json.segnalazioni.forEach((s) => {
        let _mappedElement = new Parameters();
        let _std: Standard = new Standard();
        _std.titolo = new Dato({ label: Voce.CODICE+': ', value: s.codice });
        _std.sottotitolo = new Dato({ label: Voce.DESCRIZIONE+': ', value: s.descrizione });
        _mappedElement.model = _std;
        _mappedElement.jsonP = s;
        _warn.push(_mappedElement);
      });
    }
    this.segnalazioni = _warn.slice(0);

    //Rendicontazioni
    this.rendicontazioni = this.json.rendicontazioni.map(function(item) {
      let p = new Parameters();
      p.jsonP = item;
      p.model = this._mapNewItemByType(item, UtilService.URL_RENDICONTAZIONI);
      return p;
    }, this);
  }

  protected _mapNewItemByType(item: any, type: string): Standard {
    let _std = new Standard();
    switch(type) {
      case UtilService.URL_RENDICONTAZIONI:
        let _tmpData = item.data?moment(item.data).format('DD/MM/YYYY'):Voce.NON_PRESENTE;
        let _st = Dato.arraysToDato(
          [ Voce.DATA_ESITO, Voce.IUR ],
          [ _tmpData, item.iur ],
          ', '
        );
        _std.titolo = new Dato({ label: '',  value: item.iuv });
        _std.sottotitolo = _st;
        _std.importo = this.us.currencyFormat(item.importo);
        //_std.stato = UtilService.STATI_ESITO_RENDICONTAZIONI[item.esito];
        break;
    }
    return _std;
  }

  refresh(mb: ModalBehavior) {}
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.idFlusso:null });
  }

}
