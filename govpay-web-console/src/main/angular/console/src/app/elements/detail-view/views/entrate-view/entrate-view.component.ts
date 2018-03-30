import { Component, Input, OnInit } from '@angular/core';
import { Dato } from '../../../../classes/view/dato';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { UtilService } from '../../../../services/util.service';
import { GovpayService } from '../../../../services/govpay.service';

@Component({
  selector: 'link-entrate-view',
  templateUrl: './entrate-view.component.html',
  styleUrls: ['./entrate-view.component.scss']
})
export class EntrateViewComponent implements IModalDialog, OnInit {

  @Input() informazioni = [];

  @Input() json: any;
  @Input() modified: boolean = false;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioEntrata();
  }

  protected dettaglioEntrata() {
    let _url = UtilService.URL_ENTRATE+'/'+encodeURIComponent(this.json.idEntrata);
    this.gps.getDataService(_url).subscribe(
      function (_response) {
        this.json = _response.body;
        this.mapJsonDetail();
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        //console.log(error);
        this.us.alert(error.message);
        this.gps.updateSpinner(false);
      });
  }

  protected mapJsonDetail() {
    //Riepilogo
    let _dettaglio = [];
    _dettaglio.push(new Dato({ label: 'Id entrata', value: this.json.idEntrata }));
    _dettaglio.push(new Dato({ label: 'Descrizione', value: this.json.descrizione }));
    _dettaglio.push(new Dato({ label: 'Tipo contabilita', value: this.json.tipoContabilita }));
    _dettaglio.push(new Dato({ label: 'Codice contabilita', value: this.json.codiceContabilita }));
    _dettaglio.push(new Dato({ label: 'Codifica IUV', value: this.json.codificaIUV }));

    this.informazioni = _dettaglio.slice(0);
  }

  protected _editEntrata(event: any) {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: this.json,
      dialogTitle: 'Modifica entrata',
      templateName: UtilService.ENTRATA
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.dialogBehavior.next(_mb);
  }

  /**
   * Save Entrata (Put to: /entrate/{idEntrata} )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    let _id = (mb.editMode)?this.json['idEntrata']:mb.info.viewModel['idEntrata'];
    let _service = UtilService.URL_ENTRATE+'/'+encodeURIComponent(_id);
    let _json = mb.info.viewModel;
    if(_json) {
      this.gps.saveData(_service, _json).subscribe(
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

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      this.modified = true;
      if(mb.editMode) {
        this.json = mb.info.viewModel;
        this.mapJsonDetail();
      }
    }
  }

  title(): string {
    return UtilService.defaultDisplay({ value: this.json?this.json.descrizione:null });
  }

}
