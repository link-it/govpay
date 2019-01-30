
import { Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { Voce } from '../../../../../../services/voce.service';

@Component({
  selector: 'link-acl-view',
  templateUrl: './acl-view.component.html',
  styleUrls: ['./acl-view.component.scss']
})
export class AclViewComponent implements IFormComponent, OnInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected autorizzazioni: any[] = [];
  protected servizi: any[] = [];

  protected _RISORSA = Voce.RISORSA;
  protected _OPERAZIONI = Voce.OPERAZIONI;

  constructor(public us:UtilService) {
    this.autorizzazioni = this.us.dirittiCodeUtente().map((el) => {
      el.disabled = false;
      return el;
    });
    this.servizi = UtilService.SERVIZI;
  }

  ngOnInit() {
    this.fGroup.addControl('servizio_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('autorizzazioni_ctrl', new FormControl('', Validators.required));
  }

  protected _onChangeServizio(target: any, control: string) {
    let ctrl = this.fGroup.controls[control];
    if(ctrl) {
      this.autorizzazioni.forEach((el) => {
        el.disabled = false;
        if (target.value.toLowerCase() != UtilService.CONFIGURAZIONE_E_MANUTENZIONE.toLowerCase() &&
            target.value.toLowerCase() != UtilService.PAGAMENTI_E_PENDENZE.toLowerCase() &&
            el.label == UtilService._LABEL.ESECUZIONE) {
          el.disabled = true;
        }
      });
      let i = ctrl.value.indexOf(UtilService._CODE.ESECUZIONE);
      if(i != -1 && target.value != UtilService.CONFIGURAZIONE_E_MANUTENZIONE) {
        ctrl.value.splice(i, 1);
        ctrl.setValue(ctrl.value);
      }
    }
  }

  protected _onChangeDiritti(target: any, control: string) {
    let ctrl = this.fGroup.controls[control];
    if(ctrl) {
      let i = ctrl.value.indexOf(UtilService._CODE.ESECUZIONE);
      if(i != -1 && target.value &&
        target.value.toLowerCase() != UtilService.CONFIGURAZIONE_E_MANUTENZIONE.toLowerCase() &&
        target.value.toLowerCase() != UtilService.PAGAMENTI_E_PENDENZE.toLowerCase()) {
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

    _json.servizio = (_info['servizio_ctrl'])?_info['servizio_ctrl']:null;
    _json.autorizzazioni = (_info['autorizzazioni_ctrl'])?_info['autorizzazioni_ctrl']:null;

    return _json;
  }

}
