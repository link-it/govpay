import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'link-iam-login-card',
  template: `
    <div class="{{ _btnClass }}">
      <a class="btn ext-button" [href]="_url" role="button">
        <span class="float-left" style="height: 21px"><!-- Login Ico-->
          <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 587.6 587.6" preserveAspectRatio="xMinYMin">
            <style>.loginIco0{fill:#FFF}.loginIco1{fill:#06C}</style>
            <path id="XMLID_3_" class="loginIco0" d="M587.6 293.8c0 162.3-131.5 293.8-293.8 293.8C131.6 587.6 0 456.1 0 293.8S131.6 0 293.8 0c162.3 0 293.8 131.5 293.8 293.8"/>
            <path id="XMLID_2_" class="loginIco1" d="M294.6 319c-24.4 0-44.5-8.2-60.3-24.8-15.8-16.5-23.7-37-23.7-61.4 0-24.5 7.9-44.8 23.6-61 15.7-16.2 35.7-24.3 60.2-24.3 24.4 0 44.3 8.2 59.6 24.9 15.3 16.6 23 37 23 61.5 0 24.3-7.7 44.6-23 60.8-15.3 16.1-35 24.3-59.4 24.3"/>
            <path id="XMLID_1_" class="loginIco1" d="M210.6 439.1c0-24.5 7.9-44.8 23.5-61 15.7-16.2 35.7-24.3 60.4-24.3 24.4 0 44.3 8.2 59.5 24.9 15.3 16.7 23 37.1 23 61.5"/>
          </svg>
        </span>
        <span class="float-left ml-3">{{_label}}</span>
      </a>
    </div>
  `,
  styles: [`
    :host {
      position: relative;
      display: block;
    }

    .ext-button,
    .ext-button:active {
      outline: none !important;
      box-shadow: none !important;
      background-color: #06C;
      border: 2px solid #06C;
      color: #FFF;
      padding: .5rem 1rem;
      font-size: .875rem;
      text-align: center;
      border-radius: 0;
    }

    .ext-button:hover {
      background-color: #036;
      border: 2px solid #036;
    }

    .ext-button svg {
      width: 21px;
      height: 212px;
    }
  `]
})
export class IamLoginCardComponent implements OnInit {

  @Input('label') _label: string = 'Accedi';
  @Input('url') _url: string = '#';
  @Input('button-theme') _btnClass: string = '';

  constructor() { }

  ngOnInit() {
  }

}
