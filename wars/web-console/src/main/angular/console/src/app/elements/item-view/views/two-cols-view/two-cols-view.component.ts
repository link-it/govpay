import { Component, Input, OnInit } from '@angular/core';
import { UtilService } from '../../../../services/util.service';
import { TwoCols } from '../../../../classes/view/two-cols';

@Component({
  selector: 'link-two-cols-view',
  templateUrl: './two-cols-view.component.html',
  styleUrls: ['./two-cols-view.component.scss']
})
export class TwoColsViewComponent implements OnInit {

  @Input() dataRef:any;

  @Input('info') info: TwoCols = new TwoCols();

  constructor(public us: UtilService) {
  }

  ngOnInit() {
  }

// protected _titleStyle() {
//   let _trunc: boolean = true;
//   if((this.info.titolo && this.info.titolo.label && this.info.titolo.label.indexOf(' ') != -1) ||
//     (this.info.titolo && this.info.titolo.value && this.info.titolo.value.indexOf(' ') != -1)) {
//     _trunc = false;
//   }
//   return {
//     'text-truncate': _trunc,
//     'text-normal-font': true
//   }
// }

// protected _subTitleStyle() {
//   let _trunc: boolean = true;
//   if((this.info.sottotitolo && this.info.sottotitolo.label && this.info.sottotitolo.label.indexOf(' ') != -1) ||
//     (this.info.sottotitolo && this.info.sottotitolo.value && this.info.sottotitolo.value.indexOf(' ') != -1)) {
//     _trunc = false;
//   }
//   return {
//     'text-truncate': _trunc,
//     'color-gray': true,
//     'text-small-font': true
//   }
// }
}
