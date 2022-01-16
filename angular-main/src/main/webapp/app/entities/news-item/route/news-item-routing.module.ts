import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { NewsItemComponent } from '../list/news-item.component';
import { NewsItemDetailComponent } from '../detail/news-item-detail.component';
import { NewsItemUpdateComponent } from '../update/news-item-update.component';
import { NewsItemRoutingResolveService } from './news-item-routing-resolve.service';

const newsItemRoute: Routes = [
  {
    path: '',
    component: NewsItemComponent,
    data: {
      defaultSort: 'id,asc',
    }
  },
  {
    path: ':id/view',
    component: NewsItemDetailComponent,
    resolve: {
      newsItem: NewsItemRoutingResolveService,
    }
  },
  {
    path: 'new',
    component: NewsItemUpdateComponent,
    resolve: {
      newsItem: NewsItemRoutingResolveService,
    }
  },
  {
    path: ':id/edit',
    component: NewsItemUpdateComponent,
    resolve: {
      newsItem: NewsItemRoutingResolveService,
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(newsItemRoute)],
  exports: [RouterModule],
})
export class NewsItemRoutingModule {}
