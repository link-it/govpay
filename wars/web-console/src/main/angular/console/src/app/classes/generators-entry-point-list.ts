import { ComponentRef, Type } from '@angular/core';

import { UtilService } from '../services/util.service';

import { JsonSchemaFormComponent } from 'angular2-json-schema-form';
import { SurveyComponent } from '../elements/survey/survey.component';
import { StandardViewComponent } from '../elements/item-view/views/standard-view/standard-view.component';

export class GeneratorsEntryPointList {

  public static entryList: Array<any> = [
    JsonSchemaFormComponent,
    SurveyComponent
  ];

  public static getComponentByName(name: string):Type<any> {
    let _type = null;

    switch (name) {
      //Json form generator
      case UtilService.A2_JSON_SCHEMA_FORM:
        _type = JsonSchemaFormComponent;
        break;
      case UtilService.SURVEYJS_FORM:
        _type = SurveyComponent;
        break;
      //Default Item view ref
      default:
        _type = StandardViewComponent;
    }

    return _type;
  }

  private static getComponentName(componentRef: ComponentRef<any>): string {
    let _name: string = null;
    switch (componentRef.componentType.name) {
      //Json form generator
      case 'JsonSchemaFormComponent':
        _name = UtilService.A2_JSON_SCHEMA_FORM;
        break;
      case 'SurveyComponent':
        _name = UtilService.SURVEYJS_FORM;
        break;
      //Default Item view ref
      default:
        _name = '';
    }

    return _name;
  }

}

export const GeneratorsEntryListComponents = GeneratorsEntryPointList.entryList;
