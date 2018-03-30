import { AfterViewInit, Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { Riepilogo } from '../../../../classes/view/riepilogo';
import { GovpayService } from '../../../../services/govpay.service';
import { Dato } from '../../../../classes/view/dato';
import { Parameters } from '../../../../classes/parameters';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { Standard } from '../../../../classes/view/standard';
import { UtilService } from '../../../../services/util.service';

import * as moment from 'moment';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../classes/modal-behavior';

@Component({
  selector: 'link-pagamenti-view',
  templateUrl: './pagamenti-view.component.html',
  styleUrls: ['./pagamenti-view.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PagamentiViewComponent implements IModalDialog, OnInit, AfterViewInit {

  @Input() pagamenti = [];
  @Input() json: any;

  protected info: Riepilogo;
  protected _expanded: boolean = false;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioPagamento();
    this.elencoCarrello();
  }

  ngAfterViewInit() {
  }

  protected dettaglioPagamento() {
    //console.log('pagamento/detail', this.json);
    let _url = UtilService.URL_PAGAMENTI+'/'+this.json.id;
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.json = _response.body;
        this.mapJsonDetail();
        this.gps.updateSpinner(false);
        if(this.json.canale) {
          this.extraInfoPagamento(this.json.canale, true);
        } else {
          this._expanded = true;
        }
      }.bind(this),
      (error) => {
        //console.log(error);
        this.us.alert(error.message);
        this.gps.updateSpinner(false);
      });
  }

  protected mapJsonDetail() {
    //Riepilogo
    this.info = new Riepilogo({
      titolo: new Dato({ label: 'Scadenza', value: UtilService.defaultDisplay({ value: moment(this.json.dataScadenza).format('DD/MM/YYYY') }) }),
      sottotitolo: new Dato({ label: 'C.F. versante', value: this.json.soggettoVersante?this.json.soggettoVersante.identificativo:null }),
      importo: this.json.importo,
      stato: UtilService.STATI_PAGAMENTO[this.json.stato],
      extraInfo: [
        { label: 'Data richiesta pagamento: ', value: UtilService.defaultDisplay({ value: moment(this.json.dataRichiestaPagamento).format('DD/MM/YYYY') }) },
        { label: 'Data esecuzione pagamento: ', value: UtilService.defaultDisplay({ value: moment(this.json.dataEsecuzionePagamento).format('DD/MM/YYYY') }) }
      ]
    });
    if(this.json.soggettoVersante) {
      this.info.extraInfo.unshift({ label: 'Nome versante: ', value: this.json.soggettoVersante.anagrafica });
    }
  }

  protected extraInfoPagamento(_url: string, _canale: boolean) {
    this.gps.getDataService(_url).subscribe(function (_response) {
        let _jsonExtra = _response.body;
        //ExtraInfo Riepilogo: Canale|Psp
        if(_canale) {
          this.info.extraInfo.push({ label: 'Tipologia: ', value: UtilService.TIPI_VERSAMENTO[_jsonExtra.tipoVersamento] });
          this.extraInfoPagamento(_jsonExtra.psp, false);
        } else {
          this.info.extraInfo.push({ label: 'Istituto: ', value: _jsonExtra.ragioneSociale });
          this._expanded = true;
        }
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        //console.log(error);
        this.us.alert(error.message);
        this._expanded = true;
        this.gps.updateSpinner(false);
      });
  }

  protected elencoCarrello() {
    this.gps.getDataService(this.json.rpp).subscribe(function (_response) {
        let _body = _response.body;
        let _urlPendenze: string[] = [];
        this.pagamenti = _body['risultati'].map(function(item) {
          let _st = Dato.arraysToDato(['IUV', 'CCP'], [item.iuv, item.ccp]);
          let _std = new Standard();
          _std.titolo = new Dato();
          _std.sottotitolo = _st;
          _std.stato = UtilService.STATI_ESITO_PAGAMENTO[item.esito];
          let p = new Parameters();
          p.model = _std;
          p.jsonP = item.pendenza;
          _urlPendenze.push(item.pendenza);
          return p;
        }, this);
        this.gps.updateSpinner(false);
        this.pendenzaPagamentoCarrello(_urlPendenze);
      }.bind(this),
      (error) => {
        //console.log(error);
        this.us.alert(error.message);
        this.gps.updateSpinner(false);
      });
  }

  protected pendenzaPagamentoCarrello(_urlPendenze: string[]) {
    _urlPendenze.forEach((s) => {
      this.gps.getDataService(s).subscribe(function (_response) {
          let _jsonPPC = _response.body;
          this.pagamenti.map((p) => {
            if(p.jsonP == s) {
              p.model.titolo = new Dato({ value: _jsonPPC.nome });
              p.jsonP = null;
            }
          });
          this.gps.updateSpinner(false);
        }.bind(this),
        (error) => {
          //console.log(error);
          this.us.alert(error.message);
          this.gps.updateSpinner(false);
        });
    });
  }

  infoDetail(): any {
    return this.json;
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.id:null });
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}
  refresh(mb: ModalBehavior) {}
}
