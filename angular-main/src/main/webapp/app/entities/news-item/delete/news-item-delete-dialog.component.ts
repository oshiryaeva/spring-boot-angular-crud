import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { INewsItem } from '../news-item.model';
import { NewsItemService } from '../service/news-item.service';

@Component({
  templateUrl: './news-item-delete-dialog.component.html',
})
export class NewsItemDeleteDialogComponent {
  newsItem?: INewsItem;

  constructor(protected newsItemService: NewsItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.newsItemService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
