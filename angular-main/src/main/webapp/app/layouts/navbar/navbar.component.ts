import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { VERSION } from 'app/app.constants';
import { ProfileService } from 'app/layouts/profiles/profile.service';
import { LayoutService } from '../layout.service';
import { NbSidebarService } from '@nebular/theme';

@Component({
  selector: 'wyrgorod-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent implements OnInit {
  inProduction?: boolean;
  isNavbarCollapsed = true;
  openAPIEnabled?: boolean;
  version = '';

  constructor(
    private profileService: ProfileService,
    private router: Router,
    private layoutService: LayoutService,
    private sidebarService: NbSidebarService
  ) {
    if (VERSION) {
      this.version = VERSION.toLowerCase().startsWith('v') ? VERSION : `v${VERSION}`;
    }
  }

  ngOnInit(): void {
    this.profileService.getProfileInfo().subscribe(profileInfo => {
      this.inProduction = profileInfo.inProduction;
      this.openAPIEnabled = profileInfo.openAPIEnabled;
    });
  }

  collapseNavbar(): void {
    this.isNavbarCollapsed = true;
  }


  toggleNavbar(): void {
    this.isNavbarCollapsed = !this.isNavbarCollapsed;
  }
  toggleSidebar(): boolean {
    this.sidebarService.toggle(false, 'menu-sidebar');
    this.layoutService.changeLayoutSize();
    return false;
  }
}
