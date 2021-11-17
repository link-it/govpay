import { Input, Component, OnInit, AfterViewInit } from '@angular/core';
import { trigger, transition, style, animate } from '@angular/animations';
import { UtilService } from '../../../../services/util.service';
import { NewStandardCollapse } from '../../../../classes/view/new-standard-collapse';
import { Voce } from '../../../../services/voce.service';

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

  constructor(public us: UtilService) {
  }

  ngOnInit() {
    this.createElenco();
  }

  ngAfterViewInit() {
    //console.log(this.dataRef);
    //this.outEmitter.emit({ element: this });
  }

  createElenco() {
    const vocePendenza = this.info.item.vocePendenza;
    const pendenza = vocePendenza.pendenza;
    this._elenco.push({ label: Voce.ENTE_CREDITORE, value: `${pendenza.dominio.ragioneSociale} (${pendenza.dominio.idDominio})`, type: 'string' });
    this._elenco.push({ label: Voce.DEBITORE, value: `${pendenza.soggettoPagatore.anagrafica} (${pendenza.soggettoPagatore.identificativo})`, type: 'string' });
    this._elenco.push({ label: Voce.TIPI_PENDENZA, value: `${pendenza.causale}`, type: 'string' });
    this._elenco.push({ label: Voce.QUOTE, value: vocePendenza.contabilita.quote, type: 'quote' });
    this._elenco.push({ label: Voce.CONTENUTO_ALLEGATO, value: pendenza.datiAllegati, type: 'allegati' });
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
