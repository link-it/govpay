import { Component, Input, OnInit } from '@angular/core';

import * as moment from 'moment';
import { Dato } from '../../../../classes/view/dato';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { UtilService } from '../../../../services/util.service';
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
        //console.log(error);
        this.us.alert(error.message);
        this.gps.updateSpinner(false);
      });
  }

  protected mapJsonDetail() {
    //Riepilogo
    let _dvi = UtilService.defaultDisplay({ value: moment(this.json.dataFlusso).format('DD/MM/YYYY [ore] HH:mm') });
    let _dr = UtilService.defaultDisplay({ value: moment(this.json.dataRegolamento).format('DD/MM/YYYY [ore] HH:mm') });
    this.info = new Riepilogo({
      titolo: new Dato({ label: 'Data valuta incasso', value: UtilService.defaultDisplay({ value: _dvi }) }),
      sottotitolo: new Dato({ label: 'Id operazione di riversamento (TRN)', value: this.json.trn }),
      importo: this.json.importoTotale,
      extraInfo: [
        { label: 'Id rendicontazione: ', value: this.json.idFlusso },
        { label: 'Codice Bic riversamento: ', value: this.json.bicRiversamento },
        { label: 'Id PSP: ', value: this.json.idPsp },
        { label: 'Dominio: ', value: this.json.idDominio },
        { label: 'Numero di pagamenti: ', value: this.json.numeroPagamenti },
        { label: 'Data regolamento: ', value: UtilService.defaultDisplay({ value: _dr }) }
      ]
    });
    let _warn = [];
    if(this.json.segnalazioni) {
      this.json.segnalazioni.forEach((s) => {
        let _mappedElement = new Parameters();
        let _std: Standard = new Standard();
        _std.titolo = new Dato({ label: 'Codice: ', value: s.codice });
        _std.sottotitolo = new Dato({ label: 'Descrizione: ', value: s.descrizione });
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
        let _st = Dato.arraysToDato(
          ['Data esito', 'Riscossione (IUR)'],
          [ UtilService.defaultDisplay({ value: moment(item.data).format('DD/MM/YYYY') }), item.iur ],
          ', '
        );
        _std.titolo = new Dato({ label: '',  value: item.iuv });
        _std.sottotitolo = _st;
        _std.importo = item.importo;
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
