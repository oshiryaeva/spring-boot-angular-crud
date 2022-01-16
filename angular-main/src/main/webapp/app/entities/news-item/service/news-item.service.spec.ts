import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { INewsItem, NewsItem } from '../news-item.model';

import { NewsItemService } from './news-item.service';

describe('NewsItem Service', () => {
  let service: NewsItemService;
  let httpMock: HttpTestingController;
  let elemDefault: INewsItem;
  let expectedResult: INewsItem | INewsItem[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(NewsItemService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      date: currentDate,
      title: 'AAAAAAA',
      description: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a NewsItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new NewsItem()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a NewsItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_FORMAT),
          title: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a NewsItem', () => {
      const patchObject = Object.assign(
        {
          title: 'BBBBBB',
          description: 'BBBBBB',
        },
        new NewsItem()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of NewsItem', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          date: currentDate.format(DATE_FORMAT),
          title: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a NewsItem', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addNewsItemToCollectionIfMissing', () => {
      it('should add a NewsItem to an empty array', () => {
        const newsItem: INewsItem = { id: 123 };
        expectedResult = service.addNewsItemToCollectionIfMissing([], newsItem);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(newsItem);
      });

      it('should not add a NewsItem to an array that contains it', () => {
        const newsItem: INewsItem = { id: 123 };
        const newsItemCollection: INewsItem[] = [
          {
            ...newsItem,
          },
          { id: 456 },
        ];
        expectedResult = service.addNewsItemToCollectionIfMissing(newsItemCollection, newsItem);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a NewsItem to an array that doesn't contain it", () => {
        const newsItem: INewsItem = { id: 123 };
        const newsItemCollection: INewsItem[] = [{ id: 456 }];
        expectedResult = service.addNewsItemToCollectionIfMissing(newsItemCollection, newsItem);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(newsItem);
      });

      it('should add only unique NewsItem to an array', () => {
        const newsItemArray: INewsItem[] = [{ id: 123 }, { id: 456 }, { id: 22715 }];
        const newsItemCollection: INewsItem[] = [{ id: 123 }];
        expectedResult = service.addNewsItemToCollectionIfMissing(newsItemCollection, ...newsItemArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const newsItem: INewsItem = { id: 123 };
        const newsItem2: INewsItem = { id: 456 };
        expectedResult = service.addNewsItemToCollectionIfMissing([], newsItem, newsItem2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(newsItem);
        expect(expectedResult).toContain(newsItem2);
      });

      it('should accept null and undefined values', () => {
        const newsItem: INewsItem = { id: 123 };
        expectedResult = service.addNewsItemToCollectionIfMissing([], null, newsItem, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(newsItem);
      });

      it('should return initial array if no NewsItem is added', () => {
        const newsItemCollection: INewsItem[] = [{ id: 123 }];
        expectedResult = service.addNewsItemToCollectionIfMissing(newsItemCollection, undefined, null);
        expect(expectedResult).toEqual(newsItemCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
