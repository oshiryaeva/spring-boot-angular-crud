import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { OrderItemComponent } from '../list/order-item.component';
import { OrderItemDetailComponent } from '../detail/order-item-detail.component';
import { OrderItemUpdateComponent } from '../update/order-item-update.component';
import { OrderItemRoutingResolveService } from './order-item-routing-resolve.service';

const orderItemRoute: Routes = [
  {
    path: '',
    component: OrderItemComponent,
    data: {
      defaultSort: 'id,asc',
    }
  },
  {
    path: ':id/view',
    component: OrderItemDetailComponent,
    resolve: {
      orderItem: OrderItemRoutingResolveService,
    }
  },
  {
    path: 'new',
    component: OrderItemUpdateComponent,
    resolve: {
      orderItem: OrderItemRoutingResolveService,
    }
  },
  {
    path: ':id/edit',
    component: OrderItemUpdateComponent,
    resolve: {
      orderItem: OrderItemRoutingResolveService,
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(orderItemRoute)],
  exports: [RouterModule],
})
export class OrderItemRoutingModule {}
