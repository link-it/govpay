import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';

@Component({
  selector: 'link-stazione-view',
  templateUrl: './stazione-view.component.html',
  styleUrls: ['./stazione-view.component.scss']
})
export class StazioneViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  constructor() {}

  ngOnInit() {
    this.fGroup.addControl('idStazione_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('password_ctrl', new FormControl('', Validators.required));
    this.fGroup.addControl('abilita_ctrl', new FormControl(false));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['idStazione_ctrl'].disable();
        const unds = this.json.idStazione.indexOf('_');
        let idStazione;
        if(unds !== -1) {
          idStazione = this.json.idStazione.substring(unds + 1);
        }
        this.fGroup.controls['idStazione_ctrl'].setValue((idStazione)?idStazione:'');
        this.fGroup.controls['password_ctrl'].setValue((this.json.password)?this.json.password:'');
        this.fGroup.controls['abilita_ctrl'].setValue((this.json.abilitato)?this.json.abilitato:false);
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
    if(!this.fGroup.controls['idStazione_ctrl'].disabled){
      _json.idStazione = this.parent.json.idIntermediario + '_' + _info['idStazione_ctrl'];
    } else {
     _json.idStazione = this.json.idStazione;
    }
    _json.abilitato = _info['abilita_ctrl'];
    _json.password = (_info['password_ctrl'])?_info['password_ctrl']:null;

    return _json;
  }

}
