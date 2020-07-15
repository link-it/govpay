import { Component, ElementRef, Input, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';
import { GovpayService } from '../../../../../../services/govpay.service';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';

declare let Converter: any;
declare let Papa: any;

@Component({
  selector: 'link-tracciato-view',
  templateUrl: './tracciato-view.component.html',
  styleUrls: ['./tracciato-view.component.scss']
})
export class TracciatoViewComponent implements OnInit, OnDestroy, IFormComponent {
  @ViewChild('iBrowse') iBrowse: ElementRef;

  @Input() json: any;
  @Input() fGroup: FormGroup;
  @Input() notifier: BehaviorSubject<boolean>;

  _subscription: Subscription;
  _advancedSettings: boolean = false;
  _autoIndex: number = 1;

  protected _voce = Voce;
  protected _domini = [];
  protected _tipiPendenzaDominio = [];

  //External Conversion Configuration
  protected _externalConverters: any[] = [];
  protected _methodSelected: any;

  constructor(public gps: GovpayService, private us: UtilService) {
  }

  ngOnInit() {
    if (this.notifier) {
      this._subscription = this.notifier.subscribe((value: any) => {
        if (value !== null) {
          this._advancedSettings = !this._advancedSettings;
        }
      });
    }
    this.loadDomini();

    this._externalConverters.push({ id: 1, auto: false, file: null, filename: '', name: 'Standard JSON', method: null, json: '', mimeType: 'application/json' });
    this._externalConverters.push({ id: 2, auto: true, file: null, filename: '', name: 'Standard CSV', method: null, json: '', mimeType: 'text/csv' });

    this.fGroup.addControl('tracciato_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('conversione_ctrl', new FormControl(''));
    this.fGroup.addControl('domini_ctrl', new FormControl(''));
    this.fGroup.addControl('tipiPendenzaDominio_ctrl', new FormControl(''));
    this._checkForExternalScript();
  }

  ngOnDestroy() {
    if (this._subscription) {
      this._subscription.unsubscribe();
    }
  }

  protected loadDomini() {
    const _url = UtilService.URL_DOMINI;
    this.gps.getDataService(_url, UtilService.QUERY_ASSOCIATI).subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        this._domini = (response && response.body)?response.body['risultati']:[];
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected _select() {
    if(this.iBrowse && !this._methodSelected.file) {
      this.iBrowse.nativeElement.value = '';
      this.iBrowse.nativeElement.onchange = this._handleFileSelect.bind(this);
      this.iBrowse.nativeElement.click();
    }
  }

  protected _handleFileSelect(event) {
    if(event.currentTarget.files.length != 0) {
      let _file = event.currentTarget.files[0];
      this._methodSelected.file = _file;
      this._methodSelected.filename = _file.name;
      this._methodSelected.json = '';
      this.fGroup.controls['tracciato_ctrl'].setValue(_file.name);

      if(this._methodSelected && this._methodSelected.method) {
        this._doParse();
      }
    }
  }

  protected _resetSelection(event?: any) {
    if(this.iBrowse) {
      this._methodSelected.file = null;
      this._methodSelected.filename = null;
      this.iBrowse.nativeElement.value = '';
      this.fGroup.controls['tracciato_ctrl'].setValue('');
    }
    if(event) {
      event.stopImmediatePropagation();
    }
  }

  protected _checkForExternalScript() {
    this._resetMethod();
    const jsURL = UtilService.JS_URL;
    if (jsURL) {
      if(window['Converter'] === undefined) {
        try {
          this._cleanExternalScript();
          let externalScript = document.createElement('script');
          externalScript.id = 'externalConverter';
          externalScript.type = 'text/javascript';
          externalScript.src = jsURL;
          externalScript.onload = (event) => {
            this._initConverter();
            this._preSelection();
          };
          externalScript.onerror = (error) => {
            this._cleanExternalScript();
            this._preSelection();
          };
          document.head.appendChild(externalScript);
        } catch (e) {
          console.warn('External Script Config', e);
        }
      } else {
        this._initConverter();
        this._preSelection();
      }
    } else {
      this._preSelection();
    }
  }

  protected _initConverter() {
    if(window['Converter'] && Converter.methods && (typeof Converter.methods) === 'function') {
      this._externalConverters = this._externalConverters.concat(Converter.methods() || []);
      this._externalConverters.forEach((ec, idx) => {
        ec['id'] = idx + 1;
        if(ec.auto) {
          this._autoIndex = idx;
        }
      }, this);
    }
  }

  protected _preSelection() {
    this._advancedSettings = false;
    const _selected = this._externalConverters[this._autoIndex];
    if (_selected) {
      this.fGroup.controls['conversione_ctrl'].setValue(_selected);
      this._onChangeConversion({ value: _selected });
    } else {
      this._advancedSettings = true;
    }
  }

  protected _resetMethod() {
    this._methodSelected = {
      id: -1,
      auto: false,
      file: null,
      filename: '',
      name: '',
      method: null,
      json: '',
      mimeType: ''
    }
  }

  protected _cleanExternalScript() {
    this._resetMethod();
    const _ec = document.head.querySelector('#externalConverter');
    if(_ec) {
      document.head.removeChild(_ec);
    }
  }

  protected _comparingFct(option: any, selection: any): boolean {
    return (selection && option.id == selection.id);
  }

  protected _onChangeConversion(event: any) {
    this._resetSelection();
    this.fGroup.controls['domini_ctrl'].clearValidators();
    this.fGroup.controls['tipiPendenzaDominio_ctrl'].clearValidators();
    this.fGroup.controls['domini_ctrl'].reset();
    this.fGroup.controls['tipiPendenzaDominio_ctrl'].reset();
    this._methodSelected = event.value;
    if(event && event.value && event.value['file'] && event.value['method']) {
      this._doParse();
    } else {
      if(event.value['mimeType'] === 'text/csv') {
        this.fGroup.controls['domini_ctrl'].setValidators([Validators.required]);
      }
    }
    this.fGroup.updateValueAndValidity();
  }

  protected _doParse() {
    if(this._methodSelected.method && (typeof this._methodSelected.method === 'function')) {
      Converter.filename = this._methodSelected.filename.indexOf('.json')!=-1?this._methodSelected.filename:this._methodSelected.filename + '.json';
      let _config = Converter.config;
      delete _config.error;
      _config.complete = this._papaParseComplete.bind(this);
      try {
        Papa.parse(this._methodSelected.file, _config);
      } catch (e) {
        this.us.alert('Impossibile leggere il file caricato.');
        if(Converter.verbose) {
          console.log(e);
        }
      }
    }
  }

  protected _papaParseComplete(result, _file) {
    if(this._methodSelected.method && (typeof this._methodSelected.method === 'function')) {
      this._methodSelected.json = this._methodSelected.method(result); //Converter.js
      if (result.errors && result.errors.length !== 0) {
        this.us.alert('Il file caricato potrebbe non essere compatibile con la tipologia di convertitore selezionato. Verificarne la disponibilità nelle impostazioni avanzate.');
      }
    }
  }

  protected _dominiChangeSelection(event: any) {
    this._tipiPendenzaDominio = [];
    this.fGroup.controls['tipiPendenzaDominio_ctrl'].setValue('');
    this.fGroup.controls['tipiPendenzaDominio_ctrl'].disable();
    const _url: string = event.value.tipiPendenza ;
    const _query: string = UtilService.QUERY_ASSOCIATI + '&' + UtilService.QUERY_ABILITATO + '&' + UtilService.QUERY_TIPO_DOVUTO + '&' + UtilService.QUERY_TRASFORMAZIONE_ENABLED;
    this._loadTipiPendenzaDominio(_url, _query);
  }

  protected _loadTipiPendenzaDominio(_dominioRef: string, _query:string) {
    this.gps.getDataService(_dominioRef, _query).subscribe(
      (response) => {
        this.gps.updateSpinner(false);
        this.fGroup.controls['tipiPendenzaDominio_ctrl'].enable();
        this._tipiPendenzaDominio = (response && response.body)?response.body['risultati']:[];
        if(this._tipiPendenzaDominio.length == 1) {
          const _ftpdom = this._tipiPendenzaDominio[0];
          this.fGroup.controls['tipiPendenzaDominio_ctrl'].setValue(_ftpdom);
        }
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected dominioCmpFn(d1: any, d2: any): boolean {
    return (d1 && d2)?(d1.idDominio === d2.idDominio):(d1 === d2);
  }

  protected tipoPendenzaDominioCmpFn(p1: any, p2: any): boolean {
    return (p1 && p2)?(p1.idTipoPendenza === p2.idTipoPendenza):(p1 === p2);
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _json:any = {};

    if (this._methodSelected) {
      _json.file = this._methodSelected.file;
      _json.nome = this._methodSelected.filename;
      if (this._methodSelected.json) {
        _json.json = this._methodSelected.json;
      }
      _json.mimeType = this._methodSelected.mimeType;
      _json.idDominio = this.fGroup.controls['domini_ctrl'].value?this.fGroup.controls['domini_ctrl'].value.idDominio:null;
      _json.idTipoPendenza = this.fGroup.controls['tipiPendenzaDominio_ctrl'].value?this.fGroup.controls['tipiPendenzaDominio_ctrl'].value.idTipoPendenza:null;
    }


    return _json;
  }

}
