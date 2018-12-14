import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';

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

  protected doc: any = { file: null, filename: '', json: '' };
  protected conversioneCtrl: FormControl = new FormControl();

  //External Conversion Configuration
  protected _externalConverters: any[] = [];
  protected _methodSelected: any;

  constructor(private us: UtilService) {
  }

  ngOnInit() {
    this.fGroup.addControl('tracciato_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('conversione_ctrl', this.conversioneCtrl);
    this._checkForExternalScript();
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
      this.doc = { file: _file, filename: _file.name, json: '' };
      this.fGroup.controls.tracciato_ctrl.setValue(_file.name);

      if(this._methodSelected && this._externalConverters && this._externalConverters.length != 0) {
        this._doParse();
      }

      if(_file.type != 'application/json' && (!this._externalConverters || this._externalConverters.length == 0)) {
        this._notAllowed();
        this._resetSelection();
      }
    }
  }

  protected _notAllowed() {
    this.us.alert('Documento non corretto, selezionare un formato Json.');
  }

  protected _resetSelection(event?: any) {
    if(this.iBrowse) {
      this.doc = { file: null, filename: '', json: '' };
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
      this._externalConverters = Converter.methods();
      this._externalConverters.forEach((ec) => {
        if(ec.auto) {
          this._methodSelected = ec;
          this.fGroup.controls['conversione_ctrl'].setValue(this._methodSelected);
        }
      }, this);
      this.conversioneCtrl.setValidators(null);
      if(this._externalConverters && this._externalConverters.length != 0) {
        this.conversioneCtrl.setValidators(Validators.required);
      }
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

  protected _onChangeConversion() {
    if(this.doc && this.doc.file && this._methodSelected && this._methodSelected.method) {
      this._doParse();
    }
  }

  protected _doParse() {
    if(this._methodSelected.method && (typeof this._methodSelected.method === 'function')) {
      Converter.filename = this.doc.filename + '.json';
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

    return _json;
  }

}
