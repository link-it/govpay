import { RouteReuseStrategy, DetachedRouteHandle } from '@angular/router/src/route_reuse_strategy';
import { ActivatedRouteSnapshot } from '@angular/router/src/router_state';

export class ListReuseStrategy  implements RouteReuseStrategy {

  handlers: { [ key: string ]: DetachedRouteHandle } = {};
  subscribers: { [ key: string ]: boolean } = {};

  shouldDetach(route: ActivatedRouteSnapshot): boolean {
    return (route.routeConfig.path.indexOf('/dettaglio') == -1);
  }

  store(route: ActivatedRouteSnapshot, handle: DetachedRouteHandle): void {
    this.handlers[route.routeConfig.path] = handle;

    if(!this.subscribers[route.routeConfig.path]) {
      this.subscribers[route.routeConfig.path] = true;
      route.routeConfig.data['reuse'] = true;
    }
  }

  shouldAttach(route: ActivatedRouteSnapshot): boolean {
    if(!!route.routeConfig && !route.routeConfig.data.reuse) {
      delete this.handlers[route.routeConfig.path];
      delete this.subscribers[route.routeConfig.path];
    }
    return !!route.routeConfig && !!this.handlers[route.routeConfig.path];
  }

  retrieve(route: ActivatedRouteSnapshot): DetachedRouteHandle {
    if (!route.routeConfig) return null;

    return this.handlers[route.routeConfig.path];
  }

  shouldReuseRoute(future: ActivatedRouteSnapshot, curr: ActivatedRouteSnapshot): boolean {
    return future.routeConfig === curr.routeConfig;
  }

}
