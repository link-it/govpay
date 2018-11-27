import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'link-progress',
  templateUrl: './progress.component.html',
  styleUrls: ['./progress.component.scss']
})
export class ProgressComponent implements OnInit {

  @Input() value: number = 0;

  constructor() { }

  ngOnInit() {
  }

}
