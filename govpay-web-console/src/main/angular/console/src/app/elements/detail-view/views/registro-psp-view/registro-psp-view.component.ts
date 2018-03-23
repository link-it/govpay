import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { IModalDialog } from '../../../../classes/interfaces/IModalDialog';
import { UtilService } from '../../../../services/util.service';
import { GovpayService } from '../../../../services/govpay.service';
import { Standard } from '../../../../classes/view/standard';
import { Dato } from '../../../../classes/view/dato';
import { Parameters } from '../../../../classes/parameters';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { ModalBehavior } from '../../../../classes/modal-behavior';

@Component({
  selector: 'link-registro-psp-view',
  templateUrl: './registro-psp-view.component.html',
  styleUrls: ['./registro-psp-view.component.scss']
})
export class RegistroPspViewComponent implements IModalDialog, OnInit, AfterViewInit {

  @Input() informazioni = [];
  @Input() canali = [];

  @Input() json: any;

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioRegistroPSP();
    this.elencoCanali();
  }

  ngAfterViewInit() {
  }

  protected dettaglioRegistroPSP() {
    let _ai = [];
    _ai.push(new Dato({ label: 'Ragione Sociale', value: this.json.ragioneSociale }));
    _ai.push(new Dato({ label: 'Abilitato', value: UtilService.ABILITA[this.json.abilitato.toString()] }));
    _ai.push(new Dato({ label: 'Bollo gestito', value: UtilService.ABILITAZIONI[this.json.bollo.toString()] }));
    _ai.push(new Dato({ label: 'Storno gestito', value: UtilService.ABILITAZIONI[this.json.storno.toString()] }));
    this.informazioni = _ai.slice(0);
  }

  protected elencoCanali() {
    this.gps.getDataService(this.json.canali).subscribe(function (_response) {
        let _body = _response.body;
        this.canali = _body['risultati'].map(function(item) {
          let _st = Dato.arraysToDato(
            ['Tipo', 'Abilitazione'],
            [
              UtilService.defaultDisplay({ value: UtilService.TIPI_VERSAMENTO[item.tipoVersamento] }),
              UtilService.defaultDisplay({ value: UtilService.ABILITAZIONI[(item.abilitato).toString()] })
            ],
            ', '
          );
          let _std = new Standard();
          _std.titolo = new Dato({ label: 'Canale: ', value: item.idCanale });
          _std.sottotitolo = _st;
          let p = new Parameters();
          p.model = _std;
          return p;
        }, this);
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        //console.log(error);
        this.us.alert(error.message);
        this.gps.updateSpinner(false);
      });
  }

  save(responseService: BehaviorSubject<any>, mb: ModalBehavior) {}
  refresh(mb: ModalBehavior) {}
}
