import { Standard } from './standard';
import { SocketNotification } from '../socket-notification';

export class TwoCols extends Standard {

  data: string = null;

  generalTemplate: boolean = false;
  socketNotification: SocketNotification = null;
  gtTextUL: string = '';
  gtTextUR: string = '';
  gtTextBL: string = '';
  gtTextBR: string = '';


  constructor (_data?: any) {

    super(_data);

    if(_data) {
      for (let key in _data) {
        if(this.hasOwnProperty(key)) {
          (_data[key] !== null && _data[key] !== undefined)?this[key] = _data[key]:null;
        }
      }
    }
  }

  resetSocket() {
    this.socketNotification = null;
  }

  getStandardTitle(): string {
    if (this.generalTemplate) {
      if (this.gtTextUL) {
        return this.gtTextUL.trim();
      }
    }

    return super.getStandardTitle();
  }
}
