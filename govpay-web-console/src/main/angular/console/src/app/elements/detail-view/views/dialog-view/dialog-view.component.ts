import { Component, Inject, OnInit, ViewChild, ViewContainerRef, AfterViewInit, ComponentRef, OnDestroy, AfterContentChecked } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material';
import { LinkService } from '../../../../services/link.service';
import { UtilService } from '../../../../services/util.service';
import { FormGroup, FormBuilder } from '@angular/forms';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import { Subscription } from 'rxjs/Subscription';
import { ModalBehavior } from '../../../../classes/modal-behavior';

@Component({
  selector: 'link-dialog-view',
  templateUrl: './dialog-view.component.html',
  styleUrls: ['./dialog-view.component.scss']
})
export class DialogViewComponent  implements OnInit, OnDestroy, AfterContentChecked {
  @ViewChild('dfec', {read: ViewContainerRef}) dialogFormElementContainer: ViewContainerRef;

  protected _componentRef: ComponentRef<any>;
  protected _responseSubscription: Subscription;
  protected _isValid: boolean = false;

  modalForm: FormGroup;

  constructor(public dialogRef: MatDialogRef<DialogViewComponent>, @Inject(MAT_DIALOG_DATA) public data: ModalBehavior,
              private ls: LinkService, private us: UtilService, private fb: FormBuilder) {
    this.modalForm = fb.group({});
    //this.info = data.info;
    //console.log('Data', data);
  }

  ngOnInit() {
    if(this.data.info.templateName) {
      this._componentRef = this.ls.createComponent(this.data.info.templateName, this.dialogFormElementContainer);
      this._componentRef.instance.fGroup = this.modalForm;
      this._componentRef.instance.json = this.data.info.viewModel;
      this._componentRef.instance.parent = this.data.info.parent;
    }
  }

  ngAfterContentChecked() {
    this._isValid = this.modalForm.valid;
  }

  ngOnDestroy() {
    if(this._responseSubscription) {
      this._responseSubscription.unsubscribe();
    }
  }

  protected _close(value?: any) {
    this.dialogRef.close(value);
  }

  protected _save(_form) {
    if(this.modalForm.valid) {
      this.data.info.viewModel = this._componentRef.instance.mapToJson();
      if(this.data.async_callback) {
        let responseService: BehaviorSubject<any> = new BehaviorSubject(null);
        this._responseSubscription = responseService.subscribe((value) => {
          if(value !== null) {
            this._close(this.data);
            this.us.alert('Operazione completata.');
          }
        });
        this.data.async_callback(responseService, this.data);
      } else {
        this._close(this.data);
      }
    }
  }

  protected resetForm() {
    this.modalForm.reset();
  }

  getFormData():any {
    if(this.modalForm) {
      return this.modalForm.value;
    }

    return null;
  }
}
