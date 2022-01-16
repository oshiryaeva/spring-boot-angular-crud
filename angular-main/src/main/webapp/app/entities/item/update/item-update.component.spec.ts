jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ItemService } from '../service/item.service';
import { IItem, Item } from '../item.model';
import { IArtist } from 'app/entities/artist/artist.model';
import { ArtistService } from 'app/entities/artist/service/artist.service';
import { IPublisher } from 'app/entities/publisher/publisher.model';
import { PublisherService } from 'app/entities/publisher/service/publisher.service';
import { IImage } from 'app/entities/image/image.model';
import { ImageService } from 'app/entities/image/service/image.service';

import { ItemUpdateComponent } from './item-update.component';

describe('Component Tests', () => {
  describe('Item Management Update Component', () => {
    let comp: ItemUpdateComponent;
    let fixture: ComponentFixture<ItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let itemService: ItemService;
    let artistService: ArtistService;
    let publisherService: PublisherService;
    let imageService: ImageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      itemService = TestBed.inject(ItemService);
      artistService = TestBed.inject(ArtistService);
      publisherService = TestBed.inject(PublisherService);
      imageService = TestBed.inject(ImageService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Artist query and add missing value', () => {
        const item: IItem = { id: 456 };
        const artist: IArtist = { id: 73640 };
        item.artist = artist;

        const artistCollection: IArtist[] = [{ id: 55932 }];
        jest.spyOn(artistService, 'query').mockReturnValue(of(new HttpResponse({ body: artistCollection })));
        const additionalArtists = [artist];
        const expectedCollection: IArtist[] = [...additionalArtists, ...artistCollection];
        jest.spyOn(artistService, 'addArtistToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ item });
        comp.ngOnInit();

        expect(artistService.query).toHaveBeenCalled();
        expect(artistService.addArtistToCollectionIfMissing).toHaveBeenCalledWith(artistCollection, ...additionalArtists);
        expect(comp.artistsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Publisher query and add missing value', () => {
        const item: IItem = { id: 456 };
        const publisher: IPublisher = { id: 16197 };
        item.publisher = publisher;

        const publisherCollection: IPublisher[] = [{ id: 5399 }];
        jest.spyOn(publisherService, 'query').mockReturnValue(of(new HttpResponse({ body: publisherCollection })));
        const additionalPublishers = [publisher];
        const expectedCollection: IPublisher[] = [...additionalPublishers, ...publisherCollection];
        jest.spyOn(publisherService, 'addPublisherToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ item });
        comp.ngOnInit();

        expect(publisherService.query).toHaveBeenCalled();
        expect(publisherService.addPublisherToCollectionIfMissing).toHaveBeenCalledWith(publisherCollection, ...additionalPublishers);
        expect(comp.publishersSharedCollection).toEqual(expectedCollection);
      });

      it('Should call image query and add missing value', () => {
        const item: IItem = { id: 456 };
        const image: IImage = { id: 15308 };
        item.image = image;

        const imageCollection: IImage[] = [{ id: 5347 }];
        jest.spyOn(imageService, 'query').mockReturnValue(of(new HttpResponse({ body: imageCollection })));
        const expectedCollection: IImage[] = [image, ...imageCollection];
        jest.spyOn(imageService, 'addImageToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ item });
        comp.ngOnInit();

        expect(imageService.query).toHaveBeenCalled();
        expect(imageService.addImageToCollectionIfMissing).toHaveBeenCalledWith(imageCollection, image);
        expect(comp.imagesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const item: IItem = { id: 456 };
        const artist: IArtist = { id: 49438 };
        item.artist = artist;
        const publisher: IPublisher = { id: 13365 };
        item.publisher = publisher;
        const image: IImage = { id: 6508 };
        item.image = image;

        activatedRoute.data = of({ item });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(item));
        expect(comp.artistsSharedCollection).toContain(artist);
        expect(comp.publishersSharedCollection).toContain(publisher);
        expect(comp.imagesCollection).toContain(image);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Item>>();
        const item = { id: 123 };
        jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ item });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: item }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(itemService.update).toHaveBeenCalledWith(item);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Item>>();
        const item = new Item();
        jest.spyOn(itemService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ item });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: item }));
        saveSubject.complete();

        // THEN
        expect(itemService.create).toHaveBeenCalledWith(item);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Item>>();
        const item = { id: 123 };
        jest.spyOn(itemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ item });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(itemService.update).toHaveBeenCalledWith(item);
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

      describe('trackPublisherById', () => {
        it('Should return tracked Publisher primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPublisherById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackImageById', () => {
        it('Should return tracked Image primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackImageById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
