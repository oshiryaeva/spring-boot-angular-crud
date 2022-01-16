jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { NewsItemService } from '../service/news-item.service';
import { INewsItem, NewsItem } from '../news-item.model';
import { IArtist } from 'app/entities/artist/artist.model';
import { ArtistService } from 'app/entities/artist/service/artist.service';

import { NewsItemUpdateComponent } from './news-item-update.component';

describe('Component Tests', () => {
  describe('NewsItem Management Update Component', () => {
    let comp: NewsItemUpdateComponent;
    let fixture: ComponentFixture<NewsItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let newsItemService: NewsItemService;
    let artistService: ArtistService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [NewsItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(NewsItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(NewsItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      newsItemService = TestBed.inject(NewsItemService);
      artistService = TestBed.inject(ArtistService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Artist query and add missing value', () => {
        const newsItem: INewsItem = { id: 456 };
        const artist: IArtist = { id: 98147 };
        newsItem.artist = artist;

        const artistCollection: IArtist[] = [{ id: 95613 }];
        jest.spyOn(artistService, 'query').mockReturnValue(of(new HttpResponse({ body: artistCollection })));
        const additionalArtists = [artist];
        const expectedCollection: IArtist[] = [...additionalArtists, ...artistCollection];
        jest.spyOn(artistService, 'addArtistToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ newsItem });
        comp.ngOnInit();

        expect(artistService.query).toHaveBeenCalled();
        expect(artistService.addArtistToCollectionIfMissing).toHaveBeenCalledWith(artistCollection, ...additionalArtists);
        expect(comp.artistsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const newsItem: INewsItem = { id: 456 };
        const artist: IArtist = { id: 95320 };
        newsItem.artist = artist;

        activatedRoute.data = of({ newsItem });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(newsItem));
        expect(comp.artistsSharedCollection).toContain(artist);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<NewsItem>>();
        const newsItem = { id: 123 };
        jest.spyOn(newsItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ newsItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: newsItem }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(newsItemService.update).toHaveBeenCalledWith(newsItem);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<NewsItem>>();
        const newsItem = new NewsItem();
        jest.spyOn(newsItemService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ newsItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: newsItem }));
        saveSubject.complete();

        // THEN
        expect(newsItemService.create).toHaveBeenCalledWith(newsItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<NewsItem>>();
        const newsItem = { id: 123 };
        jest.spyOn(newsItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ newsItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(newsItemService.update).toHaveBeenCalledWith(newsItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackArtistById', () => {
        it('Should return tracked Artist primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackArtistById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
