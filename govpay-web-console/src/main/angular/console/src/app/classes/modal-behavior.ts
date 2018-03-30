export class ModalBehavior {

  editMode: boolean = false;
  info: any = {
    viewModel: null, //dialog data
    parent: null, //parent
    dialogTitle: '',
    templateName: ''
  };
  closure: Function;
  async_callback: Function;

  constructor (_data?: any) {
    if(_data) {
      for (let key in _data) {
        if(this.hasOwnProperty(key)) {
          this[key] = _data[key];
        }
      }
    }
  }
}
