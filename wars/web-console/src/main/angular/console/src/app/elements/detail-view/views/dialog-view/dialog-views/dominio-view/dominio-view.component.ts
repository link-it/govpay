import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormInput } from '../../../../../../classes/view/form-input';
import { DomSanitizer } from '@angular/platform-browser';
import { UtilService } from '../../../../../../services/util.service';
import { MatDialog } from '@angular/material';
import { GovpayService } from '../../../../../../services/govpay.service';

declare global {
  interface String {
    multiReplace(find: string[], replace: string[]): string;
  }
}

@Component({
  selector: 'link-dominio-view',
  templateUrl: './dominio-view.component.html',
  styleUrls: ['./dominio-view.component.scss']
})
export class DominioViewComponent implements IFormComponent, OnInit, AfterViewInit {
  @ViewChild('iBrowse') iBrowse: ElementRef;

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected SVG: string = 'data:image/svg+xml;base64,';
  protected _jsonFormInput: boolean = false;
  protected _name: string = '';
  protected _base64File: any = null;
  protected _intermediariStazione: any[] = [];
  protected _stazioniDominio: string[] = [];

  constructor(private _sanitizer: DomSanitizer, public us: UtilService, public gps: GovpayService, protected askDialog: MatDialog) {
    String.prototype.multiReplace = function(find: string[], replace: string[]): string {
      let replaceString = this;
      for (let i = 0; i < find.length; i++) {
        replaceString = replaceString.replace(find[i], replace[i]);
      }
      return replaceString;
    };
  }

  ngOnInit() {
    if(this.json instanceof FormInput) {
      this._jsonFormInput = true;
      this.fGroup.addControl('idDominio_ctrl', new FormControl('', Validators.required));
    } else {
      this._loadIntermediariStazione();
      this.fGroup.addControl('ragioneSociale_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('idDominio_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('area_ctrl', new FormControl(''));
      this.fGroup.addControl('gln_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('intermediarioStazione_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('stazione_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('abilita_ctrl', new FormControl(false));
      this.fGroup.addControl('indirizzo_ctrl', new FormControl(''));
      this.fGroup.addControl('civico_ctrl', new FormControl(''));
      this.fGroup.addControl('cap_ctrl', new FormControl(''));
      this.fGroup.addControl('localita_ctrl', new FormControl(''));
      this.fGroup.addControl('provincia_ctrl', new FormControl(''));
      this.fGroup.addControl('nazione_ctrl', new FormControl(''));
      this.fGroup.addControl('email_ctrl', new FormControl(''));
      this.fGroup.addControl('pec_ctrl', new FormControl(''));
      this.fGroup.addControl('tel_ctrl', new FormControl(''));
      this.fGroup.addControl('fax_ctrl', new FormControl(''));
      this.fGroup.addControl('web_ctrl', new FormControl(''));
      this.fGroup.addControl('cbill_ctrl', new FormControl(''));
      this.fGroup.addControl('iuvPrefix_ctrl', new FormControl(''));
      this.fGroup.addControl('auxDigit_ctrl', new FormControl(''));
      this.fGroup.addControl('segregationCode_ctrl', new FormControl(''));
      this.fGroup.addControl('autStampaPosteItaliane_ctrl', new FormControl(''));
      this.fGroup.addControl('logo_ctrl', new FormControl(''));
    }
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        if(this.json instanceof FormInput && this.json.values.length == 0) {
          this.fGroup.controls[ 'idDominio_ctrl' ].disable();
        }
        if(!(this.json instanceof FormInput)) {
          this.fGroup.controls['idDominio_ctrl'].disable();
          this.fGroup.controls['idDominio_ctrl'].setValue((this.json.idDominio)?this.json.idDominio:'');
          this.fGroup.controls['ragioneSociale_ctrl'].setValue((this.json.ragioneSociale)?this.json.ragioneSociale:'');
          this.fGroup.controls['area_ctrl'].setValue((this.json.area)?this.json.area:'');
          this.fGroup.controls['gln_ctrl'].setValue((this.json.gln)?this.json.gln:'');
          this.fGroup.controls['intermediarioStazione_ctrl'].setValue((this.json.stazione)?this.json.stazione.split('_')[0]:'');
          this.fGroup.controls['stazione_ctrl'].setValue((this.json.stazione)?this.json.stazione:'');
          this.fGroup.controls['abilita_ctrl'].setValue((this.json.abilitato)?this.json.abilitato:false);
          this.fGroup.controls['indirizzo_ctrl'].setValue((this.json.indirizzo)?this.json.indirizzo:'');
          this.fGroup.controls['civico_ctrl'].setValue((this.json.civico)?this.json.civico:'');
          this.fGroup.controls['cap_ctrl'].setValue((this.json.cap)?this.json.cap:'');
          this.fGroup.controls['localita_ctrl'].setValue((this.json.localita)?this.json.localita:'');
          this.fGroup.controls['provincia_ctrl'].setValue((this.json.provincia)?this.json.provincia:'');
          this.fGroup.controls['nazione_ctrl'].setValue((this.json.nazione)?this.json.nazione:'');
          this.fGroup.controls['email_ctrl'].setValue((this.json.email)?this.json.email:'');
          this.fGroup.controls['pec_ctrl'].setValue((this.json.pec)?this.json.pec:'');
          this.fGroup.controls['tel_ctrl'].setValue((this.json.tel)?this.json.tel:'');
          this.fGroup.controls['fax_ctrl'].setValue((this.json.fax)?this.json.fax:'');
          this.fGroup.controls['web_ctrl'].setValue((this.json.web)?this.json.web:'');
          this.fGroup.controls['cbill_ctrl'].setValue((this.json.cbill)?this.json.cbill:'');
          this.fGroup.controls['iuvPrefix_ctrl'].setValue((this.json.iuvPrefix)?this.json.iuvPrefix:'');
          this.fGroup.controls['auxDigit_ctrl'].setValue((this.json.auxDigit)?this.json.auxDigit:'');
          this.fGroup.controls['segregationCode_ctrl'].setValue((this.json.segregationCode)?this.json.segregationCode:'');
          this.fGroup.controls['autStampaPosteItaliane_ctrl'].setValue((this.json.autStampaPosteItaliane)?this.json.autStampaPosteItaliane:'');
          // this.fGroup.controls['logo_ctrl'].setValue((this.json.logo)?this.json.logo:'');
          if(this.json.logo){
            this._reloadFile(this.json.logo, true);
          }
        }
      }
    });
  }

  protected _select() {
    if(this.iBrowse) {
      this.iBrowse.nativeElement.onchange = this._handleFileSelect.bind(this);
      this.iBrowse.nativeElement.click();
    }
  }

  protected _resetSelection() {
    if(this.iBrowse) {
      this.iBrowse.nativeElement.value = '';
      this.fGroup.controls.logo_ctrl.setValue('');
    }
    this._base64File = null;
    this._name = '';
  }

  protected _handleFileSelect(event) {
    if(event.currentTarget.files.length != 0) {
      let _file = event.currentTarget.files[0];
      this._readFile(_file);
    }
  }

  protected _readFile(_file: any) {
    let fr = new FileReader();

    fr.onload = function() {
      this._reloadFile(fr.result);
    }.bind(this);

    fr.readAsDataURL(_file);
  }

  protected _reloadFile(_url: string, bypass: boolean = false) {
    let _result = _url;

    if(!bypass && _url.indexOf('svg') != -1 && _url.indexOf('base64,') != -1) {
      let _hasColor: boolean = false;
      let _bsvg = _url.split('base64,')[1];
      let _xsvg = atob(_bsvg);
      let re = RegExp('(#[a-z0-9]{6})','gmi');
      let _tmp;
      let _results = [];
      while ((_tmp = re.exec(_xsvg)) !== null) {
        const _atmp = this._getRGB(_tmp[0]);
        if (_atmp[0] !== _atmp[1] && _atmp[0] !== _atmp[2] && _atmp[1] !== _atmp[2]) {
          _hasColor = true;
        }
        _results.push(_tmp[0]);
      }
      let _map_results = _results.map((_color) => {
        return this.us.desaturateColor(_color);
      }, this);
      if(_hasColor && _results && _results.length != 0 && _map_results && _map_results.length != 0) {
        this._askForConversion({ original: _result, xsvg: _xsvg, results: _results, map: _map_results });
      } else {
        this._base64File = this._sanitizer.bypassSecurityTrustUrl(_result);
      }
    } else {
      this._base64File = this._sanitizer.bypassSecurityTrustUrl(_result);
    }
  }

  protected _getRGB(hexVal) {
    let commaSeperated = '';
    hexVal = hexVal.substring(1, hexVal.length);
    for (let i = 0; i < hexVal.length; i++) {
      commaSeperated += hexVal.charAt(i);
      commaSeperated += (i % 2 == 1 && i != (hexVal.length - 1)) ? ',' : '';
    }
    return commaSeperated.split(',');
  }


  protected _askForConversion(data: any) {
      let askDialogRef = this.askDialog.open(AlertDialog, {
        width: '50%',
        data: data
      });

    askDialogRef.afterClosed().subscribe(response => {
      let _result = response.data.original;
      if(response.converti) {
        _result = response.data.xsvg.multiReplace(response.data.results, response.data.map);
        _result = this.SVG+btoa(_result);
      }
      this._base64File = this._sanitizer.bypassSecurityTrustUrl(_result);
    });
  }

  protected _loadIntermediariStazione(): any {
    let _service = UtilService.URL_REGISTRO_INTERMEDIARI;
    this.gps.getDataService(_service).subscribe(
      (_response) => {
        this.gps.updateSpinner(false);
        this._intermediariStazione = _response.body['risultati'].map(function(item) {
          return new Dato({ label: item.denominazione, value: item.idIntermediario });
        });
        if(this.json && this.json.stazione) {
          this._loadStazioniDominio(this.json.stazione.split('_')[0]);
        }
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected _intermediarioChange(event) {
    this.fGroup.controls['stazione_ctrl'].setValue('');
    this._stazioniDominio = [];
    this._loadStazioniDominio(event.value);
  }

  protected _loadStazioniDominio(intermediario: string): any {
    let _service = UtilService.URL_REGISTRO_INTERMEDIARI + '/' + intermediario + UtilService.URL_STAZIONI;
    this.gps.getDataService(_service).subscribe(
      (_response) => {
        this.gps.updateSpinner(false);
        this._stazioniDominio = _response.body['risultati'].map(function(item) {
          return item.idStazione;
        });
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};
    if(this.json instanceof FormInput) {
      _json.values = [];
      _info['idDominio_ctrl'].forEach((el) => {
        _json.values.push({
          idDominio: el.value,
          ragioneSociale: el.label
        });
      });
    } else {
      _json.idDominio = (!this.fGroup.controls['idDominio_ctrl'].disabled)?_info['idDominio_ctrl']:this.json.idDominio;
      _json.ragioneSociale = (_info['ragioneSociale_ctrl'])?_info['ragioneSociale_ctrl']:null;
      _json.area = (_info['area_ctrl'])?_info['area_ctrl']:null;
      _json.gln = (_info['gln_ctrl'])?_info['gln_ctrl']:null;
      _json.stazione = (_info['stazione_ctrl'])?_info['stazione_ctrl']:null;
      _json.abilitato = _info['abilita_ctrl'];
      _json.indirizzo = (_info['indirizzo_ctrl'])?_info['indirizzo_ctrl']:null;
      _json.civico = (_info['civico_ctrl'])?_info['civico_ctrl']:null;
      _json.cap = (_info['cap_ctrl'])?_info['cap_ctrl']:null;
      _json.localita = (_info['localita_ctrl'])?_info['localita_ctrl']:null;
      _json.provincia = (_info['provincia_ctrl'])?_info['provincia_ctrl']:null;
      _json.nazione = (_info['nazione_ctrl'])?_info['nazione_ctrl']:null;
      _json.email = (_info['email_ctrl'])?_info['email_ctrl']:null;
      _json.pec = (_info['pec_ctrl'])?_info['pec_ctrl']:null;
      _json.tel = (_info['tel_ctrl'])?_info['tel_ctrl']:null;
      _json.fax = (_info['fax_ctrl'])?_info['fax_ctrl']:null;
      _json.web = (_info['web_ctrl'])?_info['web_ctrl']:null;
      _json.cbill = (_info['cbill_ctrl'])?_info['cbill_ctrl']:null;
      _json.iuvPrefix = (_info['iuvPrefix_ctrl'])?_info['iuvPrefix_ctrl']:null;
      _json.auxDigit = (_info['auxDigit_ctrl'])?_info['auxDigit_ctrl']:null;
      _json.segregationCode = (_info['segregationCode_ctrl'])?_info['segregationCode_ctrl']:null;
      _json.autStampaPosteItaliane = (_info['autStampaPosteItaliane_ctrl'])?_info['autStampaPosteItaliane_ctrl']:null;
      //Sanitizer
      if(this._base64File && this._base64File.hasOwnProperty('changingThisBreaksApplicationSecurity')) {
        _json.logo = this._base64File['changingThisBreaksApplicationSecurity'] || null;//_info['logo_ctrl'];
      } else {
        _json.logo = this._base64File || null;//_info['logo_ctrl'];
      }

      // PUT: Non serve passare le liste contiAccredito, entrate, unitaOperative
      // _json.contiAccredito = (!this.fGroup.controls['idDominio_ctrl'].disabled)?(this.json?this.json.contiAccredito:[]):[];
      // _json.entrate = (!this.fGroup.controls['idDominio_ctrl'].disabled)?(this.json?this.json.entrate:[]):[];
      // _json.unitaOperative = (!this.fGroup.controls['idDominio_ctrl'].disabled)?(this.json?this.json.unitaOperative:[]):[];
    }

    return _json;
  }

}

import { Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { Dato } from '../../../../../../classes/view/dato';

@Component({
  selector: 'link-alert-dialog',
  template: `<div class="d-block">
    <p class="w-100 regular-24 color-gray">Conversione SVG</p>
      <div class="w-100">
        <p>E' stato rilevato un documento svg a colori. Si desidera eseguire la conversione in scala di grigi dell'immagine selezionata?</p>
      </div>
      <div class="d-flex justify-content-end mt-4">
        <button mat-button color="accent" type="button" (click)="_close()">Non convertire</button>
        <button mat-button color="accent" type="button" (click)="_close(true)">Converti</button>
      </div>
  </div>`,
})
export class AlertDialog {

  protected _data: any;

  constructor(public askDialog: MatDialogRef<AlertDialog>, @Inject(MAT_DIALOG_DATA) public data: any) {
    this._data = data;
  }

  _close(converti: boolean = false): void {
    this.askDialog.close({ converti: converti, data: this._data });
  }

}
