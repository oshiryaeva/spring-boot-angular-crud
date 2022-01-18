

import { waitForAsync, ComponentFixture, TestBed } from '@angular/core/testing';
import { Router, RouterEvent, NavigationEnd, NavigationStart } from '@angular/router';
import { Title } from '@angular/platform-browser';
import { Subject } from 'rxjs';


import { MainComponent } from './main.component';

describe('Component Tests', () => {
  describe('MainComponent', () => {
    let comp: MainComponent;
    let fixture: ComponentFixture<MainComponent>;
    let titleService: Title;
    const routerEventsSubject = new Subject<RouterEvent>();
    const routerState: any = { snapshot: { root: { data: {} } } };
    class MockRouter {
      events = routerEventsSubject;
      routerState = routerState;
    }

    beforeEach(
      waitForAsync(() => {
        TestBed.configureTestingModule({
          declarations: [MainComponent],
          providers: [
            Title,
            {
              provide: Router,
              useClass: MockRouter,
            },
          ],
        })
          .overrideTemplate(MainComponent, '')
          .compileComponents();
      })
    );

    beforeEach(() => {
      fixture = TestBed.createComponent(MainComponent);
      comp = fixture.componentInstance;
      titleService = TestBed.inject(Title);
    });

    describe('page title', () => {
      const defaultPageTitle = 'Olga Angular';
      const parentRoutePageTitle = 'parentTitle';
      const childRoutePageTitle = 'childTitle';
      const navigationEnd = new NavigationEnd(1, '', '');
      const navigationStart = new NavigationStart(1, '');

      beforeEach(() => {
        routerState.snapshot.root = { data: {} };
        jest.spyOn(titleService, 'setTitle');
        comp.ngOnInit();
      });

      describe('navigation end', () => {
        it('should set page title to default title if pageTitle is missing on routes', () => {
          // WHEN
          routerEventsSubject.next(navigationEnd);

          // THEN
          expect(titleService.setTitle).toHaveBeenCalledWith(defaultPageTitle);
        });

        it('should set page title to root route pageTitle if there is no child routes', () => {
          // GIVEN
          routerState.snapshot.root.data = { pageTitle: parentRoutePageTitle };

          // WHEN
          routerEventsSubject.next(navigationEnd);

          // THEN
          expect(titleService.setTitle).toHaveBeenCalledWith(parentRoutePageTitle);
        });

        it('should set page title to child route pageTitle if child routes exist and pageTitle is set for child route', () => {
          // GIVEN
          routerState.snapshot.root.data = { pageTitle: parentRoutePageTitle };
          routerState.snapshot.root.firstChild = { data: { pageTitle: childRoutePageTitle } };

          // WHEN
          routerEventsSubject.next(navigationEnd);

          // THEN
          expect(titleService.setTitle).toHaveBeenCalledWith(childRoutePageTitle);
        });

        it('should set page title to parent route pageTitle if child routes exists but pageTitle is not set for child route data', () => {
          // GIVEN
          routerState.snapshot.root.data = { pageTitle: parentRoutePageTitle };
          routerState.snapshot.root.firstChild = { data: {} };

          // WHEN
          routerEventsSubject.next(navigationEnd);

          // THEN
          expect(titleService.setTitle).toHaveBeenCalledWith(parentRoutePageTitle);
        });
      });

      describe('navigation start', () => {
        it('should not set page title on navigation start', () => {
          // WHEN
          routerEventsSubject.next(navigationStart);

          // THEN
          expect(titleService.setTitle).not.toHaveBeenCalled();
        });
      });
    });
  });
});
