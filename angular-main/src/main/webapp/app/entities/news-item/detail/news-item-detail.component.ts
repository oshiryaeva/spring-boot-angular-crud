import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { INewsItem } from '../news-item.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'wyrgorod-news-item-detail',
  templateUrl: './news-item-detail.component.html',
})
export class NewsItemDetailComponent implements OnInit {
  newsItem: INewsItem | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ newsItem }) => {
      this.newsItem = newsItem;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
