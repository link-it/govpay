import { Component, Input, OnInit } from '@angular/core';
import { Crono } from '../../../../classes/view/crono';
import { UtilService } from '../../../../services/util.service';

@Component({
  selector: 'link-crono-view',
  templateUrl: './crono-view.component.html',
  styleUrls: ['./crono-view.component.scss']
})
export class CronoViewComponent implements OnInit {

  @Input() dataRef:any;

  @Input('info') info: Crono = new Crono();

  constructor(public us: UtilService) { }

  ngOnInit() {
  }

  protected _titleTT(): string {
    let _att = [];
    if(this.info) {
      (this.info.titolo)?_att.push(this.info.titolo.label):null;
      (this.info.titolo)?_att.push(this.info.titolo.value):null;
    }

    return _att.join(' ');
  }

  protected _subtitleTT(): string {
    let _att = [];
    if(this.info) {
      (this.info.sottotitolo)?_att.push(this.info.sottotitolo.label):null;
      (this.info.sottotitolo)?_att.push(this.info.sottotitolo.value):null;
    }

    return _att.join(' ');
  }

}
