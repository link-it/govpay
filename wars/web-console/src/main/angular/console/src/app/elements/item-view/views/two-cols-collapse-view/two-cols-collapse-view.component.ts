import { Input, Component, OnInit, AfterViewInit } from '@angular/core';
import { UtilService } from '../../../../services/util.service';
import { TwoColsCollapse } from '../../../../classes/view/two-cols-collapse';
import { Voce } from '../../../../services/voce.service';

@Component({
  selector: 'link-two-cols-collapse-view',
  templateUrl: './two-cols-collapse-view.component.html',
  styleUrls: ['./two-cols-collapse-view.component.scss']
})
export class TwoColsCollapseViewComponent implements OnInit, AfterViewInit {

  @Input() dataRef:any;

  @Input('info') info: TwoColsCollapse = new TwoColsCollapse();

  _expanded: boolean = false;
  protected _voce = Voce;

  constructor(public us: UtilService) {
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
  }

  protected _collapse(event) {
    if(event.target.innerText !== "open_in_new") {
      if(this.info.motivo || (this.info.elenco && this.info.elenco.length != 0)) {
        this._expanded = !this._expanded;
      }
    }
  }
}
