import { AfterViewInit, Component, Input, OnInit, ViewEncapsulation } from '@angular/core';

import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';

import { Riepilogo } from '../../../../classes/view/riepilogo';
import { Dato } from '../../../../classes/view/dato';
import { Standard } from '../../../../classes/view/standard';
import { Parameters } from '../../../../classes/parameters';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { Crono } from '../../../../classes/view/crono';

import * as moment from 'moment';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';

@Component({
  selector: 'link-pendenze',
  templateUrl: './pendenze-view.component.html',
  styleUrls: ['./pendenze-view.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PendenzeViewComponent implements IModalDialog, OnInit, AfterViewInit {

  @Input() tentativi = [];
  @Input() importi = [];
  @Input() informazioni = [];

  @Input() json: any;
  @Input() modified: boolean = false;

  protected info: Riepilogo;
  protected _paymentsSum: number = 0;
  protected _expanded: boolean = false;

  constructor(public gps: GovpayService, public us: UtilService) {
  }

  ngOnInit() {
    this.dettaglioPendenza();
    this.elencoTentativi();
  }

  ngAfterViewInit() {
  }

  protected dettaglioPendenza(patch: boolean = false) {
    // /pendenze/idA2A/idPendenza
    let _url = UtilService.URL_PENDENZE+'/'+this.json.idA2A+'/'+this.json.idPendenza;
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.json = _response.body;
        this.mapJsonDetail();
        this._expanded = true;
        this.gps.updateSpinner(false);
        (patch)?this.us.alert('Aggiornamento stato completato'):null;
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
      sottotitolo: new Dato({ label: 'Numero avviso', value: this.json.numeroAvviso }),
      importo: this.json.importo,
      stato: UtilService.STATI_PENDENZE[this.json.stato],
      extraInfo: [ { label: 'ID: ', value: this.json.idPendenza } ]
    });
    if(UtilService.STATI_PENDENZE[this.json.stato] == UtilService.STATI_PENDENZE.ANNULLATO) {
      this.info.dataAnnullamento = UtilService.defaultDisplay({ value: moment(this.json.dataAnnullamento).format('DD/MM/YYYY') });
      this.info.causale = this.json.causale;
      this.info.cancel = true;
    }
    //Dettaglio importi
    this._paymentsSum = 0;
    this.importi = this.json.voci.map(function(item) {
      let _std = new Standard();
      _std.titolo = new Dato({ value: item.descrizione });
      _std.sottotitolo = new Dato({ label: 'ID: ', value: item.idVocePendenza });
      _std.importo = item.importo;
      this._paymentsSum += UtilService.defaultDisplay({ value: item.importo, text: 0 });
      let p = new Parameters();
      p.model = _std;
      return p;
    }, this);
    //Altre informazioni
    let _ai = [];
    if(this.json.soggettoPagatore && this.json.soggettoPagatore.anagrafica) {
      _ai.push(new Dato({ label: 'Tipologia soggetto', value: UtilService.TIPI_SOGGETTO[this.json.soggettoPagatore.tipo] }));
      _ai.push(new Dato({ label: 'Debitore', value: this.json.soggettoPagatore.anagrafica }));
      _ai.push(new Dato({ label: 'Codice fiscale', value: this.json.soggettoPagatore.identificativo }));
      _ai.push(new Dato({ label: 'Indirizzo', value: this.json.soggettoPagatore.indirizzo }));
      _ai.push(new Dato({ label: 'Numero civico', value: this.json.soggettoPagatore.civico }));
      _ai.push(new Dato({ label: 'Cap', value: this.json.soggettoPagatore.cap }));
      _ai.push(new Dato({ label: 'Località', value: this.json.soggettoPagatore.localita }));
      _ai.push(new Dato({ label: 'Provincia', value: this.json.soggettoPagatore.provincia }));
      _ai.push(new Dato({ label: 'Nazione', value: this.json.soggettoPagatore.nazione }));
      _ai.push(new Dato({ label: 'E-Mail', value: this.json.soggettoPagatore.email }));
      _ai.push(new Dato({ label: 'Cellulare', value: this.json.soggettoPagatore.cellulare }));
    }
    _ai.push(new Dato({ label: 'Data emissione', value: UtilService.defaultDisplay({ value: moment(this.json.dataCaricamento).format('DD/MM/YYYY') }) }));
    _ai.push(new Dato({ label: 'Data validità', value: UtilService.defaultDisplay({ value: moment(this.json.dataValidita).format('DD/MM/YYYY') }) }));
    _ai.push(new Dato({ label: 'Anno di riferimento', value: this.json.annoRiferimento }));
    _ai.push(new Dato({ label: 'Tassonomia avviso', value: this.json.tassonomiaAvviso }));
    _ai.push(new Dato({ label: 'Categoria', value: this.json.tassonomia }));
    this.informazioni = _ai.slice(0);
  }

  protected elencoTentativi() {
    this.gps.getDataService(this.json.rpp).subscribe(function (_response) {
        let _body = _response.body;
        this.tentativi = _body['risultati'].map(function(item) {
          let _date = UtilService.defaultDisplay({ value: moment(item.dataRichiesta).format('DD/MM/YYYY') });
          let _crn = new Crono();
          _crn.data = _date;
          _crn.titolo = new Dato({ label: 'IUV', value: item.iuv });
          _crn.sottotitolo = new Dato({ label: 'CCP', value: item.ccp });
          _crn.stato = UtilService.STATI_ESITO_PAGAMENTO[item.esito];
          let p = new Parameters();
          p.type = UtilService.CRONO;
          p.model = _crn;
          return p;
        }, this);
        this._expanded = true;
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        //console.log(error);
        this.us.alert(error.message);
        this.gps.updateSpinner(false);
      });
  }

  infoDetail(): any {
    return this.json;
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.nome:null });
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      this.modified = true;
      let _service = UtilService.URL_PENDENZE+'/'+this.json.idA2A+'/'+this.json.idPendenza;
      this.gps.saveData(_service, mb.info.viewModel, null, UtilService.METHODS.PATCH).subscribe(function () {
          this.gps.updateSpinner(false);
          this.dettaglioPendenza(true);
        }.bind(this),
        (error) => {
          //console.log(error);
          this.us.alert(error.message);
          this.gps.updateSpinner(false);
        });
    }
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}
}
