import { Standard } from './standard';
import { SocketNotification } from '../socket-notification';

export class Riepilogo extends Standard {

  extraInfo: Array<any> = []; // [ { label: 'Label', value: 'Value' } ];
  socketNotification: SocketNotification = new SocketNotification();
  avanzamento: string = null;

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
    this.avanzamento = '';
  }
}
