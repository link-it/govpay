import { Input, Component, OnInit, AfterViewInit } from '@angular/core';
import { UtilService } from '../../../../services/util.service';
import { StandardCollapse } from '../../../../classes/view/standard-collapse';

@Component({
  selector: 'link-standard-collapse-view',
  templateUrl: './standard-collapse-view.component.html',
  styleUrls: ['./standard-collapse-view.component.scss']
})
export class StandardCollapseViewComponent implements OnInit, AfterViewInit {

  @Input() dataRef:any;

  @Input('info') info: StandardCollapse = new StandardCollapse();

  _expanded: boolean = false;

  constructor(public us: UtilService) {
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
    //console.log(this.dataRef);
    //this.outEmitter.emit({ element: this });
  }

  // protected _hasStato(): boolean {
  //   return (this.info && this.info.stato !== null);
  // }

  // protected _titleStyle() {
  //   let _trunc: boolean = true;
  //   if((this.info.titolo && this.info.titolo.label && this.info.titolo.label.indexOf(' ') != -1) ||
  //     (this.info.titolo && this.info.titolo.value && this.info.titolo.value.indexOf(' ') != -1)) {
  //     _trunc = false;
  //   }
  //   return {
  //     'text-truncate': _trunc,
  //     'medium-16': true
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
  //     'medium-14': true
  //   }
  // }

  protected _collapse() {
    if(this.info.motivo || (this.info.elenco && this.info.elenco.length != 0)) {
      this._expanded = !this._expanded;
    }
  }
}
