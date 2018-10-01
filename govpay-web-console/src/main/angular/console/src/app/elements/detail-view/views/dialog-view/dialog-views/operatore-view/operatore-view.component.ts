import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';

@Component({
  selector: 'link-operatore-view',
  templateUrl: './operatore-view.component.html',
  styleUrls: ['./operatore-view.component.scss']
})
export class OperatoreViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('principal_ctrl', new FormControl(''));
    this.fGroup.addControl('abilita_ctrl', new FormControl(false));
    this.fGroup.addControl('ragioneSociale_ctrl', new FormControl(''));
  }

  ngAfterViewInit() {
    setTimeout(() => {
      if(this.json) {
        this.fGroup.controls['principal_ctrl'].disable();
        this.fGroup.controls['principal_ctrl'].setValue(this.json.principal);
        this.fGroup.controls['abilita_ctrl'].setValue(this.json.abilitato);
        this.fGroup.controls['ragioneSociale_ctrl'].setValue(this.json.ragioneSociale);
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
    _json.principal = (!this.fGroup.controls['principal_ctrl'].disabled)?_info['principal_ctrl']: this.json.principal;
    _json.abilitato = _info['abilita_ctrl'];
    _json.ragioneSociale = (_info['ragioneSociale_ctrl'])?_info['ragioneSociale_ctrl']:null;

    _json.domini = (this.fGroup.controls['principal_ctrl'].disabled)?this.json.domini:[];
    _json.entrate = (this.fGroup.controls['principal_ctrl'].disabled)?this.json.entrate:[];
    _json.acl = (this.fGroup.controls['principal_ctrl'].disabled)?this.json.acl:[];

    return _json;
  }

}
