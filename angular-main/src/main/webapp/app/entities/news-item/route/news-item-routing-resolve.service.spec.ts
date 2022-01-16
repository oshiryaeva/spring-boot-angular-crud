jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { INewsItem, NewsItem } from '../news-item.model';
import { NewsItemService } from '../service/news-item.service';

import { NewsItemRoutingResolveService } from './news-item-routing-resolve.service';

describe('NewsItem routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: NewsItemRoutingResolveService;
  let service: NewsItemService;
  let resultNewsItem: INewsItem | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(NewsItemRoutingResolveService);
    service = TestBed.inject(NewsItemService);
    resultNewsItem = undefined;
  });

  describe('resolve', () => {
    it('should return INewsItem returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNewsItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNewsItem).toEqual({ id: 123 });
    });

    it('should return new INewsItem if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNewsItem = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultNewsItem).toEqual(new NewsItem());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as NewsItem })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultNewsItem = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultNewsItem).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
