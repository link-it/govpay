import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';
import { GovpayService } from '../../../../../../services/govpay.service';

declare let Converter: any;
declare let Papa: any;

@Component({
  selector: 'link-tracciato-view',
  templateUrl: './tracciato-view.component.html',
  styleUrls: ['./tracciato-view.component.scss']
})
export class TracciatoViewComponent implements OnInit, IFormComponent {
  @ViewChild('iBrowse') iBrowse: ElementRef;

  @Input() json: any;
  @Input() fGroup: FormGroup;

  protected _voce = Voce;
  protected _domini = [];
  protected _tipiPendenzaDominio = [];

  protected doc: any = { auto: false, file: null, filename: '', name: 'Standard JSON', method: null, json: '', mimeType: 'application/json' };

  //External Conversion Configuration
  protected _externalConverters: any[] = [];
  protected _methodSelected: any;

  constructor(public gps: GovpayService, private us: UtilService) {
  }

  ngOnInit() {
    this.loadDomini();

    this._externalConverters.push({ id: 1, auto: false, file: null, filename: '', name: 'Standard JSON', method: null, mimeType: 'application/json' });
    this._externalConverters.push({ id: 2, auto: false, file: null, filename: '', name: 'Standard CSV', method: null, mimeType: 'text/csv' });

    this.fGroup.addControl('tracciato_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('conversione_ctrl', new FormControl(''));
    this.fGroup.addControl('domini_ctrl', new FormControl(''));
    this.fGroup.addControl('tipiPendenzaDominio_ctrl', new FormControl(''));
    this._checkForExternalScript();
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
    if(this.iBrowse && !this.doc.file) {
      this.iBrowse.nativeElement.value = '';
      this.iBrowse.nativeElement.onchange = this._handleFileSelect.bind(this);
      this.iBrowse.nativeElement.click();
    }
  }

  protected _handleFileSelect(event) {
    if(event.currentTarget.files.length != 0) {
      let _file = event.currentTarget.files[0];
      this.doc.file = _file;
      this.doc.filename = _file.name;
      this.doc.json = '';
      this.fGroup.controls.tracciato_ctrl.setValue(_file.name);

      if(this._methodSelected && this._methodSelected.method) {
        this._doParse();
      }
    }
  }

  protected _resetSelection(event?: any) {
    if(this.iBrowse) {
      this.doc.file = null;
      this.doc.filename = null;
      this.iBrowse.nativeElement.value = '';
      this.fGroup.controls.tracciato_ctrl.setValue('');
    }
    if(event) {
      event.stopImmediatePropagation();
    }
  }

  protected _checkForExternalScript() {
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
          };
          externalScript.onerror = (error) => {
            this._cleanExternalScript();
          };
          document.head.appendChild(externalScript);
        } catch (e) {
          console.warn('External Script Config', e);
        }
      } else {
        this._initConverter();
      }
    }
  }

  protected _initConverter() {
    if(window['Converter'] && Converter.methods && (typeof Converter.methods) === 'function') {
      this._externalConverters = this._externalConverters.concat(Converter.methods() || []);
      this._externalConverters.forEach((ec, idx) => {
        ec['id'] = idx + 1;
        if(ec.auto) {
          this._methodSelected = ec;
          this.fGroup.controls['conversione_ctrl'].setValue(this._methodSelected);
        }
      }, this);
    }
  }

  protected _cleanExternalScript() {
    this._methodSelected = undefined;
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
    this.doc = event.value;
    if(this.doc && this.doc.file && this._methodSelected && this._methodSelected.method) {
      this._doParse();
    } else {
      if(this.doc.mimeType === 'text/csv') {
        this.fGroup.controls['domini_ctrl'].setValidators([Validators.required]);
      }
    }
    this.fGroup.updateValueAndValidity();
  }

  protected _doParse() {
    if(this._methodSelected.method && (typeof this._methodSelected.method === 'function')) {
      Converter.filename = this.doc.filename.indexOf('.json')!=-1?this.doc.filename:this.doc.filename + '.json';
      let _config = Converter.config;
      delete _config.error;
      _config.complete = this._papaParseComplete.bind(this);
      try {
        Papa.parse(this.doc.file, _config);
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
      this.doc.json = this._methodSelected.method(result); //Converter.js
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

    _json.file = this.doc.file;
    _json.nome = this.doc.filename;
    if (this.doc.json) {
      _json.json = this.doc.json;
    }
    _json.mimeType = this.doc.mimeType;
    _json.idDominio = this.fGroup.controls['domini_ctrl'].value?this.fGroup.controls['domini_ctrl'].value.idDominio:null;
    _json.idTipoPendenza = this.fGroup.controls['tipiPendenzaDominio_ctrl'].value?this.fGroup.controls['tipiPendenzaDominio_ctrl'].value.idTipoPendenza:null;


    return _json;
  }

}
