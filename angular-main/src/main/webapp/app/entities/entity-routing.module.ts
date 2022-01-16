import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'publisher',
        data: { pageTitle: 'Publishers' },
        loadChildren: () => import('./publisher/publisher.module').then(m => m.PublisherModule),
      },
      {
        path: 'order-item',
        data: { pageTitle: 'OrderItems' },
        loadChildren: () => import('./order-item/order-item.module').then(m => m.OrderItemModule),
      },
      {
        path: 'order',
        data: { pageTitle: 'Orders' },
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule),
      },
      {
        path: 'news-item',
        data: { pageTitle: 'NewsItems' },
        loadChildren: () => import('./news-item/news-item.module').then(m => m.NewsItemModule),
      },
      {
        path: 'item',
        data: { pageTitle: 'Items' },
        loadChildren: () => import('./item/item.module').then(m => m.ItemModule),
      },
      {
        path: 'image',
        data: { pageTitle: 'Images' },
        loadChildren: () => import('./image/image.module').then(m => m.ImageModule),
      },
      {
        path: 'customer',
        data: { pageTitle: 'Customers' },
        loadChildren: () => import('./customer/customer.module').then(m => m.CustomerModule),
      },
      {
        path: 'artist',
        data: { pageTitle: 'Artists' },
        loadChildren: () => import('./artist/artist.module').then(m => m.ArtistModule),
      }
    ]),
  ],
})
export class EntityRoutingModule {}
