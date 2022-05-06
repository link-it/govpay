import { Input, Component, OnInit, AfterViewInit } from '@angular/core';
import { GovpayService } from '../../../../services/govpay.service';
import { UtilService } from '../../../../services/util.service';

declare let JSZip: any;
declare let FileSaver: any;

@Component({
  selector: 'link-allegato-view',
  templateUrl: './allegato-view.component.html',
  styleUrls: ['./allegato-view.component.scss']
})
export class AllegatoViewComponent implements OnInit, AfterViewInit {

  @Input() info: any;

  constructor(public gps: GovpayService, public us: UtilService) {
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
    //console.log(this.dataRef);
    //this.outEmitter.emit({ element: this });
  }

  _downloadFile() {
    const filename = this.info.item.nome;
    const type = this.info.item.tipo;
    const link = this.info.item.contenuto;

    this.gps.file(link, type).subscribe(
      (_response) => {
        this.gps.updateSpinner(false);
        this._saveFile(_response.body, filename, type);
      },
      (error) => {
        this.gps.updateSpinner(false);
        this.us.onError(error);
      });
  }

  protected _saveFile(data: string, fileName: string, type: string) {
    try {
      FileSaver(data, fileName);
    } catch (e) {
      console.log('Si è verificato un errore non previsto durante la creazione del file.');
    }
  }
}
