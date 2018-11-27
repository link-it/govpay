import { Input, Component, OnInit, AfterViewInit } from '@angular/core';
import { Standard } from '../../../../classes/view/standard';
import { UtilService } from '../../../../services/util.service';

@Component({
  selector: 'link-standard-view',
  templateUrl: './standard-view.component.html',
  styleUrls: ['./standard-view.component.scss']
})
export class StandardViewComponent implements OnInit, AfterViewInit {

  @Input() dataRef:any;

  @Input('info') info: Standard = new Standard();

  constructor(public us: UtilService) {
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
    //console.log(this.dataRef);
    //this.outEmitter.emit({ element: this });
  }

  protected _hasStato(): boolean {
    return (this.info && this.info.stato !== null);
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
