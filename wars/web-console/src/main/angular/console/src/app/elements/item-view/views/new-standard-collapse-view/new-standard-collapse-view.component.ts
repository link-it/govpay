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
    if (this.info.elenco && this.info.motivo === 'force_elenco') {
      this._elenco = this.info.elenco;
    } else {
      const item = this.info.item;
      if (item) {
        this.info.stato = this.info.item.data ? moment(this.info.item.data).format('DD/MM/YYYY') : '';
        if (item.vocePendenza) { // Riconciliazioni/Pagamenti
          const vocePendenza = item.vocePendenza;
          const pendenza = vocePendenza.pendenza;

          let dominio;
          if (vocePendenza.dominio) {
            dominio = vocePendenza.dominio;
          } else {
            dominio = pendenza.dominio;
          }
          this._elenco.push({ label: Voce.ENTE_CREDITORE, value: `${dominio.ragioneSociale} (${dominio.idDominio})`, type: 'string' });
          this._elenco.push({ label: Voce.DEBITORE, value: `${pendenza.soggettoPagatore.anagrafica} (${pendenza.soggettoPagatore.identificativo})`, type: 'string' });
		  if(!vocePendenza.tipoBollo){
			if (vocePendenza.codEntrata) {
	              this._elenco.push({ label: Voce.CODICE_ENTRATA, value: vocePendenza.codEntrata, type: 'string' });
	              this.getEntrataRiconciliazione(dominio.idDominio, vocePendenza.codEntrata, vocePendenza);
	          } else {
	            let tipoContabilitaLabel =  UtilService.TIPI_CONTABILITA_NUMERICHE[vocePendenza.tipoContabilita];
	            this._elenco.push({ label: Voce.TASSONOMIA, value: Dato.concatStrings([tipoContabilitaLabel, vocePendenza.codiceContabilita], '/'), type: 'string' });
	            this.valorizzaDatiVocePendenza(vocePendenza);
			  }
		  } else {
			this._elenco.push({ label: Voce.ID_BOLLO, value: vocePendenza.tipoBollo , type: 'string' });
			this._elenco.push({ label: Voce.PROVINCIA, value: vocePendenza.provinciaResidenza, type: 'string'  });
			this._elenco.push({ label: Voce.HASH_DOCUMENTO, value: vocePendenza.hashDocumento });
			
			this.valorizzaDatiVocePendenza(vocePendenza);
		  }
        } else { // Pendenze/Dettaglio/Importi
          this._elenco.push({ label: Voce.ENTE_CREDITORE, value: `${item.dominio.ragioneSociale} (${item.dominio.idDominio})`, type: 'string' });
          if (!item.tipoBollo) {
            if (item.codEntrata) {
              this._elenco.push({ label: Voce.CODICE_ENTRATA, value: item.codEntrata, type: 'string' });
              if (item.idDominio) {
                this.getEntrata(item.idDominio, item.codEntrata, item);
              } else {
				this.valorizzaDatiVocePendenza(item);
			  }
            } else {
              let tipoContabilitaLabel =  UtilService.TIPI_CONTABILITA_NUMERICHE[item.tipoContabilita];
                this._elenco.push({ label: Voce.TASSONOMIA, value: Dato.concatStrings([tipoContabilitaLabel, item.codiceContabilita], '/'), type: 'string' });
                this._elenco.push({ label: Voce.CONTO_ACCREDITO, value: item.ibanAccredito, type: 'string' });
                if(item.ibanAppoggio){
                this._elenco.push({ label: Voce.CONTO_APPOGGIO, value: item.ibanAppoggio, type: 'string' });
              }
			  this.valorizzaDatiVocePendenza(item);
            }
          } else {
			this._elenco.push({ label: Voce.ID_BOLLO, value: item.tipoBollo , type: 'string' });
			this._elenco.push({ label: Voce.PROVINCIA, value: item.provinciaResidenza, type: 'string'  });
			this._elenco.push({ label: Voce.HASH_DOCUMENTO, value: item.hashDocumento });
			
			this.valorizzaDatiVocePendenza(item);
		  }
        }
      }
    }
  }

  protected getEntrata(idDominio, ideEntrata, vocePendenza) {
    // /domini/idDominio/entrate/idEntrata
    const _url = UtilService.URL_DOMINI + '/' + UtilService.EncodeURIComponent(idDominio) + '/' + UtilService.ENTRATE + '/' + ideEntrata;
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.gps.updateSpinner(false);
        if (_response.body) {
          const entrata = _response.body;
          if(entrata.tipoContabilita && entrata.codiceContabilita){
            let tipoContabilitaLabel =  UtilService.TIPI_CONTABILITA_NUMERICHE[entrata.tipoContabilita];
            this._elenco.push({ label: Voce.TASSONOMIA, value: Dato.concatStrings([tipoContabilitaLabel, entrata.codiceContabilita], '/'), type: 'string' });
          }
          this._elenco.push({ label: Voce.CONTO_ACCREDITO, value: entrata.ibanAccredito, type: 'string' });
          if(entrata.ibanAppoggio){
            this._elenco.push({ label: Voce.CONTO_APPOGGIO, value: entrata.ibanAppoggio, type: 'string' });
          }
		  this.valorizzaDatiVocePendenza(vocePendenza);
        }
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected getEntrataRiconciliazione(idDominio, idEntrata, vocePendenza) {
    // /domini/idDominio/entrate/idEntrata
    const _url = UtilService.URL_DOMINI + '/' + UtilService.EncodeURIComponent(idDominio) + '/' + UtilService.ENTRATE + '/' + idEntrata;
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.gps.updateSpinner(false);
        if (_response.body) {
          const entrata = _response.body;
          if(entrata.tipoContabilita && entrata.codiceContabilita){
            let tipoContabilitaLabel =  UtilService.TIPI_CONTABILITA_NUMERICHE[entrata.tipoContabilita];
            this._elenco.push({ label: Voce.TASSONOMIA, value: Dato.concatStrings([tipoContabilitaLabel, entrata.codiceContabilita], '/'), type: 'string' });
          }
		  this.valorizzaDatiVocePendenza(vocePendenza);
        }
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }
  
  protected valorizzaDatiVocePendenza(item) {
	// contabilita'
	if (item.contabilita && item.contabilita.quote) {
		this._elenco.push({ label: Voce.DETTAGLIO_CONTABILITA, value: item.contabilita.quote, type: 'quote' });
    }
    
	// Metadata
    if (item.metadata && item.metadata.mapEntries) {
    	const _mapEntries = item.metadata.mapEntries.map(x => { return { label: x.key, value: x.value } });
        this._elenco.push({ label: Voce.METADATA, value: _mapEntries, type: 'metadata' });
	}
	
	// dati allegati
	if(item.datiAllegati) {
		const _datiAllegati = item.datiAllegati;
		this._elenco.push({ label: Voce.DATI_CUSTOM, value: _datiAllegati, type: 'allegati' });
	}
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
