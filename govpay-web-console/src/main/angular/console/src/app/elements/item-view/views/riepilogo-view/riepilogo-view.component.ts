import { Component, Input, OnInit } from '@angular/core';
import { Riepilogo } from '../../../../classes/view/riepilogo';
import { UtilService } from '../../../../services/util.service';

@Component({
  selector: 'link-riepilogo-view',
  templateUrl: './riepilogo-view.component.html',
  styleUrls: ['./riepilogo-view.component.scss']
})
export class RiepilogoViewComponent implements OnInit {

  @Input('info') info: Riepilogo = new Riepilogo();

  constructor(public us: UtilService) { }

  ngOnInit() {
  }

}
