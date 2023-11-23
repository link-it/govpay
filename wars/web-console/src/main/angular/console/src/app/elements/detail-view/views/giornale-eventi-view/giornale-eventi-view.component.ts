import { Component, Input, OnInit } from '@angular/core';

import * as moment from 'moment';
import { Dato } from '../../../../classes/view/dato';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';
import { IExport } from '../../../../classes/interfaces/IExport';

@Component({
  selector: 'link-giornale-eventi-view',
  templateUrl: './giornale-eventi-view.component.html',
  styleUrls: ['./giornale-eventi-view.component.scss']
})
export class GiornaleEventiViewComponent implements IModalDialog, OnInit, IExport {

  @Input() informazioni = [];
  @Input() informazioniPA = [];

  @Input() json: any;

  protected _voce = Voce;
  protected infoPACompletaUrl: string = '';

  _json: any;
  dettaglioRichiesta = [];
  dettaglioRisposta = [];

  _tableAttributes: any[] = [
    { label: 'Nome', attribute: 'nome', col: 'col-4'},
    { label: 'Valore', attribute: 'valore', col: 'col-8'}
  ];

  _arrXML = ['API_PAGOPA', 'API_MAGGIOLI'];

  constructor(public gps: GovpayService, protected us: UtilService) { }

  ngOnInit() {
    this.dettaglioEvento();
    this._loadDetails();
  }

  protected dettaglioEvento() {
    let _dettaglio = [];
    let _dettaglioPA = [];
    this.infoPACompletaUrl = '';
    let _date = this.json.dataEvento?moment(this.json.dataEvento).format('DD/MM/YYYY [-] HH:mm:ss.SSS'):Voce.NON_PRESENTE;
    _dettaglio.push(new Dato({ label: Voce.CATEGORIA_EVENTO, value: UtilService.TIPI_CATEGORIA_EVENTO[this.json.categoriaEvento] }));
    _dettaglio.push(new Dato({ label: Voce.MODULO, value: this.us.mappaturaComponente(this.json.componente) }));
    _dettaglio.push(new Dato({ label: Voce.TIPO_EVENTO, value: this.us.mappaturaTipoEvento(this.json.componente, this.json.tipoEvento) }));
    _dettaglio.push(new Dato({ label: Voce.RUOLO, value: this.json.ruolo }));
    _dettaglio.push(new Dato({ label: Voce.DATA, value: _date }));
    _dettaglio.push(new Dato({ label: Voce.DURATA, value: this.json.durataEvento + 'ms' }));
    _dettaglio.push(new Dato({ label: Voce.ESITO, value: this.json.esito }));
    _dettaglio.push(new Dato({ label: Voce.SOTTOTIPO_ESITO, value: this.json.sottotipoEsito }));
    _dettaglio.push(new Dato({ label: Voce.DETTAGLIO_ESITO, value: UtilService.defaultDisplay({ value: this.json?this.json.dettaglioEsito:null }) }));
    _dettaglio.push(new Dato({ label: Voce.ID_DOMINIO, value: this.json.idDominio }));
    _dettaglio.push(new Dato({ label: Voce.IUV, value: this.json.iuv }));
    _dettaglio.push(new Dato({ label: Voce.CCP, value: this.json.ccp }));
    _dettaglio.push(new Dato({ label: Voce.ID_A2A, value: this.json.idA2A }));
    _dettaglio.push(new Dato({ label: Voce.ID_PENDENZA, value: this.json.idPendenza }));
    _dettaglio.push(new Dato({ label: Voce.ID_PAGAMENTO, value: this.json.idPagamento }));
    if(this.json.datiPagoPA) {
      _dettaglioPA.push(new Dato({label: Voce.ID_PSP, value: UtilService.defaultDisplay({ value: this.json.datiPagoPA.idPsp })}));
      _dettaglioPA.push(new Dato({label: Voce.ID_CANALE, value: UtilService.defaultDisplay({ value: this.json.datiPagoPA.idCanale })}));
      _dettaglioPA.push(new Dato({label: Voce.ID_INTERMEDIARIO_PSP, value: UtilService.defaultDisplay({ value: this.json.datiPagoPA.idIntermediarioPsp })}));
      _dettaglioPA.push(new Dato({label: Voce.TIPO_VERSAMENTO, value: UtilService.defaultDisplay({ value: UtilService.TIPI_VERSAMENTO[this.json.datiPagoPA.tipoVersamento] })}));
      _dettaglioPA.push(new Dato({label: Voce.MODELLO_PAGAMENTO, value: UtilService.defaultDisplay({ value: UtilService.MODELLI_PAGAMENTO[this.json.datiPagoPA.modelloPagamento] })}));
      _dettaglioPA.push(new Dato({label: Voce.ID_INTERMEDIARIO, value: UtilService.defaultDisplay({ value: this.json.datiPagoPA.idIntermediario })}));
      _dettaglioPA.push(new Dato({label: Voce.ID_STAZIONE, value: UtilService.defaultDisplay({ value: this.json.datiPagoPA.idStazione })}));
    }
    this.infoPACompletaUrl = ''; // UtilService.RootByTOA() + UtilService.URL_GIORNALE_EVENTI + '/' + this.json.id;
    this.informazioni = _dettaglio.slice(0);
    this.informazioniPA = _dettaglioPA.slice(0);
  }

  protected _loadDetails() {
    let _url = UtilService.URL_GIORNALE_EVENTI+'/'+this.json.id;
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this._json = _response.body;

        let _date = this._json.dataEvento?moment(this._json.dataEvento).format('DD/MM/YYYY [-] HH:mm:ss.SSS'):Voce.NON_PRESENTE;
        let _dettaglioRichiesta = [];
        if (this._json.parametriRichiesta) {
          if (this._json.parametriRichiesta.principal) {
            _dettaglioRichiesta.push(new Dato({ label: Voce.PRINCIPAL, value: this._json.parametriRichiesta.principal }));
          }
          _dettaglioRichiesta.push(new Dato({ label: Voce.UTENTE, value: this._json.parametriRichiesta.utente }));
          _date = this._json.parametriRichiesta.dataOraRichiesta?moment(this._json.parametriRichiesta.dataOraRichiesta).format('DD/MM/YYYY [-] HH:mm:ss.SSS'):Voce.NON_PRESENTE;
          _dettaglioRichiesta.push(new Dato({ label: Voce.DATA_RICHIESTA, value: _date }));
          _dettaglioRichiesta.push(new Dato({ label: Voce.URL, value: `${this._json.parametriRichiesta.method} ${this._json.parametriRichiesta.url}` }));
        }
        this.dettaglioRichiesta = _dettaglioRichiesta.slice(0);

        let _dettaglioRisposta = [];
        if (this._json.parametriRisposta) {
          _dettaglioRisposta.push(new Dato({ label: Voce.STATO, value: this._json.parametriRisposta.status }));
          _date = this._json.parametriRisposta.dataOraRisposta?moment(this._json.parametriRisposta.dataOraRisposta).format('DD/MM/YYYY [-] HH:mm:ss.SSS'):Voce.NON_PRESENTE;
          _dettaglioRisposta.push(new Dato({ label: Voce.DATA_RISPOSTA, value: _date }));
        }
        this.dettaglioRisposta = _dettaglioRisposta.slice(0);

        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  refresh(mb: ModalBehavior) {}
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}
  exportData() {}

  vistaCompletaEvento() {
    window.open(UtilService.RootByTOA() + UtilService.URL_GIORNALE_EVENTI + '/' + this.json.id, '_blank');
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.tipoEvento:null });
  }

  onCopied(event: boolean) {
    const message = event ? 'Payload copiato!' : 'Errore: Payload non copiato!';
    this.us.alert(message);
  }
}
