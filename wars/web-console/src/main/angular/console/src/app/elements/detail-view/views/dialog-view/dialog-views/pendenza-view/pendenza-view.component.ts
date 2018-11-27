import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UtilService } from '../../../../../../services/util.service';
import { IFormComponent } from '../../../../../../classes/interfaces/IFormComponent';

@Component({
  selector: 'link-pendenza-view',
  templateUrl: './pendenza-view.component.html',
  styleUrls: ['./pendenza-view.component.scss']
})
export class PendenzaViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() fGroup: FormGroup;
  @Input() json: any;
  @Input() parent: any;

  protected stati: any[];

  constructor(public us:UtilService) {
  }

  ngOnInit() {
    this.stati = this.us.statiPendenza().filter((stato) => {
      return (this.parent.stato != stato.value);
    });
    let _stato = '';
    let _choice = UtilService.STATI_PENDENZE[this.parent.stato];
    switch(_choice) {
      case UtilService.STATI_PENDENZE.NON_ESEGUITA:
        _stato = this.us.getKeyByValue(UtilService.STATI_PENDENZE, UtilService.STATI_PENDENZE.ANNULLATA);
      break;
      case UtilService.STATI_PENDENZE.ANNULLATA:
        _stato =  this.us.getKeyByValue(UtilService.STATI_PENDENZE, UtilService.STATI_PENDENZE.NON_ESEGUITA);
      break;
    }
    this.fGroup.addControl('stato_ctrl', new FormControl(_stato, Validators.required));
    this.fGroup.addControl('descrizioneStato_ctrl', new FormControl(''));
    this.fGroup.controls['stato_ctrl'].disable();
  }

  ngAfterViewInit() {
  }

  /**
   * Interface IFormComponent: Form controls to json object
   * @returns {any}
   */
  mapToJson(): any {
    let _info = this.fGroup.value;
    let _json:any = {};

    _json.stato = this.fGroup.controls['stato_ctrl'].enabled?_info['stato_ctrl']:this.fGroup.controls['stato_ctrl'].value;
    _json.descrizioneStato = (_info['descrizioneStato_ctrl'])?_info['descrizioneStato_ctrl']:null;

    return _json;
  }
}
