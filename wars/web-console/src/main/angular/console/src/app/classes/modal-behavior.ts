export class ModalBehavior {

  editMode: boolean = false;
  operation: string;
  info: any = {
    viewModel: null, //dialog data
    parent: null, //parent
    dialogTitle: '',
    templateName: ''
  };
  closure: Function;
  async_callback: Function;
  actions: any = {
    label: '',
    notifier: null
  };

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
