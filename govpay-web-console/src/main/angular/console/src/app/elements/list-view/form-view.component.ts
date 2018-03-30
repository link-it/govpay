import { AfterViewInit, Component, OnInit, Output, EventEmitter, ChangeDetectorRef, ChangeDetectionStrategy, Input } from '@angular/core';
import { ViewChild, ViewContainerRef } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { LinkService } from '../../services/link.service';
import { UtilService } from '../../services/util.service';

@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  selector: 'link-form-view',
  templateUrl: './form-view.component.html',
  styleUrls: [ './form-view.component.scss' ]
})
export class FormViewComponent implements OnInit, AfterViewInit {
  @ViewChild('fec', {read: ViewContainerRef}) formElementContainer: ViewContainerRef;

  @Input() fields: Array<any> = [];
  @Input('dialog-mode') dialog: boolean = false;
  @Input('modal-submit-button') modalForm: boolean = false;
  @Input('submit-label') submitLabel: string = 'Cerca';

  @Output('submit-form-data') onFormSubmit: EventEmitter<any> = new EventEmitter();

  basicForm: FormGroup;

  constructor(private ls: LinkService, private fb: FormBuilder, private us: UtilService,
              private changeDetector: ChangeDetectorRef) {
    this.basicForm = fb.group({});
  }

  ngOnInit() {
  }

  ngAfterViewInit() {
    let controls = {};
    this.fields.forEach((field) => {
      let _validators = [];
      if(field.required && !field.dependency) {
        _validators.push(Validators.required);
      }
      field.pattern?_validators.push(Validators.pattern(field.pattern)):null;
      controls[field.id+'_ctrl'] = new FormControl('', (_validators.length != 0)?_validators:null);
    });
    this.basicForm = this.fb.group(controls);
    this.fields.forEach(function(field) {
      setTimeout(() => {
        let _componentRef = this.ls.createComponent(field.type, this.formElementContainer);
        _componentRef.instance.fGroup = this.basicForm;
        _componentRef.instance.json = field;
        field.dependency?_componentRef.instance.json['source'] = controls[field.dependency+'_ctrl']:null;
        this.changeDetector.detectChanges();
      }, this);
    }, this);
  }

  protected onSubmit(_form) {
    this.onFormSubmit.emit({ value: _form.value })
  }

  resetForm() {
    this.basicForm.reset();
  }

  getFormData():any {
    if(this.basicForm) {
      return this.basicForm.value;
    }

    return null;
  }

}
