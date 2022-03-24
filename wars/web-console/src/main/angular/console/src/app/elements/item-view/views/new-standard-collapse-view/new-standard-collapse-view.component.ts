import { Input, Component, OnInit, AfterViewInit } from '@angular/core';
import { trigger, transition, style, animate } from '@angular/animations';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { NewStandardCollapse } from '../../../../classes/view/new-standard-collapse';
import { Voce } from '../../../../services/voce.service';
import { Dato } from '../../../../classes/view/dato';

import * as moment from 'moment';

@Component({
  selector: 'link-new-standard-collapse-view',
  templateUrl: './new-standard-collapse-view.component.html',
  styleUrls: ['./new-standard-collapse-view.component.scss'],
  animations: [
    trigger('slideDownUp', [
      transition(':enter', [style({ height: 0 }), animate(200, style({ height: 200 }))]),
      transition(':leave', [animate(200, style({ height: 0 }))]),
    ])
  ]
})
export class NewStandardCollapseViewComponent implements OnInit, AfterViewInit {

  @Input() dataRef: any;

  @Input('info') info: NewStandardCollapse = new NewStandardCollapse();

  _expanded: boolean = false;

  _elenco: any[] = [];

  constructor(public gps: GovpayService, public us: UtilService) {
  }

  ngOnInit() {
    this.createElenco();
  }

  ngAfterViewInit() {
    //console.log(this.dataRef);
    //this.outEmitter.emit({ element: this });
  }

  createElenco() {
    const item = this.info.item;
    if (item) {
      this.info.stato = this.info.item.data ? moment(this.info.item.data).format('DD/MM/YYYY') : '';
      if (item.vocePendenza) { // Riconciliazioni/Pagamenti
        const vocePendenza = item.vocePendenza;
        const pendenza = vocePendenza.pendenza;
        this._elenco.push({ label: Voce.ENTE_CREDITORE, value: `${pendenza.dominio.ragioneSociale} (${pendenza.dominio.idDominio})`, type: 'string' });
        this._elenco.push({ label: Voce.DEBITORE, value: `${pendenza.soggettoPagatore.anagrafica} (${pendenza.soggettoPagatore.identificativo})`, type: 'string' });
        this._elenco.push({ label: Voce.TIPO_PENDENZA, value: `${pendenza.tipoPendenza.idTipoPendenza} - ${pendenza.tipoPendenza.descrizione}`, type: 'string' });
        if (vocePendenza.contabilita && vocePendenza.contabilita.quote) {
          this._elenco.push({ label: Voce.QUOTE, value: vocePendenza.contabilita.quote, type: 'quote' });
        }
        if (pendenza.datiAllegati) {
          this._elenco.push({ label: Voce.CONTENUTO_ALLEGATO, value: pendenza.datiAllegati, type: 'allegati' });
        }
      } else { // Pendenze/Dettaglio/Importi
        if (!item.tipoBollo) {
          if (item.codEntrata) {
            this._elenco.push({ label: Voce.CODICE_ENTRATA, value: item.codEntrata, type: 'string' });
            if (item.idDominio) {
              this.getEntrata(item.idDominio, item.codEntrata);
            }
          } else {
            this._elenco.push({ label: Voce.CONTABILITA, value: Dato.concatStrings([item.tipoContabilita, item.codiceContabilita], ', '), type: 'string' });
            this._elenco.push({ label: Voce.CONTO_ACCREDITO, value: item.ibanAccredito, type: 'string' });
            if (item.contabilita && item.contabilita.quote) {
              this._elenco.push({ label: Voce.QUOTE, value: item.contabilita.quote, type: 'quote' });
            }
          }
        }
      }
    }
  }

  protected getEntrata(idDominio, ideEntrata) {
    // /domini/idDominio/entrate/idEntrata
    const _url = UtilService.URL_DOMINI + '/' + UtilService.EncodeURIComponent(idDominio) + '/' + UtilService.ENTRATE + '/' + ideEntrata;
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.gps.updateSpinner(false);
        if (_response.body) {
          const entrata = _response.body;
          this._elenco.push({ label: Voce.CONTABILITA, value: Dato.concatStrings([entrata.tipoContabilita, entrata.codiceContabilita], ', '), type: 'string' });
          this._elenco.push({ label: Voce.CONTO_ACCREDITO, value: entrata.ibanAccredito, type: 'string' });
          if (this.info.item.contabilita && this.info.item.contabilita.quote) {
            this._elenco.push({ label: Voce.QUOTE, value: this.info.item.contabilita.quote, type: 'quote' });
          }
        }
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  // protected _hasStato(): boolean {
  //   return (this.info && this.info.stato !== null);
  // }

  // protected _titleStyle() {
  //   let _trunc: boolean = true;
  //   if((this.info.titolo && this.info.titolo.label && this.info.titolo.label.indexOf(' ') != -1) ||
  //     (this.info.titolo && this.info.titolo.value && this.info.titolo.value.indexOf(' ') != -1)) {
  //     _trunc = false;
  //   }
  //   return {
  //     'text-truncate': _trunc,
  //     'medium-16': true
  //   }
  // }

  // protected _subTitleStyle() {
  //   let _trunc: boolean = true;
  //   if((this.info.sottotitolo && this.info.sottotitolo.label && this.info.sottotitolo.label.indexOf(' ') != -1) ||
  //     (this.info.sottotitolo && this.info.sottotitolo.value && this.info.sottotitolo.value.indexOf(' ') != -1)) {
  //     _trunc = false;
  //   }
  //   return {
  //     'text-truncate': _trunc,
  //     'color-gray': true,
  //     'medium-14': true
  //   }
  // }

  protected _collapse() {
    if (this.info.motivo || (this.info.elenco && this.info.elenco.length != 0)) {
      this._expanded = !this._expanded;
    }
  }
}
