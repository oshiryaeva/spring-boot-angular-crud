import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { INewsItem, getNewsItemIdentifier } from '../news-item.model';

export type EntityResponseType = HttpResponse<INewsItem>;
export type EntityArrayResponseType = HttpResponse<INewsItem[]>;

@Injectable({ providedIn: 'root' })
export class NewsItemService {
  protected resourceUrl = "http://localhost:8080/news-items";

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(newsItem: INewsItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(newsItem);
    return this.http
      .post<INewsItem>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(newsItem: INewsItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(newsItem);
    return this.http
      .put<INewsItem>(`${this.resourceUrl}/${getNewsItemIdentifier(newsItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(newsItem: INewsItem): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(newsItem);
    return this.http
      .patch<INewsItem>(`${this.resourceUrl}/${getNewsItemIdentifier(newsItem) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<INewsItem>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<INewsItem[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addNewsItemToCollectionIfMissing(newsItemCollection: INewsItem[], ...newsItemsToCheck: (INewsItem | null | undefined)[]): INewsItem[] {
    const newsItems: INewsItem[] = newsItemsToCheck.filter(isPresent);
    if (newsItems.length > 0) {
      const newsItemCollectionIdentifiers = newsItemCollection.map(newsItemItem => getNewsItemIdentifier(newsItemItem)!);
      const newsItemsToAdd = newsItems.filter(newsItemItem => {
        const newsItemIdentifier = getNewsItemIdentifier(newsItemItem);
        if (newsItemIdentifier == null || newsItemCollectionIdentifiers.includes(newsItemIdentifier)) {
          return false;
        }
        newsItemCollectionIdentifiers.push(newsItemIdentifier);
        return true;
      });
      return [...newsItemsToAdd, ...newsItemCollection];
    }
    return newsItemCollection;
  }

  protected convertDateFromClient(newsItem: INewsItem): INewsItem {
    return Object.assign({}, newsItem, {
      date: newsItem.date?.isValid() ? newsItem.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((newsItem: INewsItem) => {
        newsItem.date = newsItem.date ? dayjs(newsItem.date) : undefined;
      });
    }
    return res;
  }
}
