import { Component, ComponentRef, Input, OnInit } from '@angular/core';
import { Dato } from '../../classes/view/dato';
import { UtilService } from '../../services/util.service';
import { LinkService } from '../../services/link.service';
import { Voce } from '../../services/voce.service';
import { ModalBehavior } from '../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { IModalDialog } from '../../classes/interfaces/IModalDialog';
import { GovpayService } from '../../services/govpay.service';

@Component({
  selector: 'link-profilo',
  templateUrl: './profilo.component.html',
  styleUrls: ['./profilo.component.scss']
})
export class ProfiloComponent implements OnInit, IModalDialog {

  protected cards: any[] = [];
  protected username: string = '';
  protected info: any = { domini: [], tipiPendenza: [] };

  protected _voce = Voce;
  protected _us= UtilService;

  @Input() json: any;
  @Input() modified: boolean = false;

  constructor(public ls: LinkService, public us: UtilService, public gps: GovpayService) { }

  ngOnInit() {
    if(!UtilService.PROFILO_UTENTE) {
      this.ls.navigateToRoot();
    } else {
      this._mapProfilo();
    }
  }

  protected _mapProfilo(profilo?: any) {
    profilo = profilo || UtilService.PROFILO_UTENTE || {};
    this.username = profilo.nome;
    if(profilo.domini) {
      this.info.domini = profilo.domini.map((item, index) => {
        if(index === 0) {
          return new Dato({ label: Voce.ENTI_CREDITORI, value:  item.ragioneSociale });
        }
        return new Dato({ value:  item.ragioneSociale });
      });
    }
    if(profilo.tipiPendenza) {
      this.info.tipiPendenza = profilo.tipiPendenza.map((item, index) => {
        if(index === 0) {
          return new Dato({ label: 'Tipi pendenza', value:  item.descrizione });
        }
        return new Dato({ value:  item.descrizione });
      });
    }
    this.cards = profilo.acl.map(function(item) {
      let informazioni = [];
      informazioni.push(new Dato({ label:'Servizio', value: UtilService.MAP_ACL(item.servizio) }));
      let _auths = item.autorizzazioni.map((a) => {
        let _filter = UtilService.DIRITTI_CODE.filter((d) => {
          return d.code === a;
        });
        return _filter[0].label;
      }, this);
      informazioni.push(new Dato({ label: Voce.OPERAZIONI, value: _auths.length != 0?_auths.join(', '):'Nessuna.' }));

      // Sort Acls informazioni
      informazioni.sort((item1, item2) => {
        return (item1.value>item2.value)?1:(item1.value<item2.value)?-1:0;
      });
      return informazioni;
    });
  }

  protected _editProfilo() {
    let _mb = new ModalBehavior();
    _mb.editMode = true;
    _mb.info = {
      viewModel: UtilService.PROFILO_UTENTE,
      dialogTitle: 'Modifica profilo',
      templateName: UtilService.PROFILO_UTENTE
    };
    _mb.async_callback = this.save.bind(this);
    _mb.closure = this.refresh.bind(this);
    UtilService.dialogBehavior.next(_mb);
  }

  /**
   * Save Profilo (Patch )
   * @param {BehaviorSubject<any>} responseService
   * @param {ModalBehavior} mb
   */
  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {
    const _service = UtilService.URL_PROFILO;
    const _body = [{
      op: UtilService.PATCH_METHODS.REPLACE,
      path: "/password",
      value: mb.info.viewModel['password']
    }];
    if(mb.info.viewModel['password']) {
      this.gps.saveData(_service, _body, null, UtilService.METHODS.PATCH).subscribe(
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

  refresh(mb: ModalBehavior) { }
}
