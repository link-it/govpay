import { Injectable, OnDestroy, ComponentRef, ViewContainerRef, Injector } from '@angular/core';
import { ComponentFactoryResolver } from '@angular/core';
import { MediaMatcher } from "@angular/cdk/layout";
import { Router } from "@angular/router";
import { EntryPointList } from '../classes/entry-point-list';


@Injectable()
export class LinkService implements OnDestroy{

  routeHistory: Array<any> = [];

  constructor(private router: Router, private media: MediaMatcher, private cFR: ComponentFactoryResolver, private injRef: Injector) {
  }

  ngOnDestroy(): void {
  }

  /**
   * Large screen min limit media matcher
   */
  checkLargeMediaMatch(): MediaQueryList {
    return this.media.matchMedia('(min-width: 1440px)');
  }

  /**
   * Medium screen min limit media matcher
   */
  checkMediumMediaMatch(): MediaQueryList {
    //return this.media.matchMedia('(min-width: 576px)');
    return this.media.matchMedia('(min-width: 768px)');
  }

  /**
   * Small screen limit media matcher
   */
  checkSmallMediaMatch(): MediaQueryList {
    //return this.media.matchMedia('(max-width: 575px)');
    return this.media.matchMedia('(max-width: 767px)');
  }

  /**
   * Media matcher
   */
  checkMediaMatch(): MediaQueryList {
    return this.media.matchMedia('(max-width: 599px)');
  }

  resetRouteHistory() {
    this.routeHistory = [];
  }

  resetRouteReuseStrategy() {
    this.router.config.forEach((rc) => {
      (rc.data)?rc.data['reuse'] = false:null;
    });
  }

  navigateTo(path: any) {
    this.routeHistory.push([this.router.url]);
    this.router.navigate([path[0]]);
  }

  navigateToRoot() {
    this.routeHistory = [];
    this.router.navigate(['/']);
  }

  routeToLoginForm(url: string) {
    this.routeHistory = [];
    this.router.navigateByUrl(url);
  }

  navigateToMainList() {
    let _redirect = this.getRouterStateConfig();
    if(_redirect) {
      this.routeHistory = [];
      this.router.navigate([_redirect.path]);
    } else {
      this.navigateToRoot();
    }
  }

  navigateBack() {
    let _rsc = this.getRouterStateConfig();
    _rsc.data.info = null;
    _rsc.data.title = '';
    this.setRouterStateConfigData(_rsc.data);
    let _path = this.routeHistory.pop();
    this.router.navigate([_path[0]]);
  }

  /**
   * Get detail data by activated route|url
   * @param {string} _url
   * @returns {any} data
   */
  getRouterStateConfig(_url?: string): any {
    _url = _url || this.router.url;
    _url = _url.split('?')[0];
    let re = _url.substring(1).match(/^[^/]+(\/(\w+)){0,2}/); // ROUTING - {0,1} vs {0,2}
    let _redirect;
    let _parent;
    if(re && re[1]) {
      _parent = '/'+re[0].split(re[1]).join('');
      _redirect = this.router.config.filter((obj) => obj.path == re[0])[0];
    } else {
      let s = re?re[0]:null;
      _parent = re?'/'+re[0]:'/';
      _redirect = this.router.config.filter((obj) => obj.path == s)[0];
    }
    return (_redirect)?{ data: _redirect.data, path: _parent, fullPath: _url }:null;
  }

  setRouterStateConfigData(data: any,_url?: string) {
    _url = _url || this.router.url;
    let re = _url.split('?')[0].substring(1).match(/^[^/]+(\/(\w+)){0,2}/); // ROUTING - {0,1} vs {0,2}
    let s;
    if(re) {
      s = re[0];
    } else {
      s = re ? re.input : '';
    }
    this.router.config.map((obj) => {
      (obj.path == s)?obj.data = data:null;
    });
  }

  getRouterUrl(): string {
    return this.router.url;
  }

  createComponent(name: string, vcRef: ViewContainerRef): ComponentRef<any> {
    let componentType = EntryPointList.getComponentByName(name);
    let componentFact = this.cFR.resolveComponentFactory(componentType);
    let _viewRef = vcRef.createComponent(componentFact);

    return _viewRef;
  }

  componentRefByName(name: string): ComponentRef<any> {
    let componentType = EntryPointList.getComponentByName(name);
    return this.cFR.resolveComponentFactory(componentType).create(this.injRef);
  }

  resolveComponentType(_componentType: any): ComponentRef<any> {
    return this.cFR.resolveComponentFactory(_componentType).create(this.injRef);
  }

}
