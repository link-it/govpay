import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { GovpayService } from '../../../../../../services/govpay.service';
import { Parameters } from '../../../../../../classes/parameters';
import { Standard } from '../../../../../../classes/view/standard';
import { Dato } from '../../../../../../classes/view/dato';

@Component({
  selector: 'link-tipi-pendenza-dominio-view',
  templateUrl: './tipi-pendenza-dominio-view.component.html',
  styleUrls: ['./tipi-pendenza-dominio-view.component.scss']
})
export class TipiPendenzaDominioViewComponent implements IFormComponent,  OnInit, AfterViewInit {
  @ViewChild('iSchemaBrowse') _iSchemaBrowse: ElementRef;
  @ViewChild('iLayoutBrowse') _iLayoutBrowse: ElementRef;

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() modified: boolean = false;
  @Input() parent: any;

  protected tipiPendenza_items: any[];

  protected _voce = Voce;

  protected _generatori: any[] = UtilService.GENERATORI;
  protected _applicazioni: any[] = [];
  protected _doubleSet: any = {
    visualizzazione: false,
    schema: false,
    validazione: false,
    definizione: false,
    oggetto: false,
    messaggio: false,
    oggettoRicevuta: false,
    messaggioRicevuta: false,
    shadow_visualizzazione_ctrl: new FormControl(''),
    shadow_schema_ctrl: new FormControl(''),
    shadow_validazione_ctrl: new FormControl(''),
    shadow_definizione_ctrl: new FormControl(''),
    shadow_oggetto_ctrl: new FormControl(''),
    shadow_messaggio_ctrl: new FormControl(''),
    shadow_oggettoRicevuta_ctrl: new FormControl(''),
    shadow_messaggioRicevuta_ctrl: new FormControl('')
  };

  constructor(public gps: GovpayService, public us: UtilService) {
    this._getTipiPendenza();
    this._elencoApplicazioni();
  }

  ngOnInit() {
    this.fGroup.addControl('tipoPendenza_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('codificaIUV_ctrl', new FormControl(''));
    this.fGroup.addControl('abilita_ctrl', new FormControl(null));
    this.fGroup.addControl('pagaTerzi_ctrl', new FormControl(null));

    this.fGroup.addControl('visualizzazione_ctrl', new FormControl(''));
    this.fGroup.addControl('generatore_ctrl', new FormControl(''));
    this.fGroup.addControl('schema_ctrl', new FormControl(''));

    this.fGroup.addControl('validazione_ctrl', new FormControl(''));
    this.fGroup.addControl('tipoTrasformazione_ctrl', new FormControl(''));
    this.fGroup.addControl('definizione_ctrl', new FormControl(''));
    this.fGroup.addControl('inoltro_ctrl', new FormControl(''));

    this.fGroup.addControl('tipoTemplateAP_ctrl', new FormControl(''));
    this.fGroup.addControl('oggetto_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggio_ctrl', new FormControl(''));
    this.fGroup.addControl('allegaPdf_ctrl', new FormControl(null));

    this.fGroup.addControl('tipoTemplateAR_ctrl', new FormControl(''));
    this.fGroup.addControl('oggettoRicevuta_ctrl', new FormControl(''));
    this.fGroup.addControl('messaggioRicevuta_ctrl', new FormControl(''));
    this.fGroup.addControl('allegaPdfRicevuta_ctrl', new FormControl(null));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['tipoPendenza_ctrl'].disable();
        this.fGroup.controls['tipoPendenza_ctrl'].setValue((this.json)?this.json:'');
        this._updateValues(this.json);
      }
    });
  }

  protected _onChangeSelection(target) {
    this.fGroup.reset();
    this.fGroup.controls['tipoPendenza_ctrl'].setValue(target.value);
    this._updateValues(target.value);
  }

  protected _tipoPendenzaComparingFct(option: any, selection: any): boolean {
    return (selection && option.idTipoPendenza == selection.idTipoPendenza);
  }

  /**
   * Filter by list and key to {label,value} object mapped list
   * @param {any[]} fullList
   * @param {Parameters[]} checkList
   * @param {string} key
   * @returns { any[] }
   */
  protected filterByList(fullList: any[], checkList: Parameters[], key: string): any[] {
    return fullList.filter((item) => {
      let _keep: boolean = true;
      checkList.forEach((el) => {
        if(el.jsonP[key] == item.jsonP[key]) {
          _keep = false;
        }
      });
      return _keep;
    });
  }

  /**
   * Check item index list by key
   * @param item: json item
   * @param {Parameters[]} checkList
   * @param {string} key
   * @returns {boolean}
   */
  // protected checkItemIndex(item: any, checkList: Parameters[], key: string): boolean {
  //   let _hasEntry: boolean = false;
  //   checkList.forEach((el) => {
  //     if(el.jsonP[key] == item[key]) {
  //       _hasEntry = true;
  //     }
  //   });
  //   return _hasEntry;
  // }

  protected _elencoApplicazioni() {
    this.gps.getDataService(UtilService.URL_APPLICAZIONI).subscribe(
      (response) => {
        this._applicazioni = response.body?(response.body.risultati || []):[];
        this.gps.updateSpinner(false);
      },
      (error) => {
        this._applicazioni = [];
        this.gps.updateSpinner(false);
        this.us.onError(error);
      }
    );
  }

  protected _lfsChange(event: any, controller: string) {
    if(event.type == 'file-selector-change') {
      if(event.value) {
        this.fGroup.controls[controller].setValidators([ Validators.required ]);
      } else {
        this.fGroup.controls[controller].clearValidators();
      }
      this.fGroup.controls[controller].updateValueAndValidity({ onlySelf: true });
    }
  }

  protected _lfsReset(event: any, controller: string) {
    if(event.type == 'file-selector-reset') {
      this.fGroup.controls[controller].setValue(this._doubleSet['shadow_'+controller].value);
    }
  }

  protected _getTipiPendenza() {
    let _service = UtilService.URL_TIPI_PENDENZA;
    this.gps.getDataService(_service).subscribe(
    (_response) => {
      let _body = _response.body;
      let p: Parameters;
      let _dtp = _body['risultati'].map(function(item) {
        p = new Parameters();
        p.jsonP = item;
        p.model = this.mapNewItem(item);
        return p;
      }, this);
      if(!this.json) {
        //Insert mode
        this.tipiPendenza_items = this.filterByList(_dtp.slice(0), this.parent.tipiPendenza, 'idTipoPendenza');
      } else {
        //Edit mode
        this.tipiPendenza_items = _dtp;
      }
      this.gps.updateSpinner(false);
    },
    (error) => {
      this.gps.updateSpinner(false);
      this.us.onError(error);
    });
  }

  protected mapNewItem(item: any): Standard {
    let _std = new Standard();
    _std.titolo = new Dato({ label: item.descrizione, value: '' });
    _std.sottotitolo = new Dato({ label: Voce.ID_TIPO_PENDENZA+': ', value: item.idTipoPendenza });
    return _std;
  }

  protected _updateValues(json: any) {
    if(json.valori && json.valori.visualizzazione) {
      this._doubleSet.visualizzazione = false;
    } else {
      this._doubleSet.visualizzazione = true;
    }
    if(json.valori && json.valori.form && json.valori.form.definizione) {
      this._doubleSet.schema = false;
    } else {
      this._doubleSet.schema = true;
    }
    if(json.valori && json.valori.validazione) {
      this._doubleSet.validazione = false;
    } else {
      this._doubleSet.validazione = true;
    }
    if(json.valori && json.valori.trasformazione && json.valori.trasformazione.definizione) {
      this._doubleSet.definizione = false;
    } else {
      this._doubleSet.definizione = true;
    }
    if(json.valori && json.valori.promemoriaAvviso && json.valori.promemoriaAvviso.oggetto) {
      this._doubleSet.oggetto = false;
    } else {
      this._doubleSet.oggetto = true;
    }
    if(json.valori && json.valori.promemoriaAvviso && json.valori.promemoriaAvviso.messaggio) {
      this._doubleSet.messaggio = false;
    } else {
      this._doubleSet.messaggio = true;
    }
    if(json.valori && json.valori.promemoriaRicevuta && json.valori.promemoriaRicevuta.oggetto) {
      this._doubleSet.oggettoRicevuta = false;
    } else {
      this._doubleSet.oggettoRicevuta = true;
    }
    if(json.valori && json.valori.promemoriaRicevuta && json.valori.promemoriaRicevuta.messaggio) {
      this._doubleSet.messaggioRicevuta = false;
    } else {
      this._doubleSet.messaggioRicevuta = true;
    }
    this._doubleSet[ 'shadow_visualizzazione_ctrl' ].setValue(json.visualizzazione?json.visualizzazione:'');
    this._doubleSet[ 'shadow_schema_ctrl' ].setValue(json.form?json.form.definizione:'');
    this._doubleSet[ 'shadow_validazione_ctrl' ].setValue(json.validazione || '');
    this._doubleSet[ 'shadow_definizione_ctrl' ].setValue(json.trasformazione?json.trasformazione.definizione:'');
    this._doubleSet[ 'shadow_oggetto_ctrl' ].setValue(json.promemoriaAvviso?json.promemoriaAvviso.oggetto:'');
    this._doubleSet[ 'shadow_messaggio_ctrl' ].setValue(json.promemoriaAvviso?json.promemoriaAvviso.messaggio:'');
    this._doubleSet[ 'shadow_oggettoRicevuta_ctrl' ].setValue(json.promemoriaRicevuta?json.promemoriaRicevuta.oggettoRicevuta:'');
    this._doubleSet[ 'shadow_messaggioRicevuta_ctrl' ].setValue(json.promemoriaRicevuta?json.promemoriaRicevuta.messaggioRicevuta:'');

    setTimeout(() => {
      if (json.valori && json.valori.codificaIUV) {
        this.fGroup.controls[ 'codificaIUV_ctrl' ].setValue(json.valori.codificaIUV);
      }
      if (json.valori) {
        this.fGroup.controls[ 'abilita_ctrl' ].setValue(json.valori.abilitato);
      }
      if (json.valori) {
        this.fGroup.controls[ 'pagaTerzi_ctrl' ].setValue(json.valori.pagaTerzi);
      }
      if (json.valori && json.valori.form && json.valori.form.tipo) {
        this.fGroup.controls[ 'generatore_ctrl' ].setValue(json.valori.form.tipo || '');
      }
      if (json.valori && json.valori.visualizzazione) {
        this.fGroup.controls[ 'visualizzazione_ctrl' ].setValue(json.valori.visualizzazione || '');
      } else {
        this.fGroup.controls[ 'visualizzazione_ctrl' ].setValue(json.visualizzazione?json.visualizzazione:'');
      }
      if (json.valori && json.valori.form && json.valori.form.definizione) {
        this.fGroup.controls[ 'schema_ctrl' ].setValue(json.valori.form.definizione || '');
      } else {
        this.fGroup.controls[ 'schema_ctrl' ].setValue(json.form?json.form.definizione:'');
      }
      if (json.valori && json.valori.validazione) {
        this.fGroup.controls[ 'validazione_ctrl' ].setValue(json.valori.validazione);
      } else {
        this.fGroup.controls[ 'validazione_ctrl' ].setValue(json.validazione || '');
      }
      if (json.valori && json.valori.trasformazione && json.valori.trasformazione.tipo) {
        this.fGroup.controls[ 'tipoTrasformazione_ctrl' ].setValue(json.valori.trasformazione.tipo);
      }
      if (json.valori && json.valori.trasformazione && json.valori.trasformazione.definizione) {
        this.fGroup.controls[ 'definizione_ctrl' ].setValue(json.valori.trasformazione.definizione);
      } else {
        this.fGroup.controls[ 'definizione_ctrl' ].setValue(json.trasformazione?json.trasformazione.definizione:'');
      }
      if (json.valori && json.valori.inoltro) {
        this.fGroup.controls[ 'inoltro_ctrl' ].setValue(json.valori.inoltro);
      }
      if (json.valori && json.valori.promemoriaAvviso && json.valori.promemoriaAvviso.tipo) {
        this.fGroup.controls[ 'tipoTemplateAP_ctrl' ].setValue(json.valori.promemoriaAvviso.tipo);
      }
      if (json.valori && json.valori.promemoriaAvviso && json.valori.promemoriaAvviso.oggetto) {
        this.fGroup.controls[ 'oggetto_ctrl' ].setValue(json.valori.promemoriaAvviso.oggetto);
      } else {
        this.fGroup.controls[ 'oggetto_ctrl' ].setValue(json.promemoriaAvviso?json.promemoriaAvviso.oggetto:'');
      }
      if (json.valori && json.valori.promemoriaAvviso && json.valori.promemoriaAvviso.messaggio) {
        this.fGroup.controls[ 'messaggio_ctrl' ].setValue(json.valori.promemoriaAvviso.messaggio);
      } else {
        this.fGroup.controls[ 'messaggio_ctrl' ].setValue(json.promemoriaAvviso?json.promemoriaAvviso.messaggio:'');
      }
      if (json.valori && json.valori.promemoriaAvviso) {
        this.fGroup.controls[ 'allegaPdf_ctrl' ].setValue(json.valori.promemoriaAvviso.allegaPdf);
      }
      if (json.valori && json.valori.promemoriaRicevuta && json.valori.promemoriaRicevuta.tipo) {
        this.fGroup.controls[ 'tipoTemplateAR_ctrl' ].setValue(json.valori.promemoriaRicevuta.tipo);
      }
      if (json.valori && json.valori.promemoriaRicevuta && json.valori.promemoriaRicevuta.oggetto) {
        this.fGroup.controls[ 'oggettoRicevuta_ctrl' ].setValue(json.valori.promemoriaRicevuta.oggetto);
      } else {
        this.fGroup.controls[ 'oggettoRicevuta_ctrl' ].setValue(json.promemoriaRicevuta?json.promemoriaRicevuta.oggetto:'');
      }
      if (json.valori && json.valori.promemoriaRicevuta && json.valori.promemoriaRicevuta.messaggio) {
        this.fGroup.controls[ 'messaggioRicevuta_ctrl' ].setValue(json.valori.promemoriaRicevuta.messaggio);
      } else {
        this.fGroup.controls[ 'messaggioRicevuta_ctrl' ].setValue(json.promemoriaRicevuta?json.promemoriaRicevuta.messaggio:'');
      }
      if (json.valori && json.valori.promemoriaRicevuta) {
        this.fGroup.controls[ 'allegaPdfRicevuta_ctrl' ].setValue(json.valori.promemoriaRicevuta.allegaPdf);
      }
    });

  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    if(!this.fGroup.controls['tipoPendenza_ctrl'].disabled) {
      _json = _info['tipoPendenza_ctrl'];
    } else {
      _json = this.json;
    }
    _json.valori = {
      pagaTerzi: (_info['pagaTerzi_ctrl'] !== undefined)?_info['pagaTerzi_ctrl']:null,
      abilitato: (_info['abilita_ctrl'] !== undefined)?_info['abilita_ctrl']:null,
      codificaIUV: _info['codificaIUV_ctrl'],
      visualizzazione: _info['visualizzazione_ctrl'] || null,
      form: {
        tipo: _info['generatore_ctrl'] || null,
        definizione: _info['schema_ctrl'] || null
      },
      validazione: _info['validazione_ctrl'] || null,
      trasformazione: {
        tipo: _info['tipoTrasformazione_ctrl'] || null,
        definizione: _info['definizione_ctrl'] || null
      },
      inoltro: _info['inoltro_ctrl'] || null,
      promemoriaAvviso: {
        tipo: _info['tipoTemplateAP_ctrl'] || null,
        oggetto: _info['oggetto_ctrl'] || null,
        messaggio: _info['messaggio_ctrl'] || null,
        allegaPdf: (_info['allegaPdf_ctrl'] !== undefined)?_info['allegaPdf_ctrl']:null
      },
      promemoriaRicevuta: {
        tipo: _info['tipoTemplateAR_ctrl'] || null,
        oggetto: _info['oggettoRicevuta_ctrl'] || null,
        messaggio: _info['messaggioRicevuta_ctrl'] || null,
        allegaPdf: (_info['allegaPdfRicevuta_ctrl'] !== undefined)?_info['allegaPdfRicevuta_ctrl']:null
      }
    };

    if(!_json.valori.promemoriaAvviso.oggetto) {
      _json.valori.promemoriaAvviso.messaggio = null;
    }
    if(!_json.valori.promemoriaRicevuta.oggetto) {
      _json.valori.promemoriaRicevuta.messaggio = null;
    }

    return _json;
  }

}
