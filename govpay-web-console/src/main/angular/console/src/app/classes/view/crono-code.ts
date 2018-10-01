import { Standard } from './standard';

export class CronoCode extends Standard {

  data: string = '00/00/0000 hh:mm';
  codice: string = '0000';

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
}
