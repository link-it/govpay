
import { Component, Input, OnInit } from '@angular/core';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Voce } from '../../../../../../services/voce.service';

@Component({
  selector: 'link-ruolo-view',
  templateUrl: './ruolo-view.component.html',
  styleUrls: ['./ruolo-view.component.scss']
})
export class RuoloViewComponent implements IFormComponent, OnInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;

  protected _IDENTIFICATIVO = Voce.IDENTIFICATIVO;

  constructor() { }

  ngOnInit() {
    this.fGroup.addControl('id_ctrl', new FormControl('', Validators.required));
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.id = (_info['id_ctrl'])?_info['id_ctrl']:null;
    _json.servizio = (_info['servizio_ctrl'])?_info['servizio_ctrl']:null;
    _json.autorizzazioni = (_info['autorizzazioni_ctrl'])?_info['autorizzazioni_ctrl']:null;

    return _json;
  }

}
