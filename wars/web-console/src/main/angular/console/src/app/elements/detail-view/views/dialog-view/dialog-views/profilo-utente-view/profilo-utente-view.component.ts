import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';
import { Voce } from '../../../../../../services/voce.service';

@Component({
  selector: 'link-profilo-utente-view',
  templateUrl: './profilo-utente-view.component.html',
  styleUrls: ['./profilo-utente-view.component.scss']
})
export class ProfiloUtenteViewComponent implements OnInit, IFormComponent {

  _voce = Voce;
  hide: boolean = true;

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  constructor(public us:UtilService) { }

  ngOnInit() {
    this.fGroup.addControl('password_ctrl', new FormControl('', [ Validators.required, Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?!.*[\s]).{8,}$/) ]));
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.password = _info['password_ctrl'];

    return _json;
  }

}
