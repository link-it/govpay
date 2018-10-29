import { Component, Input, OnInit, OnDestroy, AfterViewInit } from '@angular/core';
import { UtilService } from '../../services/util.service';
import { GovpayService } from '../../services/govpay.service';
import { NavigationEnd, Router } from '@angular/router';

const TIMER_LOG = false;

@Component({
  selector: 'link-badge-card',
  templateUrl: './badge-card.component.html',
  styleUrls: ['./badge-card.component.scss']
})
export class BadgeCardComponent implements OnInit, OnDestroy, AfterViewInit {

  @Input('badge-warning') _badgeWarning: boolean = false;
  @Input('badge-title') _badgeTitle: string = '';
  @Input('badge-subtitle') _badgeSubTitle: string = '';
  @Input('value') _badgeValue: string = '0';
  @Input('service') _badgeService: string;
  @Input('diff-service') _badgeDiffService: string;
  @Input('exam-service') _badgeExamService: string;
  @Input('badge-location') _location: string = '-no-timer-location-';

  protected _isLoading: boolean = false;

  protected _timerInitialized: boolean = false;
  protected GLOBAL_TIMER: string = 'GovPayActiveTimers';

  constructor(public router: Router, public gps: GovpayService, private us: UtilService) { }

  ngOnInit() {
    if(this._badgeService && this._badgeDiffService && this._badgeExamService) {
      this.forkBadge();
    }
  }

  ngAfterViewInit() {
    this._initTimer();
    this.router.events.filter(event => event instanceof NavigationEnd).subscribe((ne: NavigationEnd) => {
      let sub = ne.urlAfterRedirects.split('?')[0];
      this._clearTimer();
      if(sub.indexOf(this._location) != -1) {
        this._initTimer();
      }
    });
  }

  ngOnDestroy() {
    this._clearTimer();
  }

  protected _initTimer() {
    if(!this._timerInitialized) {
      this._timerInitialized = true;
      //Init del timer una volta
      //per ogni Badge-card creata.
      this._loadTimer();
    }
  }

  protected _loadTimer() {
    //Durata intervallo chiamate del timer
    let _timerInterval = UtilService.GET_BADGE_TIMER();
    if(_timerInterval > 0) {
      if(!window[this.GLOBAL_TIMER]) {
        window[ this.GLOBAL_TIMER ] = [];
      }
      //Impostazione funzione di refresh al trigger del timer
      let refresh = this.forkBadge.bind(this);
      window[this.GLOBAL_TIMER].push(setInterval(refresh, _timerInterval));
      if(TIMER_LOG){ console.log('Timer added.'); }
    }
  }

  protected _clearTimer() {
    if(window[this.GLOBAL_TIMER]) {
      window[this.GLOBAL_TIMER].forEach(t => {
        clearInterval(t);
        if(TIMER_LOG){ console.log('Servizio '+ t +' sospeso.'); }
      });
      window[this.GLOBAL_TIMER] = [];
    }
  }

  protected forkBadge() {
    if(TIMER_LOG){ console.log(window[this.GLOBAL_TIMER]?window[this.GLOBAL_TIMER]:'No timer yet.'); }
    this._isLoading = true;
    let methods: string[] = [];
    methods.push(this._badgeService);
    methods.push(this._badgeExamService);
    methods.push(this._badgeDiffService);

    this.gps.forkService(methods).subscribe(
      (response) => {
        try {
          let _json = response[0].body;  //results
          let _json1 = response[1].body; //exam results
          let _json2 = response[2].body; //plus

          this._badgeValue = parseInt(_json['numRisultati']).toString();
          this._badgeSubTitle = 'Esaminati: '+parseInt(_json1['numRisultati']);
          if(_json2) {
            let _diff = parseInt(_json2['numRisultati']);
            if(_diff > 0) {
              this._badgeValue = '+' + _diff;
              this._badgeSubTitle = 'Totali: '+parseInt(_json['numRisultati'])+', Esaminati: '+parseInt(_json1['numRisultati']);
              this._badgeWarning = true;
            }
          }
        } catch (e) {
          this._badgeValue = '0';
          this._badgeSubTitle = 'Esaminati: 0';
          this._clearTimer();
        }
        this._isLoading = false;
      },
      (error) => {
        this._isLoading = false;
        this._clearTimer();
        this.us.onError({ message: 'Le informazioni sui pagamenti non sono attualmente disponibili' });
      }
    );
  }

}
