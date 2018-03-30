import { Component, Input } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { UtilService } from '../../../../services/util.service';
import { GovpayService } from '../../../../services/govpay.service';

@Component({
  selector: 'link-acls-view',
  template: ``
  // templateUrl: './acls-view.component.html',
  // styleUrls: ['./acls-view.component.scss']
})
export class AclsViewComponent implements IModalDialog {

  @Input() json: any;

  constructor(public gps: GovpayService, public us: UtilService) { }

  /**
   * Save ACL (Post to: /acl )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    if(mb && mb.info.viewModel) {
      let _service = UtilService.URL_ACL;
      this.json = mb.info.viewModel;
      this.gps.saveData(_service, this.json, null, UtilService.METHODS.POST).subscribe(
        () => {
          this.gps.updateSpinner(false);
          responseService.next(true);
        },
        (error) => {
          this.gps.updateSpinner(false);
          this.us.alert(error.message);
        });
    }
  }

  refresh(mb: ModalBehavior) {}

  title(): string {
    let _t = this.json?((this.json.principal)?this.json.principal:this.json.ruolo):null;
    return UtilService.defaultDisplay({ value: _t });
  }

  infoDetail(): any {
    return null;
  }

}
