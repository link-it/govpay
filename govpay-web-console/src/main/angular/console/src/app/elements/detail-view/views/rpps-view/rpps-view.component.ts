import { Component, Input, OnInit } from '@angular/core';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { Dato } from '../../../../classes/view/dato';
import { Voce } from '../../../../services/voce.service';

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
    this.mapJsonDetail();
  }

  protected mapJsonDetail() {
    let _info = [];
    _info.push(new Dato({ label: Voce.IUV, value: this.json.rpt.datiVersamento.identificativoUnivocoVersamento }));
    _info.push(new Dato({ label: Voce.CCP, value: this.json.rpt.datiVersamento.codiceContestoPagamento }));
    this.informazioni = _info.slice(0);

    //Altre informazioni
    let _ai = [];
    _ai.push(new Dato({ label: Voce.ID_DOMINIO, value: this.json.rpt.dominio.identificativoDominio }));
    _ai.push(new Dato({ label: Voce.TIPO_VERSAMENTO, value: UtilService.TIPI_VERSAMENTO[this.json.rpt.datiVersamento.tipoVersamento] }));
    _ai.push(new Dato({ label: Voce.STATO, value: this._mapStato().stato }));
    _ai.push(new Dato({ label: Voce.DATA_RICHIESTA, value: UtilService.defaultDisplay({ value: moment(this.json.rpt.dataOraMessaggioRichiesta).format('DD/MM/YYYY') }) }));
    if(this.json.rt) {
      _ai.push(new Dato({ label: Voce.DATA_RICEVUTA, value: UtilService.defaultDisplay({ value: moment(this.json.rt.dataOraMessaggioRicevuta).format('DD/MM/YYYY') }) }));
    }
    this.extra = _ai.slice(0);

  }

  _mapStato(): any {
    let _map: any = { stato: '', motivo: '' };
    switch (this.json.stato) {
      case 'RT_ACCETTATA_PA':
        _map.stato = (this.json.rt)?UtilService.STATI_ESITO_PAGAMENTO[this.json.rt.datiPagamento.codiceEsitoPagamento]:'n/a';
        break;
      case 'RPT_RIFIUTATA_NODO':
      case 'RPT_RIFIUTATA_PSP':
      case 'RPT_ERRORE_INVIO_A_PSP':
        _map.stato = UtilService.STATI_RPP.FALLITO;
        _map.motivo = this.json.dettaglioStato+' - stato: '+this.json.stato;
        break;
      case 'RT_RIFIUTATA_PA':
      case 'RT_ESITO_SCONOSCIUTO_PA':
        _map.stato = UtilService.STATI_RPP.ANOMALO;
        _map.motivo = this.json.dettaglioStato+' - stato: '+this.json.stato;
        break;
      default:
        _map.stato = UtilService.STATI_RPP.IN_CORSO;
    }
    return _map;
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?(this.json.rt.istitutoAttestante?this.json.rt.istitutoAttestante.denominazioneAttestante:Voce.NO_PSP):null });
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}
  refresh(mb: ModalBehavior) {}

}
