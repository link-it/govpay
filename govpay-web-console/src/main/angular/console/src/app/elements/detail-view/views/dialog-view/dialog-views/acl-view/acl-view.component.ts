
import { Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { FormInput } from '../../../../../../classes/view/form-input';

@Component({
  selector: 'link-acl-view',
  templateUrl: './acl-view.component.html',
  styleUrls: ['./acl-view.component.scss']
})
export class AclViewComponent implements IFormComponent,  OnInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected autorizzazioni: any[] = [];
  protected servizi: any[] = [];

  constructor(public us:UtilService) {
    this.autorizzazioni = this.us.dirittiUtente().map((el) => {
      el.disabled = false;
      return el;
    });
    this.servizi = this.us.elencoServizi();
  }

  ngOnInit() {
    if(this.json instanceof FormInput) {
    } else {
      this.fGroup.addControl('id_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('ruolo_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('principal_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('servizio_ctrl', new FormControl('', Validators.required));
      this.fGroup.addControl('autorizzazioni_ctrl', new FormControl('', Validators.required));
    }
  }

  protected _inputChange(control: string, crossControl: string) {
    let cc = this.fGroup.controls[crossControl];
    let ctrl = this.fGroup.controls[control];
    if(cc && ctrl) {
      if(ctrl.value == '' && cc.value == '') {
        ctrl.enable();
        cc.enable();
      }
      if(ctrl.value != '' && cc.value == '') {
        cc.disable();
      }
    }
  }

  protected _onChangeServizio(target: any, control: string) {
    let ctrl = this.fGroup.controls[control];
    if(ctrl) {
      this.autorizzazioni.forEach((el) => {
        el.disabled = (target.value != UtilService.SERVIZI.CONFIGURAZIONE_E_MANUTENZIONE && el.label == UtilService.DIRITTI.ESECUZIONE);
      });
      let i = ctrl.value.indexOf(UtilService.DIRITTI.ESECUZIONE);
      if(i != -1 && target.value != UtilService.SERVIZI.CONFIGURAZIONE_E_MANUTENZIONE) {
        ctrl.value.splice(i, 1);
        ctrl.setValue(ctrl.value);
      }
    }
  }

  protected _onChangeDiritti(target: any, control: string) {
    let ctrl = this.fGroup.controls[control];
    if(ctrl) {
      let i = ctrl.value.indexOf(UtilService.DIRITTI.ESECUZIONE);
      if(i != -1 && target.value && target.value != UtilService.SERVIZI.CONFIGURAZIONE_E_MANUTENZIONE) {
        ctrl.value.splice(i, 1);
        ctrl.setValue(ctrl.value);
      }
    }
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.id = _info[ 'id_ctrl' ];
    _info[ 'ruolo_ctrl' ]?_json.ruolo = _info[ 'ruolo_ctrl' ]:null;
    _info[ 'principal_ctrl' ]?_json.principal = _info[ 'principal_ctrl' ]:null;
    _json.servizio = _info[ 'servizio_ctrl' ];
    _json.autorizzazioni = _info[ 'autorizzazioni_ctrl' ];

    return _json;
  }

}
