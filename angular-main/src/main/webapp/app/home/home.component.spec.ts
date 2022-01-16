
jest.mock('@angular/router');

import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';
import { Router } from '@angular/router';

import { HomeComponent } from './home.component';

describe('Component Tests', () => {
  describe('Home Component', () => {
    let comp: HomeComponent;
    let fixture: ComponentFixture<HomeComponent>;
    let mockRouter: Router;
    

    beforeEach(
      waitForAsync(() => {
        TestBed.configureTestingModule({
          declarations: [HomeComponent],
          providers: [Router],
        })
          .overrideTemplate(HomeComponent, '')
          .compileComponents();
      })
    );

    beforeEach(() => {
      fixture = TestBed.createComponent(HomeComponent);
      comp = fixture.componentInstance;
      mockRouter = TestBed.inject(Router);
    });

    describe('toArtists', () => {
      it('Should navigate to /artist', () => {
        // WHEN
        comp.toArtists();

        // THEN
        expect(mockRouter.navigate).toHaveBeenCalledWith(['/artist']);
      });
    });

    describe('toItems', () => {
      it('Should navigate to /item', () => {
        // WHEN
        comp.toItems();

        // THEN
        expect(mockRouter.navigate).toHaveBeenCalledWith(['/item']);
      });
    });


    describe('toNewsItems', () => {
      it('Should navigate to /news-item', () => {
        // WHEN
        comp.toNewsItems();

        // THEN
        expect(mockRouter.navigate).toHaveBeenCalledWith(['/news-item']);
      });
    });

    describe('toPublishers', () => {
      it('Should navigate to /publisher', () => {
        // WHEN
        comp.toPublishers();

        // THEN
        expect(mockRouter.navigate).toHaveBeenCalledWith(['/publisher']);
      });
    });
  
  
    describe('toOrders', () => {
      it('Should navigate to /order', () => {
        // WHEN
        comp.toOrders();

        // THEN
        expect(mockRouter.navigate).toHaveBeenCalledWith(['/order']);
      });
    });

    describe('toCustomers', () => {
      it('Should navigate to /customer', () => {
        // WHEN
        comp.toCustomers();

        // THEN
        expect(mockRouter.navigate).toHaveBeenCalledWith(['/customer']);
      });
    });
  

  });
});
