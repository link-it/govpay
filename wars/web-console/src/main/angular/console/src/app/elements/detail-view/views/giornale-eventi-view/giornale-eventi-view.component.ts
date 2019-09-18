import { Component, Input, OnInit } from '@angular/core';

import * as moment from 'moment';
import { Dato } from '../../../../classes/view/dato';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
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

  constructor(protected us: UtilService) { }

  ngOnInit() {
    this.dettaglioEvento();
  }

  protected dettaglioEvento() {
    let _dettaglio = [];
    let _dettaglioPA = [];
    this.infoPACompletaUrl = '';
    let _date = this.json.dataEvento?moment(this.json.dataEvento).format('DD/MM/YYYY [-] HH:mm:ss.SSS'):Voce.NON_PRESENTE;
    _dettaglio.push(new Dato({ label: Voce.CATEGORIA_EVENTO, value: UtilService.TIPI_CATEGORIA_EVENTO[this.json.categoriaEvento] }));
    _dettaglio.push(new Dato({ label: Voce.MODULO, value: this.json.componente }));
    _dettaglio.push(new Dato({ label: Voce.TIPO_EVENTO, value: this.us.mappaturaTipoEvento(this.json.tipoEvento) }));
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
    this.infoPACompletaUrl = UtilService.RootByTOA() + UtilService.URL_GIORNALE_EVENTI + '/' + this.json.id;
    this.informazioni = _dettaglio.slice(0);
    this.informazioniPA = _dettaglioPA.slice(0);
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
}
