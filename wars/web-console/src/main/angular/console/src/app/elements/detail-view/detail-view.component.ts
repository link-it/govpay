import { Component, OnInit, ViewChild, AfterContentChecked } from '@angular/core';
import { ViewContainerRef, ComponentRef, OnDestroy } from '@angular/core';
import { LinkService } from "../../services/link.service";
import { UtilService } from '../../services/util.service';

@Component({
  selector: 'link-detail-view',
  templateUrl: './detail-view.component.html',
  styleUrls: ['./detail-view.component.css']
})
export class DetailViewComponent implements OnInit, AfterContentChecked, OnDestroy {
  @ViewChild('vcDetailRef', {read: ViewContainerRef}) vcRef: ViewContainerRef;

  protected _componentRef: ComponentRef<any>;
  protected _rsc: any;

  constructor(public ls:LinkService) { }

  ngOnInit() {
    this._rsc = this.ls.getRouterStateConfig();
    let _type = (this._rsc && this._rsc.data)?this._rsc.data.type:null;
    if(this.ls.routeHistory && this.ls.routeHistory.length != 0 && _type) {
        this._componentRef = this.ls.createComponent(_type, this.vcRef);
        (this._rsc.data && this._rsc.data.info)?this._componentRef.instance.json = this._rsc.data.info:null;
    } else {
      this.ls.navigateToMainList();
    }
  }

  ngOnDestroy() {
    if(this._componentRef) {
      this._componentRef.destroy();
    }
    this.vcRef.clear();
  }

  ngAfterContentChecked() {
    if(this._componentRef && this._componentRef.instance && this._rsc) {
      UtilService.ActiveDetailState = this._componentRef;
      if(this._componentRef.instance.modified) {
        this.ls.getRouterStateConfig(this._rsc.path).data.reuse = false;
        this._rsc.data.info = this._componentRef.instance.json;
        this._rsc.data.title = this._componentRef.instance.title();
        this._componentRef.instance.modified = false;
        setTimeout(() => {
          UtilService.headBehavior.next(this._rsc);
        }, 0, this);
      }
    }
  }

}
