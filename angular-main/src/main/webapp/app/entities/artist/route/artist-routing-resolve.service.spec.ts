jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IArtist, Artist } from '../artist.model';
import { ArtistService } from '../service/artist.service';

import { ArtistRoutingResolveService } from './artist-routing-resolve.service';

describe('Artist routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ArtistRoutingResolveService;
  let service: ArtistService;
  let resultArtist: IArtist | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ArtistRoutingResolveService);
    service = TestBed.inject(ArtistService);
    resultArtist = undefined;
  });

  describe('resolve', () => {
    it('should return IArtist returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultArtist = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultArtist).toEqual({ id: 123 });
    });

    it('should return new IArtist if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultArtist = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultArtist).toEqual(new Artist());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Artist })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultArtist = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultArtist).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
