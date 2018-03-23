import { AfterViewInit, Component, OnInit } from '@angular/core';
import { Dato } from '../../classes/view/dato';
import { UtilService } from '../../services/util.service';
import { GovpayService } from '../../services/govpay.service';

@Component({
  selector: 'link-profilo',
  templateUrl: './profilo.component.html',
  styleUrls: ['./profilo.component.scss']
})
export class ProfiloComponent implements OnInit, AfterViewInit {

  protected cards: any[] = [];

  constructor(public gps: GovpayService, public us: UtilService) { }

  ngOnInit() {
    this.dettaglioProfilo();
  }

  ngAfterViewInit() {
  }

  dettaglioProfilo() {
    let _service = UtilService.URL_PROFILO;
    this.gps.getDataService(_service).subscribe(function (_response) {
        let _body = _response.body;
        this.cards = _body.map(function(item) {
          let informazioni = [];
          informazioni.push(new Dato({ label:'Id', value: item.id }));
          if(item.principal) {
            informazioni.push(new Dato({ label:'Principal', value: item.principal }));
          }
          if(item.ruolo) {
            informazioni.push(new Dato({ label:'Ruolo', value: item.ruolo }));
          }
          informazioni.push(new Dato({ label:'Servizio', value: item.servizio }));
          item.autorizzazioni.forEach((a) => {
            informazioni.push(new Dato({ label:'Tipo autorizzazione', value: a }));
          }, this);
          return informazioni;
        });
        this.gps.updateSpinner(false);
      }.bind(this),
      (error) => {
        this.gps.updateSpinner(false);
        this.us.alert(error.message);
      });
  }

}
