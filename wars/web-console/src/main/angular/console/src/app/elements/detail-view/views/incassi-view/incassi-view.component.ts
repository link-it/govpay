import { Component, Input, OnInit } from '@angular/core';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';
import { Dato } from '../../../../classes/view/dato';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

import * as moment from 'moment';
import { Riepilogo } from '../../../../classes/view/riepilogo';
import { Parameters } from '../../../../classes/parameters';
import { Standard } from '../../../../classes/view/standard';

@Component({
  selector: 'link-incassi-view',
  templateUrl: './incassi-view.component.html',
  styleUrls: ['./incassi-view.component.scss']
})
export class IncassiViewComponent implements IModalDialog, OnInit {

  @Input() informazioni = [];
  @Input() riscossioni = [];

  @Input() json: any;

  protected info: Riepilogo;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioIncasso();
  }

  protected dettaglioIncasso() {
    let _url = UtilService.URL_INCASSI+'/'+this.json.idDominio+'/'+this.json.idIncasso;
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
    let _dvi = this.json.dataValuta?moment(this.json.dataValuta).format('DD/MM/YYYY'):Voce.NON_PRESENTE;
    let _dci = this.json.dataContabile?moment(this.json.dataContabile).format('DD/MM/YYYY'):Voce.NON_PRESENTE;
    this.info = new Riepilogo({
      titolo: new Dato({ label: Voce.DATA_VALUTA_INCASSO, value: _dvi }),
      sottotitolo: new Dato({ label: Voce.ID_INCASSO, value: this.json.idIncasso }),
      importo: this.us.currencyFormat(this.json.importo),
      extraInfo: [
        { label: Voce.CAUSALE+': ', value: this.json.causale },
        { label: Voce.DATA_CONTABILE+': ', value: _dci },
        { label: Voce.SCT+': ', value: this.json.sct }
      ]
    });

    //Riscossioni
    this.riscossioni = this.json.riscossioni.map(function(item) {
      let p = new Parameters();
      p.jsonP = item;
      p.model = this._mapNewItemByType(item, UtilService.URL_RISCOSSIONI);
      return p;
    }, this);
  }

  protected _mapNewItemByType(item: any, type: string): Standard {
    let _std = new Standard();
    switch(type) {
      case UtilService.URL_RISCOSSIONI:
        let _st = Dato.arraysToDato(
          [ Voce.ID_PENDENZA, Voce.IUV, Voce.ID_DOMINIO ],
          [ item.idVocePendenza, item.iuv, item.idDominio ],
          ', '
        );
        _std.titolo = new Dato({ label: Voce.IUR+': ', value: item.iur });
        _std.sottotitolo = _st;
        _std.importo = this.us.currencyFormat(item.importo);
        break;
    }
    return _std;
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let _service = null;
    let _json = null;
    let _method = null;
    switch(mb.info.templateName) {
      case UtilService.INCASSO:
        _service = UtilService.URL_INCASSI + '/' + encodeURIComponent(mb.info.viewModel['idDominio']);
        _json = mb.info.viewModel;
        delete _json.idDominio;
        _method = UtilService.METHODS.POST;
        break;
    }
    if(_json && _service) {
      this.gps.saveData(_service, _json, null, _method).subscribe(
        (response) => {
          this.gps.updateSpinner(false);
          mb.info.viewModel = response.body;
          responseService.next(true);
        },
        (error) => {
          this.gps.updateSpinner(false);
          this.us.onError(error);
        });
    }
  }

  refresh(mb: ModalBehavior) {}

  title(): string {
    return this.json?('Incasso '+this.json.idIncasso):'';
  }
}
