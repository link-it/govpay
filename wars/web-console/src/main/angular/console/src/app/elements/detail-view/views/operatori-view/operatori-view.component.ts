import { Component, Input, OnInit } from '@angular/core';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';
import { Voce } from '../../../../services/voce.service';
import { Dato } from '../../../../classes/view/dato';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../classes/modal-behavior';

@Component({
  selector: 'link-operatori-view',
  templateUrl: './operatori-view.component.html',
  styleUrls: ['./operatori-view.component.scss']
})
export class OperatoriViewComponent implements IModalDialog, OnInit {

  @Input() informazioni = [];
  @Input() acls = [];

  @Input() json: any;
  @Input() modified: boolean = false;

  protected voce = Voce;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioOperatore();
  }

  protected dettaglioOperatore() {
    if(this.json) {

      let _dettaglio = [];
      _dettaglio.push(new Dato({ label: Voce.PRINCIPAL, value: this.json.principal }));
      _dettaglio.push(new Dato({ label: Voce.NOME, value: this.json.ragioneSociale }));
      _dettaglio.push(new Dato({ label: Voce.ABILITATO, value: UtilService.ABILITA[this.json.abilitato.toString()] }));

      if(this.json.domini.length != 0) {
        this.json.domini.forEach((item, index) => {
          _dettaglio.push(new Dato({ label: (index != 0)?'':Voce.DOMINI, value: item.ragioneSociale }));
        });
      } else {
        _dettaglio.push(new Dato({ label: Voce.DOMINI, value: 'Nessuna informazione' }));
      }
      if(this.json.tipiPendenza.length != 0) {
        this.json.tipiPendenza.forEach((item, index) => {
          _dettaglio.push(new Dato({ label: (index != 0)?'':Voce.PENDENZE, value: item.descrizione }));
        });
      } else {
        _dettaglio.push(new Dato({ label: Voce.PENDENZE, value: 'Nessuna informazione' }));
      }

      this.acls =[];
      if(this.json.acl.length != 0) {
        this.json.acl.forEach((item) => {
          let auths = item.autorizzazioni.map((s) => {
            const codes = UtilService.DIRITTI_CODE.filter((a) => {
              return (a.code == s);
            });
            return (codes.length!=0)?codes[0].label:'';
          });
          this.acls.push(new Dato({ label: this.us.mapACL(item.servizio), value: auths.join(', ') }));
        });
        // Sort Acls
        this.acls.sort((item1, item2) => {
          return (item1.label>item2.label)?1:(item1.label<item2.label)?-1:0;
        });
      } else {
        _dettaglio.push(new Dato({ label: Voce.AUTORIZZAZIONI_BACKOFFICE, value: 'Nessuna autorizzazione' }));
      }

      this.informazioni = _dettaglio.slice(0);
    }

  }

  protected _editOperatore(event: any) {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: this.json,
      dialogTitle: 'Modifica operatore',
      templateName: UtilService.OPERATORE
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.blueDialogBehavior.next(_mb);
  }

  refresh(mb: ModalBehavior) {
    this.modified = false;
    if(mb && mb.info && mb.info.viewModel) {
      this.modified = true;
      if(mb.info.templateName === UtilService.OPERATORE) {
        this.json = mb.info.viewModel;
        this.dettaglioOperatore();
      }
    }
  }

  /**
   * Save Operatore|Dominio|tipiPendenza|Acl (Put to: /operatori/{principal} )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    if(mb && mb.info.viewModel) {
      let _json;
      let _query = null;
      let _method = null;
      let _id = (mb.editMode)?this.json['principal']:mb.info.viewModel['principal'];
      let _service = UtilService.URL_OPERATORI+'/'+encodeURIComponent(_id);
      if(mb.info.templateName === UtilService.OPERATORE) {
        _json = {
          ragioneSociale: mb.info.viewModel.ragioneSociale,
          principal: mb.info.viewModel.principal,
          abilitato: mb.info.viewModel.abilitato,
          tipiPendenza: mb.info.viewModel.tipiPendenza,
          domini: mb.info.viewModel.domini,
          acl: mb.info.viewModel.acl
        };
        delete _json.principal;
        _json.domini = _json.domini.map((d) => {
          return d.idDominio;
        });
        _json.tipiPendenza = _json.tipiPendenza.map((e) => {
          return e.idTipoPendenza;
        });
      }
      this.gps.saveData(_service, _json, _query, _method).subscribe(
        (response) => {
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
    return UtilService.defaultDisplay({ value: this.json?this.json.ragioneSociale:null });
  }

  infoDetail(): any {
    return {};
  }


}
