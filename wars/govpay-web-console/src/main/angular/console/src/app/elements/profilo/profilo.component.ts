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
  protected domini: string = '';
  protected entrate: string = '';

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
    this.domini = profilo.domini?profilo.domini.join(', '):'';
    this.entrate = profilo.entrate?profilo.entrate.join(', '):'';
    this.cards = profilo.acl.map(function(item) {
      let informazioni = [];
      informazioni.push(new Dato({ label:'Servizio', value: item.servizio }));
      let _auths = item.autorizzazioni.map((a) => {
        let _filter = UtilService.DIRITTI_CODE.filter((d) => {
          return d.code === a;
        });
        return _filter[0].label;
      }, this);
      informazioni.push(new Dato({ label: Voce.OPERAZIONI, value: _auths.length != 0?_auths.join(', '):'Nessuna.' }));
      return informazioni;
    });
  }

}
