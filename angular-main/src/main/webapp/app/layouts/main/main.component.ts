import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { Router, ActivatedRouteSnapshot, NavigationEnd } from '@angular/router';
import { NbMenuItem } from '@nebular/theme';

import { USER_MENU } from './menu-item';

@Component({
  selector: 'wyrgorod-main',
  templateUrl: './main.component.html',
})
export class MainComponent implements OnInit {
  public userItems: NbMenuItem[] = USER_MENU;
  constructor(private titleService: Title, private router: Router) {}

  ngOnInit(): void {

    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.updateTitle();
      }
    });
  }

  private getPageTitle(routeSnapshot: ActivatedRouteSnapshot): string {
    const title: string = routeSnapshot.data['pageTitle'] ?? '';
    if (routeSnapshot.firstChild) {
      return this.getPageTitle(routeSnapshot.firstChild) || title;
    }
    return title;
  }

  private updateTitle(): void {
    let pageTitle = this.getPageTitle(this.router.routerState.snapshot.root);
    if (!pageTitle) {
      pageTitle = 'Olga Angular';
    }
    this.titleService.setTitle(pageTitle);
  }
}
