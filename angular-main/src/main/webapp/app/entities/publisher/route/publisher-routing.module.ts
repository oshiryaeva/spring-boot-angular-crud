import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { PublisherComponent } from '../list/publisher.component';
import { PublisherDetailComponent } from '../detail/publisher-detail.component';
import { PublisherUpdateComponent } from '../update/publisher-update.component';
import { PublisherRoutingResolveService } from './publisher-routing-resolve.service';

const publisherRoute: Routes = [
  {
    path: '',
    component: PublisherComponent,
    data: {
      defaultSort: 'id,asc',
    }
  },
  {
    path: ':id/view',
    component: PublisherDetailComponent,
    resolve: {
      publisher: PublisherRoutingResolveService,
    }
  },
  {
    path: 'new',
    component: PublisherUpdateComponent,
    resolve: {
      publisher: PublisherRoutingResolveService,
    }
  },
  {
    path: ':id/edit',
    component: PublisherUpdateComponent,
    resolve: {
      publisher: PublisherRoutingResolveService,
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(publisherRoute)],
  exports: [RouterModule],
})
export class PublisherRoutingModule {}
