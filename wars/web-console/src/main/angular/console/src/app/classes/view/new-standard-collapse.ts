import { Standard } from './standard';

export class NewStandardCollapse extends Standard {

  motivo: string = null;
  elenco: any[] = null;

  item: any = null;

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
