import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { CustomerComponent } from '../list/customer.component';
import { CustomerDetailComponent } from '../detail/customer-detail.component';
import { CustomerUpdateComponent } from '../update/customer-update.component';
import { CustomerRoutingResolveService } from './customer-routing-resolve.service';

const customerRoute: Routes = [
  {
    path: '',
    component: CustomerComponent,
    data: {
      defaultSort: 'id,asc',
    }
  },
  {
    path: ':id/view',
    component: CustomerDetailComponent,
    resolve: {
      customer: CustomerRoutingResolveService,
    }
  },
  {
    path: 'new',
    component: CustomerUpdateComponent,
    resolve: {
      customer: CustomerRoutingResolveService,
    }
  },
  {
    path: ':id/edit',
    component: CustomerUpdateComponent,
    resolve: {
      customer: CustomerRoutingResolveService,
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(customerRoute)],
  exports: [RouterModule],
})
export class CustomerRoutingModule {}
