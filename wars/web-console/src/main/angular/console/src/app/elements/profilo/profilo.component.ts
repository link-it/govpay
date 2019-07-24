import { Component, OnInit } from '@angular/core';
import { Dato } from '../../classes/view/dato';
import { UtilService } from '../../services/util.service';
import { LinkService } from '../../services/link.service';
import { Voce } from '../../services/voce.service';

@Component({
  selector: 'link-profilo',
  templateUrl: './profilo.component.html',
  styleUrls: ['./profilo.component.scss']
})
export class ProfiloComponent implements OnInit {

  protected cards: any[] = [];
  protected username: string = '';
  protected info: any = { domini: [], tipiPendenza: [] };

  protected _voce = Voce;

  constructor(public ls: LinkService, public us: UtilService) { }

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

}
