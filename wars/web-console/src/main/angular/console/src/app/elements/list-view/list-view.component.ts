import { AfterContentChecked, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { LinkService } from '../../services/link.service';
import { UtilService } from '../../services/util.service';
import { SideListComponent } from './side-list.component';
import { FormViewComponent } from './form-view.component';
import { query } from '@angular/core/src/render3/instructions';

@Component({
  selector: 'link-list-view',
  templateUrl: './list-view.component.html',
  styleUrls: ['./list-view.component.scss']
})
export class ListViewComponent implements OnInit, AfterContentChecked {
  @ViewChild('cheat') cheat: ElementRef;
  @ViewChild('fv') formView: FormViewComponent;
  @ViewChild('sl') sideList: SideListComponent;

  protected _showFormView: boolean = true;
  protected _preventMultiCall: boolean = false;
  protected _fields: any[];

  constructor(public ls: LinkService, private us: UtilService) { }

  ngOnInit() {
    let _rsc = this.ls.getRouterStateConfig();
    this._fields = this.us.fieldsForService(_rsc.data.type);
    Form.fields = (this._fields && this._fields.length !== 0);
  }

  ngAfterContentChecked() {
    if(this._fields) {
      this._showFormView = this._fields.length !== 0;
    }
  }

  protected _formSubmit(event) {
    if(this.sideList) {
	  let _rsc = this.ls.getRouterStateConfig();
	  let fixNAV_IUV = false;
	  if (_rsc.data.type === UtilService.GIORNALE_EVENTI) {
	          fixNAV_IUV = true;
	        }
      let _query:string = this._formQuery(event, fixNAV_IUV);
      
      if (_rsc.data.type === UtilService.RICEVUTE) {
        _query += '&esito=ESEGUITO';
      }

      this.sideList.getList(null, _query);
      this.sideList.loadMetadati(null, _query);
    }
  }

  protected _formQuery(event, fixNAV_IUV): string {
    if(this.sideList) {
      let _keys: string[] = (event.value)?Object.keys(event.value):[];
      let _values = [];
      _keys.forEach(function(key) {
        if(this.us.hasValue(event.value[key])) {
          let _key = key.replace('_ctrl','');
		  if(_key === 'iuv' && fixNAV_IUV) {
			let searchIUVVal = UtilService.navToIuv(event.value[key]);
			_values.push(`${_key}=${searchIUVVal}`);			
		  } else {
			_values.push(`${_key}=${event.value[key]}`);
		  }
        }
      }, this);
      return _values.join('&');
    }
    return [].join('&');
  }

  protected formFieldsData() {
    let _fd;
    if(this.formView) {
      _fd = this.formView.getFormData();
    }

    return _fd;
  }

  /**
   * _loadMoreData: Infinite scrolling
   * @private
   */
  protected _loadMoreData() {
    //let _fd = this.formFieldsData();
    //let _query = this._formQuery({ value: _fd });
    let _results = this.sideList.getLastResult();
    if(_results && _results['prossimiRisultati']) {
      this.sideList.getList(_results['prossimiRisultati'], null, true);
    }
  }

  protected _isVisible() {
    return Form.toggle || (this._showFormView && this.ls.checkMediumMediaMatch().matches);
  }

  protected _cheatClass(target: any) {
    return {
      'to-top': true,
      'd-none': ((target && target.scrollTop <= window.innerHeight) || window.innerWidth < 768)
    }
  }

  protected _toTop(target: any) {
    if (target) {
      target.scrollTop = 0;
    }
  }

}

export const Form: any = { toggle: false, fields: false };
