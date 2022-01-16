import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ImageComponent } from '../list/image.component';
import { ImageDetailComponent } from '../detail/image-detail.component';
import { ImageUpdateComponent } from '../update/image-update.component';
import { ImageRoutingResolveService } from './image-routing-resolve.service';

const imageRoute: Routes = [
  {
    path: '',
    component: ImageComponent,
    data: {
      defaultSort: 'id,asc',
    }
  },
  {
    path: ':id/view',
    component: ImageDetailComponent,
    resolve: {
      image: ImageRoutingResolveService,
    }
  },
  {
    path: 'new',
    component: ImageUpdateComponent,
    resolve: {
      image: ImageRoutingResolveService,
    }
  },
  {
    path: ':id/edit',
    component: ImageUpdateComponent,
    resolve: {
      image: ImageRoutingResolveService,
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(imageRoute)],
  exports: [RouterModule],
})
export class ImageRoutingModule {}
