import { Component, Input, OnInit } from '@angular/core';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { UtilService } from '../../../../services/util.service';
import { GovpayService } from '../../../../services/govpay.service';
import { Voce } from '../../../../services/voce.service';
import { Dato } from '../../../../classes/view/dato';

@Component({
  selector: 'link-ruoli-view',
  templateUrl: './ruoli-view.component.html',
  styleUrls: ['./ruoli-view.component.scss']
})
export class RuoliViewComponent implements OnInit, IModalDialog {

  @Input() json: any;
  @Input() autorizzazioni = [];

  protected voce = Voce;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioRuoli();
  }

  protected dettaglioRuoli() {
    let _service = UtilService.URL_RUOLI+'/'+this.json.id;
    this.gps.getDataService(_service).subscribe(function (_response) {
        this.json.acl = _response.body['acl'];
        this.mapAutorizzazioni();
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected mapAutorizzazioni() {
    this.autorizzazioni = [];
    if(this.json.acl.length != 0) {
      this.json.acl.forEach((item) => {
        let auths = item.autorizzazioni.map((s) => {
          const codes = UtilService.DIRITTI_CODE.filter((a) => {
            return (a.code == s);
          });
          return (codes.length!=0)?codes[0].label:'';
        });
        this.autorizzazioni.push(new Dato({ label: this.us.mapACL(item.servizio), value: auths.join(', ') }));
      });
      // Sort Acls
      this.autorizzazioni.sort((item1, item2) => {
        return (item1.label>item2.label)?1:(item1.label<item2.label)?-1:0;
      });
    }
  }


  protected _editRuolo(event: any) {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: this.json,
      dialogTitle: 'Modifica ruolo',
      templateName: UtilService.RUOLO
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.blueDialogBehavior.next(_mb);
  }

  /**
   * Refresh
   * @param {ModalBehavior} mb
   */
  refresh(mb: ModalBehavior) {
    if(mb && mb.info.viewModel) {
      this.dettaglioRuoli();
    }
  }

  /**
   * Save Ruolo
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    if(mb && mb.info.viewModel) {
      let _service = UtilService.URL_RUOLI+'/' + encodeURIComponent(mb.info.viewModel.id);
      let _json = JSON.parse(JSON.stringify(mb.info.viewModel));
      delete _json.id;
      this.gps.saveData(_service, _json).subscribe(
        () => {
          this.gps.updateSpinner(false);
          responseService.next(true);
        },
        (error) => {
          this.gps.updateSpinner(false);
          this.us.onError(error);
        });
    }
  }

  title(): string {
    let _t = this.json?((this.json.principal)?this.json.principal:this.json.ruolo):null;
    return UtilService.defaultDisplay({ value: _t });
  }

  infoDetail(): any {
    return null;
  }

}
