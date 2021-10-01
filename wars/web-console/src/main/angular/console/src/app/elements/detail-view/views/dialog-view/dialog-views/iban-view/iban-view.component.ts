import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { AbstractControl, FormControl, FormGroup } from '@angular/forms';
import { Parameters } from '../../../../../../classes/parameters';
import { Voce } from '../../../../../../services/voce.service';

@Component({
  selector: 'link-iban-view',
  templateUrl: './iban-view.component.html',
  styleUrls: ['./iban-view.component.scss']
})
export class IbanViewComponent implements IFormComponent, OnInit, AfterViewInit {

  _Voce = Voce;
  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('ibanAccredito_ctrl', new FormControl('', this.getIbanValidator(this.parent.iban_cc)));
    this.fGroup.addControl('bicAccredito_ctrl', new FormControl(''));
    this.fGroup.addControl('descrizione_ctrl', new FormControl(''));
    this.fGroup.addControl('intestatario_ctrl', new FormControl(''));
    this.fGroup.addControl('autStampaPosteItaliane_ctrl', new FormControl(''));
    this.fGroup.addControl('postale_ctrl', new FormControl(false));
    this.fGroup.addControl('abilita_ctrl', new FormControl(false));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['ibanAccredito_ctrl'].disable();
        this.fGroup.controls['ibanAccredito_ctrl'].setValue((this.json.iban)?this.json.iban:'');
        this.fGroup.controls['bicAccredito_ctrl'].setValue((this.json.bic)?this.json.bic:'');
        this.fGroup.controls['descrizione_ctrl'].setValue((this.json.descrizione)?this.json.descrizione:'');
        this.fGroup.controls['intestatario_ctrl'].setValue((this.json.intestatario)?this.json.intestatario:'');
        if (this.json.postale) {
          this.fGroup.controls['autStampaPosteItaliane_ctrl'].setValue((this.json.autStampaPosteItaliane)?this.json.autStampaPosteItaliane:'');
        }
        this.fGroup.controls['postale_ctrl'].setValue((this.json.postale)?this.json.postale:'');
        this.fGroup.controls['abilita_ctrl'].setValue((this.json.abilitato)?this.json.abilitato:false);
      }
    });
  }

  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.iban = (!this.fGroup.controls['ibanAccredito_ctrl'].disabled)?_info['ibanAccredito_ctrl']:this.json.iban;
    _json.bic = (_info['bicAccredito_ctrl'])?_info['bicAccredito_ctrl']:null;
    _json.descrizione = (_info['descrizione_ctrl'])?_info['descrizione_ctrl']:null;
    if (_info['postale_ctrl']) {
      _json.intestatario = (_info['intestatario_ctrl'])?_info['intestatario_ctrl']:null;
      _json.autStampaPosteItaliane = (_info['autStampaPosteItaliane_ctrl'])?_info['autStampaPosteItaliane_ctrl']:null;
    }
    _json.postale = _info['postale_ctrl'] || false;
    _json.abilitato = _info['abilita_ctrl'] || false;

    return _json;
  }

  getIbanValidator(iban_cc: Parameters[]) {
    return function (control: AbstractControl) {
      let valid = null;
      if(control.value == '') {
        return { valid: false, error: '' };
      }
      if(iban_cc) {
        iban_cc.forEach((item) => {
          if(control.value == item.jsonP.iban) {
            valid = { valid: false, error: 'Iban già presente.' };
          }
        }, this);
      }

      return valid;
    }
  }
}
