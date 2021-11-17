import { Component, Input, OnChanges, OnInit, SimpleChanges } from '@angular/core';
import { DatoEx } from '../../../../classes/view/dato-ex';

@Component({
  selector: 'link-key-json-view',
  templateUrl: './key-json-view.component.html',
  styleUrls: ['./key-json-view.component.scss']
})
export class KeyJsonViewComponent implements OnInit, OnChanges {

  @Input('info') info: DatoEx = new DatoEx();

  quoteKeys: any[] = [];

  // quoteOrder = ['capitolo', 'annoEsercizio', 'importo', 'titolo', 'accertamento', 'tipologia', 'categoria', 'articolo'];
  quoteOrder = ['titolo', 'tipologia', 'categoria', 'capitolo', 'articolo', 'accertamento', 'annoEsercizio', 'importo'];
  quoteLabel = {
    capitolo: 'Capitolo',
    annoEsercizio: 'Anno esercizio',
    importo: 'Importo',
    titolo: 'Titolo',
    accertamento: 'Accertamento',
    tipologia: 'Tipologia',
    categoria: 'Categoria',
    articolo: 'Articolo',
    proprietaCustom: 'Proprieta custom'
  };

  constructor() { }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes.info && changes.info.currentValue) {
      this.prepareData();
    }
  }

  prepareData() {
    if ((this.info.type == 'quote') && this.info.value && this.info.value[0]) {
      this.quoteKeys = Object.keys(this.info.value[0]);
      const sorted = this.quoteKeys.sort((a, b) => this.quoteOrder.indexOf(a) - this.quoteOrder.indexOf(b));
      this.quoteKeys = sorted;
    }
  }

  // protected _setLabelStyle() {
  //   let _trunc: boolean = true;
  //   if(this.info && this.info.value && this.info.value.indexOf(' ') != -1) {
  //     _trunc = false;
  //   }
  //   return {
  //     'text-truncate': _trunc,
  //     'color-gray': true,
  //     'my-0': true
  //   }
  // }

  // protected _tooltip(): string {
  //   if(this.info) {
  //     return this.info.label+': '+this.info.value;
  //   }
  //   return null;
  // }

}
