import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormInput } from '../../../../classes/view/form-input';
import { IFormComponent } from '../../../../classes/interfaces/IFormComponent';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import { map, startWith } from 'rxjs/operators';

@Component({
  selector: 'link-filterable-view',
  templateUrl: './filterable-view.component.html',
  styleUrls: ['./filterable-view.component.scss']
})
export class FilterableViewComponent implements IFormComponent, OnInit, AfterViewInit {

  @Input() json: FormInput = new FormInput();
  @Input() fGroup: FormGroup;

  filteredOptions: Observable<any[]>;

  constructor() { }

  ngOnInit() {
    this.filteredOptions = this.fGroup.controls[this.json.id+'_ctrl'].valueChanges
      .pipe(
        startWith(''),
        map((value) => this._filter(value))
      );
  }

  ngAfterViewInit() {
    if(this.json) {
      if (this.json.promise && this.json.promise.async) {
        document.removeEventListener(this.json.promise.eventType, this._asyncLoaded.bind(this));
        document.addEventListener(this.json.promise.eventType, this._asyncLoaded.bind(this));
        this.json.asyncValues();
      }
      if (this.json.value !== null && this.json.value !== undefined) {
        //Default selection
        this.fGroup.controls[this.json.id+'_ctrl'].setValue(this.json.value);
      }
    }
  }

  protected _reset() {
    this.fGroup.controls[this.json.id+'_ctrl'].setValue(  '');
  }

  protected _filter(value: any): any[] {
    const filterValue = value?value.toLowerCase():'';

    return this.json.values.filter((option) => {
      return (option.label.toLowerCase().includes(filterValue) || option.value.includes(filterValue));
    });
  }

  protected _asyncLoaded() {
    if(this.json && this.json.promise && this.json.promise.async) {
      if(this.json.promise.loaded) {
        this.fGroup.controls[this.json.id+'_ctrl'].enable();
        if(this.json.promise.preventSelection) {
          if(this.json.values.length <= 1) {
            this.fGroup.controls[this.json.id+'_ctrl'].setValue(this.json.values[0].value);
            this.fGroup.controls[this.json.id+'_ctrl'].disable();
          }
        }
      } else {
        this.fGroup.controls[this.json.id+'_ctrl'].disable();
      }
    }
  }

  verifyDisplay(): boolean {
    let _showMe: boolean = !this.json.dependency;
    if(this.json && this.json.source) {
      _showMe = (this.json.source.value == this.json.target);
    }
    this.markRequiredFormControl(_showMe);

    return _showMe;
  }

  markRequiredFormControl(value: boolean) {
    if(!value) {
      if(this.json.required && this.json.dependency) {
        if(this.fGroup.controls.hasOwnProperty(this.json.id+'_ctrl')) {
          this.fGroup.removeControl(this.json.id+'_ctrl');
        }
      }
    } else {
      if(this.json.required && this.json.dependency) {
        if(!this.fGroup.controls.hasOwnProperty(this.json.id+'_ctrl')) {
          this.fGroup.addControl(this.json.id+'_ctrl', new FormControl('', Validators.required));
        }
      }
    }
  }

}
