import { Component, Input, EventEmitter, Output, OnInit, OnChanges, SimpleChanges, AfterViewInit, OnDestroy } from '@angular/core';

import { UtilService } from '../../services/util.service';

import * as Survey from 'survey-angular';
import * as widgets from 'surveyjs-widgets';

import { init as initCustomWidget } from './customwidget';

import * as $ from 'jquery';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'survey',
  template: `<div id="survey-component" class="survey-container contentcontainer codecontainer">
    <div id="surveyElement"></div>
  </div>`
})
export class SurveyComponent implements OnInit, AfterViewInit, OnChanges, OnDestroy {
  @Input() lang: string = 'it';
  @Input() json: object;
  @Input() data: any = null;
  @Input() edit: boolean = false;
  @Input() theme: string = 'survey'; // survey - defaultV2 - modern - bootstrapmaterial

  @Output() submitSurvey = new EventEmitter<any>();

  result: any;

  surveyModel;

  _isBootstrapMaterial = false;

  constructor() {
  }

  ngOnInit() {
    this.initSurvey();
  }

  initSurvey() {
    // if (this._isBootstrapMaterial) {
    //   const dom: any = window || {};
    //   dom.jQuery = $;
    //   $.noConflict();
    //   require('bootstrap-material-design');
    // }

    widgets.icheck(Survey);
    widgets.select2(Survey);
    widgets.inputmask(Survey);
    widgets.jquerybarrating(Survey);
    widgets.jqueryuidatepicker(Survey);
    widgets.nouislider(Survey);
    widgets.select2tagbox(Survey);
    // widgets.signaturepad(Survey);
    widgets.sortablejs(Survey);
    // widgets.ckeditor(Survey);
    widgets.autocomplete(Survey);
    widgets.bootstrapslider(Survey);
    widgets.prettycheckbox(Survey);
    // widgets.emotionsratings(Survey);
    initCustomWidget(Survey);

    Survey.Serializer.addProperty('questionbase', 'popupdescription:text');
    Survey.Serializer.addProperty('page', 'popupdescription:text');

    Survey.StylesManager.applyTheme(this.theme);
    Survey.defaultBootstrapCss.navigationButton = 'btn btn-primary no-border-radius';

    this.surveyModel = new Survey.Model(this.json);

    // this.surveyModel.setDesignMode(false);
    this.surveyModel.locale = this.lang;
    // this.surveyModel.questionTitleLocation= 'hidden';
    // this.surveyModel.logoPosition = 'right';
    this.surveyModel.focusFirstQuestionAutomatic = false;
    // const compleText = this.Pay.EditMode ? this.Pay.I18n.json.DettaglioServizio.Form.Salva : this.Pay.I18n.json.DettaglioServizio.Dialog.Submit;
    this.surveyModel.completeText = 'Salva';
    // this.surveyModel.previewText = 'Anteprima';
    // this.surveyModel.editText = 'Modifica';
    this.surveyModel.startSurveyText = 'Inizia';
    this.surveyModel.completedHtml = '<div class="px-5 py-5 text-center"><h5>Salvataggio …</h5></div>';
    this.surveyModel.showCompletedPage = true;
    this.surveyModel.questionDescriptionLocation = 'underInput';
    this.surveyModel.questionErrorLocation = 'bottom';
    this.surveyModel.clearInvisibleValues = 'none';
    // this.surveyModel.showPreviewBeforeComplete = 'showAnsweredQuestions';
    this.surveyModel.checkErrorsMode = 'onValueChanging';
    this.surveyModel.textUpdateMode = 'onTyping';
    this.surveyModel.autoGrowComment = true;
    this.surveyModel.questionStartIndex = '1';
    this.surveyModel.widthMode = 'responsive';

    // if (this._isBootstrapMaterial) {
    //   this.surveyModel.onAfterRenderQuestionInput.add((sender, options) => {
    //     const el: any = options.htmlElement;
    //     const question: any = options.question;
    //     if (question.getType() === 'text') {
    //       question.titleLocation = 'hidden';
    //       const label: any = document.createElement('label');
    //       label.classList.add('bmd-label-floating');
    //       label.innerHTML = question.locTitle.textOrHtml;
    //       el.parentNode.insertBefore(label, el);
    //     }
    //   });
    // }

    this.surveyModel.onComplete.add((sender, options) => {
      this.submitSurvey.emit(sender.data);
      this.result = sender.data;
      sender.clear(false);
      this._setBmdFilled();
      // this._initBoostrapMaterial();
      // sender.mode = 'display';
    });

    if (this.data) { this.surveyModel.data = this.data; }

  const myCss = {
    file: {
      root: 'sd-file',
      other: 'sd-input sd-comment',
      placeholderInput: 'sd-visuallyhidden',
      preview: 'sd-file__preview',
      fileSign: '',
      fileList: 'sd-file__list',
      fileSignBottom: 'sd-file__sign',
      fileDecorator: 'sd-file__decorator',
      onError: 'sd-file__decorator--error',
      fileDecoratorDrag: 'sd-file__decorator--drag',
      fileInput: 'sd-hidden',
      noFileChosen: 'sd-description sd-file__no-file-chosen',
      chooseFile: 'sd-file__choose-btn',
      chooseFileAsText: 'sd-action sd-file__choose-btn--text',
      chooseFileAsTextDisabled: 'sd-action--disabled',
      chooseFileAsIcon: 'sd-context-btn sd-file__choose-btn--icon',
      chooseFileIconId: 'icon-choosefile',
      disabled: 'sd-file__choose-btn--disabled',
      removeButton: '',
      removeButtonBottom: 'sd-hidden', // 'sd-context-btn sd-context-btn--negative sd-file__btn sd-file__clean-btn',
      removeButtonIconId: 'icon-clear',
      removeFile: 'sd-hidden',
      removeFileSvg: '',
      removeFileSvgIconId: 'icon-delete',
      wrapper: 'sd-file__wrapper',
      defaultImage: 'sd-file__default-image',
      defaultImageIconId: 'icon-defaultfile',
      leftIconId: 'icon-arrowleft',
      rightIconId: 'icon-arrowright',
      removeFileButton: 'sd-context-btn sd-context-btn--negative sd-file__remove-file-button',
      dragAreaPlaceholder: 'sd-hidden', // 'sd-file__drag-area-placeholder',
      imageWrapper: 'sd-file__image-wrapper',
      single: 'sd-file--single',
      singleImage: 'sd-file--single-image',
      mobile: 'sd-file--mobile',
    }
  };

    Survey.SurveyNG.render('surveyElement', {
      model: this.surveyModel,
      css: myCss
    });
  }

  ngAfterViewInit() {
    // this._initBoostrapMaterial();
  }

  _initBoostrapMaterial() {
    setTimeout(() => {
      if (this._isBootstrapMaterial) {
        if (typeof $('#surveyElement').bootstrapMaterialDesign === 'function') {
          $('#surveyElement').bootstrapMaterialDesign();
        } else {
          console.log('bootstrapMaterialDesign non esiste');
        }
      }
    });
  }

  _setBmdFilled() {
    setTimeout(() => {
      $('.bmd-form-group').addClass('is-filled');
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.lang) {
      this.lang = changes.lang.currentValue;
      if (this.surveyModel && this.lang) {
        this.surveyModel.locale = this.lang;
        this.surveyModel.clear(true, true);
        this.surveyModel.render();
        // this._initBoostrapMaterial();
      }
    }
    if (changes.edit) {
      this.edit = changes.edit.currentValue;
      if (this.surveyModel && this.edit) {
        this.surveyModel.clear(false, true);
        this.surveyModel.render();
        // this._initBoostrapMaterial();
        this._setBmdFilled();
      }
    }
    if (changes.theme) {
      this.theme = changes.theme.currentValue;
      this._isBootstrapMaterial = (this.theme === 'bootstrapmaterial');
    }
  }

  ngOnDestroy(): void {
    // remove element
    // document.getElementById('survey-content-angular').remove();
  }
}
