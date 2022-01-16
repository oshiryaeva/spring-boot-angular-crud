import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { INewsItem, NewsItem } from '../news-item.model';
import { NewsItemService } from '../service/news-item.service';

@Injectable({ providedIn: 'root' })
export class NewsItemRoutingResolveService implements Resolve<INewsItem> {
  constructor(protected service: NewsItemService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<INewsItem> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((newsItem: HttpResponse<NewsItem>) => {
          if (newsItem.body) {
            return of(newsItem.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new NewsItem());
  }
}
