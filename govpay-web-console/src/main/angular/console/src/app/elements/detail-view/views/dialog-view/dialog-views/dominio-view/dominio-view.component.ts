import { AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormInput } from '../../../../../../classes/view/form-input';

@Component({
  selector: 'link-dominio-view',
  templateUrl: './dominio-view.component.html',
  styleUrls: ['./dominio-view.component.scss']
})
export class DominioViewComponent implements IFormComponent, OnInit, AfterViewInit {
  @ViewChild('iBrowse') iBrowse: ElementRef;

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected _jsonFormInput: boolean = false;
  protected _name: string = '';
  protected _base64File: string = null;

  constructor() { }

  ngOnInit() {
    if(this.json instanceof FormInput) {
      this._jsonFormInput = true;
      this.fGroup.addControl('idDominio_ctrl', new FormControl('', Validators.required));
    } else {
      this.fGroup.addControl('ragioneSociale_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('idDominio_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('gln_ctrl', new FormControl('', Validators.required));
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
          this.fGroup.controls['gln_ctrl'].setValue((this.json.gln)?this.json.gln:'');
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
          // this.fGroup.controls['logo_ctrl'].setValue((this.json.logo)?this.json.logo:'');
          if(this.json.logo){
            this._reloadFile(this.json.logo);
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
      this._base64File = fr.result;
    }.bind(this);

    fr.readAsDataURL(_file);
  }

  protected _reloadFile(_url: string) {
    let img = new Image();
    img.onload = function() {
      try {
        let canvas = document.createElement('canvas');
        let ctx = canvas.getContext('2d');
        canvas.width = img.width;
        canvas.height = img.height;
        ctx.drawImage(img, 0, 0);
        this._base64File = canvas.toDataURL();
      } catch (e) {
        this._base64File = null;
      }
    }.bind(this);

    img.src = window['hostname']()+window['rootService']()+_url;
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
      _json.logo = this._base64File || null;//_info['logo_ctrl'];

      // PUT: Non serve passare le liste contiAccredito, entrate, unitaOperative
      // _json.contiAccredito = (!this.fGroup.controls['idDominio_ctrl'].disabled)?(this.json?this.json.contiAccredito:[]):[];
      // _json.entrate = (!this.fGroup.controls['idDominio_ctrl'].disabled)?(this.json?this.json.entrate:[]):[];
      // _json.unitaOperative = (!this.fGroup.controls['idDominio_ctrl'].disabled)?(this.json?this.json.unitaOperative:[]):[];
    }

    return _json;
  }

}
