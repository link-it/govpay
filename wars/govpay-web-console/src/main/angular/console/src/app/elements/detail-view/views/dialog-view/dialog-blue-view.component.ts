import { AfterViewInit, ChangeDetectorRef, Component, ComponentRef, HostListener, OnDestroy, OnInit } from '@angular/core';
import { EventEmitter, Input, Output, ViewChild, ViewContainerRef } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { LinkService } from '../../../../services/link.service';
import { UtilService } from '../../../../services/util.service';
import { ModalBehavior } from '../../../../classes/modal-behavior';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';

@Component({
  selector: 'link-dialog-blue-view',
  templateUrl: './dialog-blue-view.component.html',
  styleUrls: ['./dialog-blue-view.component.scss']
})
export class DialogBlueViewComponent implements OnInit, AfterViewInit, OnDestroy {
  @HostListener('window:resize', [ '$event' ]) onResize() {
    this._styleTopDialog();
  }
  @ViewChild('bdfec', {read: ViewContainerRef}) blueDialogFormElementContainer: ViewContainerRef;

  @Input('blue-data') _blueData: ModalBehavior;
  @Output('on-close-blue-dialog') onClose: EventEmitter<ModalBehavior> = new EventEmitter();

  protected _componentRef: ComponentRef<any>;
  protected _responseSubscription: Subscription;
  protected _Y: number = 64;

  info: any;
  blueModalForm: FormGroup;

  constructor(public ls: LinkService, private us: UtilService, private fb: FormBuilder,
              private changeDetector: ChangeDetectorRef) {
    this._Y = (this.ls.checkMediaMatch().matches)?56:64;
    this.blueModalForm = fb.group({});
  }

  ngOnInit() {
    if(this._blueData.info && this._blueData.info.templateName) {
      this._componentRef = this.ls.createComponent(this._blueData.info.templateName, this.blueDialogFormElementContainer);
      this._componentRef.instance.fGroup = this.blueModalForm;
      this._componentRef.instance.json = this._blueData.info.viewModel;
      this._componentRef.instance.parent = this._blueData.info.parent;
      this.changeDetector.detectChanges();
    }
  }

  ngAfterViewInit() {
  }

  ngOnDestroy() {
    if(this._responseSubscription) {
      this._responseSubscription.unsubscribe();
    }
  }

  protected _styleTopDialog() {
    this._Y = (this.ls.checkMediaMatch().matches)?56:64;
  }

  protected _close() {
    this._blueData.info.viewModel = undefined;
    this.onClose.emit(this._blueData);
  }

  protected _save(form) {
    if(this._blueData.async_callback) {
      this._blueData.info.viewModel = this._componentRef.instance.mapToJson();
      let responseService: BehaviorSubject<any> = new BehaviorSubject(null);
      this._responseSubscription = responseService.subscribe((value) => {
        if(value !== null) {
          this.onClose.emit(this._blueData);
          this.us.alert('Operazione completata.');
        }
      });
      this._blueData.async_callback(responseService, this._blueData);
    } else {
      this.onClose.emit(this._blueData);
    }
  }
}
