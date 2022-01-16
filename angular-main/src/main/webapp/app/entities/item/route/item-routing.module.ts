import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ItemComponent } from '../list/item.component';
import { ItemDetailComponent } from '../detail/item-detail.component';
import { ItemUpdateComponent } from '../update/item-update.component';
import { ItemRoutingResolveService } from './item-routing-resolve.service';

const itemRoute: Routes = [
  {
    path: '',
    component: ItemComponent,
    data: {
      defaultSort: 'id,asc',
    }
  },
  {
    path: ':id/view',
    component: ItemDetailComponent,
    resolve: {
      item: ItemRoutingResolveService,
    }
  },
  {
    path: 'new',
    component: ItemUpdateComponent,
    resolve: {
      item: ItemRoutingResolveService,
    }
  },
  {
    path: ':id/edit',
    component: ItemUpdateComponent,
    resolve: {
      item: ItemRoutingResolveService,
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(itemRoute)],
  exports: [RouterModule],
})
export class ItemRoutingModule {}
