import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Riepilogo } from '../../../../classes/view/riepilogo';
import { UtilService } from '../../../../services/util.service';
import { Updater } from '../../../../classes/socket-notification';


@Component({
  selector: 'link-riepilogo-view',
  templateUrl: './riepilogo-view.component.html',
  styleUrls: ['./riepilogo-view.component.scss']
})
export class RiepilogoViewComponent implements OnInit, OnDestroy {

  @Input('info') info: Riepilogo = new Riepilogo();

  constructor(public us: UtilService) { }

  ngOnInit() {
  }

  ngOnDestroy() {
    this.resetSocket();
  }

  ngAfterViewInit() {
    if (this.info && this.info.socketNotification && this.info.socketNotification.notifier) {
      this.info.socketNotification.notifier(this.info, this.updater.bind(this));
    }
  }

  protected updater(data: Updater) {
    if (data && data.property) {
      switch (data.property) {
        case 'avanzamento':
        case 'stato':
          this.info[data.property] = (data.value !== null && data.value !== undefined)?data.value:'';
          break;
        default:
      }
    }
  }

  protected resetSocket() {
    if (this.info) {
      this.info.resetSocket();
    }
  }

}
