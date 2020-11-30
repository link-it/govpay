export type Updater = {
  property: string,
  value: string
};

export class SocketNotification {

  notifier: Function = (info: any, updater: Function) => {};
  resetSocket: Function = null;
  URI: string = '';
  data: any = null;
  timeout: number = 30000;

  constructor (_data?: any) {
    if(_data) {
      for (let key in _data) {
        if(this.hasOwnProperty(key)) {
          (_data[key] !== null && _data[key] !== undefined)?this[key] = _data[key]:null;
        }
      }
    }
  }
}
