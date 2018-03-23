import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { AbstractControl, FormControl, FormGroup, Validators } from '@angular/forms';
import { Parameters } from '../../../../../../classes/parameters';

@Component({
  selector: 'link-iban-view',
  templateUrl: './iban-view.component.html',
  styleUrls: ['./iban-view.component.scss']
})
export class IbanViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('ibanAccredito_ctrl', new FormControl('', this.getIbanValidator(this.parent.iban_cc)));
    this.fGroup.addControl('bicAccredito_ctrl', new FormControl(''));
    this.fGroup.addControl('postale_ctrl', new FormControl(false));
    this.fGroup.addControl('mybank_ctrl', new FormControl(false));
    this.fGroup.addControl('abilita_ctrl', new FormControl(false));
  }

  ngAfterViewInit() {
  }

  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.ibanAccredito = _info['ibanAccredito_ctrl'];
    _json.bicAccredito = _info['bicAccredito_ctrl'];
    _json.postale = _info['postale_ctrl'];
    _json.mybank = _info['mybank_ctrl'];
    _json.abilitato = _info['abilita_ctrl'];

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
          if(control.value == item.jsonP.ibanAccredito) {
            valid = { valid: false, error: 'Iban già presente.' };
          }
        }, this);
      }

      return valid;
    }
  }
}
