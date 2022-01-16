import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { NewsItemComponent } from './list/news-item.component';
import { NewsItemDetailComponent } from './detail/news-item-detail.component';
import { NewsItemUpdateComponent } from './update/news-item-update.component';
import { NewsItemDeleteDialogComponent } from './delete/news-item-delete-dialog.component';
import { NewsItemRoutingModule } from './route/news-item-routing.module';

@NgModule({
  imports: [SharedModule, NewsItemRoutingModule],
  declarations: [NewsItemComponent, NewsItemDetailComponent, NewsItemUpdateComponent, NewsItemDeleteDialogComponent],
  entryComponents: [NewsItemDeleteDialogComponent],
})
export class NewsItemModule {}
