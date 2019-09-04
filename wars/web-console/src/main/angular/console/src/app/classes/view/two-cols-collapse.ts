import { TwoCols } from './two-cols';

export class TwoColsCollapse extends TwoCols {

  motivo: string = null;
  elenco: any[] = null;
  url: string = null;

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
