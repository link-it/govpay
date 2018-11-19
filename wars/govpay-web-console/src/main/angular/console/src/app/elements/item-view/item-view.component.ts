import { Component, OnInit, AfterViewInit, ViewChild, Output, EventEmitter, OnDestroy, ComponentRef, AfterContentChecked } from '@angular/core';
import { Input, ViewContainerRef } from '@angular/core';
import { LinkService } from "../../services/link.service";
import { Parameters } from "../../classes/parameters";
import { trigger, transition, style, animate } from '@angular/animations';

@Component({
  selector: 'link-item-view',
  templateUrl: './item-view.component.html',
  styleUrls: ['./item-view.component.scss'],
  animations: [
    trigger(
      'IconViewAnimation',
      [
        transition(
          ':enter', [
            style({transform: 'translateX(100%)'}),
            animate('250ms', style({transform: 'translateX(0)', opacity: 1}))
          ]
        ),
        transition(
          ':leave', [
            style({transform: 'translateX(0)'}),
            animate('250ms', style({transform: 'translateX(100%)', opacity: 0}))
          ]
        )
      ]
    )
  ],
})
export class ItemViewComponent implements OnInit, AfterViewInit, OnDestroy, AfterContentChecked {
  @ViewChild('vcRef', {read: ViewContainerRef}) vcRef: ViewContainerRef;

  @Input('use-item-view-directive') _itemDirective: boolean = false;
  @Input('component-data') _componentData: Parameters;
  @Input('show-over-icons') _showOverIcons: boolean = true;
  @Input('over-icons') _icons: string[];

  @Output('icon-click') onIconClick: EventEmitter<any> = new EventEmitter();

  _showIcons: boolean = false;
  _hasSwiped: boolean = false;
  _componentRef: ComponentRef<any>;

  constructor(private ls:LinkService) {
  }

  ngOnInit() {
    if(this._componentData){
        this._componentRef = this.ls.createComponent(this._componentData.type, this.vcRef);
        this._componentRef.instance.info = this._componentData.model;
    }
  }

  ngOnDestroy() {
    if(this._componentRef) {
      this._componentRef.destroy();
    }
    this.vcRef.clear();
  }

  ngAfterContentChecked() {
    if(this._componentRef && this._componentRef.instance && this._componentData && this._componentData.model) {
      if(this._componentRef.instance.info !== this._componentData.model) {
        this._componentRef.instance.info = this._componentData.model;
      }
    }
  }

  ngAfterViewInit() {
  }

  protected _swipeLeft() {
    //console.log('_swipeLeft');
    this._hasSwiped = true;
    this._showIcons = true;
  }

  protected _onOver() {
    this._showIcons = true;
  }

  protected _onOut() {
    (!this._hasSwiped)?this._showIcons = false:null;
  }

  protected _itemClick(event) {
    (this._hasSwiped)?event.stopImmediatePropagation():null;
    (this._hasSwiped)?this._showIcons = false:null;
    this._hasSwiped = false;
  }

  protected _iconItemClick(event, source) {
    event.stopImmediatePropagation();
    (this._hasSwiped)?this._showIcons = false:null;
    this.onIconClick.emit({ target: this, type: source });
  }

  getItemView(): any {
    return this.vcRef.get(this.vcRef.length - 1);
  }

  getItemViewModel(): Parameters {
    return this._componentData;
  }

}
