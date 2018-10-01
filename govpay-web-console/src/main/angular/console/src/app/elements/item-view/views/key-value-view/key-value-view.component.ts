import { Component, Input, OnInit } from '@angular/core';
import { Dato } from '../../../../classes/view/dato';

@Component({
  selector: 'link-key-value-view',
  templateUrl: './key-value-view.component.html',
  styleUrls: ['./key-value-view.component.scss']
})
export class KeyValueViewComponent implements OnInit {

  @Input('info') info: Dato = new Dato();

  constructor() { }

  ngOnInit() {
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
