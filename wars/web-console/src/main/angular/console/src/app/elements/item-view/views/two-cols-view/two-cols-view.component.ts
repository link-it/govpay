import { AfterViewInit, Component, Input, OnDestroy } from '@angular/core';
import { UtilService } from '../../../../services/util.service';
import { TwoCols } from '../../../../classes/view/two-cols';
import { Updater } from '../../../../classes/socket-notification';

@Component({
  selector: 'link-two-cols-view',
  templateUrl: './two-cols-view.component.html',
  styleUrls: ['./two-cols-view.component.scss']
})
export class TwoColsViewComponent implements AfterViewInit, OnDestroy {

  @Input() dataRef:any;

  @Input('info') info: TwoCols = new TwoCols();

  constructor(public us: UtilService) {}

  ngAfterViewInit() {
    if (this.info && this.info.generalTemplate) {
      if (this.info.socketNotification && this.info.socketNotification.notifier) {
        this.info.socketNotification.notifier(this.info, this.updater.bind(this));
      }
    }
  }

  ngOnDestroy() {
    this.resetSocket();
  }

  protected updater(data: Updater) {
    if (data && data.property) {
      switch (data.property) {
        case 'gtTextUL':
        case 'gtTextUR':
        case 'gtTextBL':
        case 'gtTextBR':
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
