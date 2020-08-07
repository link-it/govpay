import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'link-progress',
  templateUrl: './progress.component.html',
  styleUrls: ['./progress.component.scss']
})
export class ProgressComponent implements OnInit {

  @Input('progress') _value: number = 0;
  @Input('buffer') _buffer: number = 0;
  @Input('mode') _mode: string = 'determinate'; // 'indeterminate/determinate'
  @Input('label') _label: string = '';

  constructor() { }

  ngOnInit() {
  }

}
