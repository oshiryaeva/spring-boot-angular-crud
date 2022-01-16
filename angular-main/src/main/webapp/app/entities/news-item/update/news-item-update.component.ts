import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { INewsItem, NewsItem } from '../news-item.model';
import { NewsItemService } from '../service/news-item.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IArtist } from 'app/entities/artist/artist.model';
import { ArtistService } from 'app/entities/artist/service/artist.service';

@Component({
  selector: 'wyrgorod-news-item-update',
  templateUrl: './news-item-update.component.html',
})
export class NewsItemUpdateComponent implements OnInit {
  isSaving = false;

  artistsSharedCollection: IArtist[] = [];

  editForm = this.fb.group({
    id: [],
    date: [],
    title: [],
    description: [],
    artist: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected newsItemService: NewsItemService,
    protected artistService: ArtistService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ newsItem }) => {
      this.updateForm(newsItem);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('wyrgorodAngularApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const newsItem = this.createFromForm();
    if (newsItem.id !== undefined) {
      this.subscribeToSaveResponse(this.newsItemService.update(newsItem));
    } else {
      this.subscribeToSaveResponse(this.newsItemService.create(newsItem));
    }
  }

  trackArtistById(index: number, item: IArtist): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INewsItem>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(newsItem: INewsItem): void {
    this.editForm.patchValue({
      id: newsItem.id,
      date: newsItem.date,
      title: newsItem.title,
      description: newsItem.description,
      artist: newsItem.artist,
    });

    this.artistsSharedCollection = this.artistService.addArtistToCollectionIfMissing(this.artistsSharedCollection, newsItem.artist);
  }

  protected loadRelationshipsOptions(): void {
    this.artistService
      .query()
      .pipe(map((res: HttpResponse<IArtist[]>) => res.body ?? []))
      .pipe(map((artists: IArtist[]) => this.artistService.addArtistToCollectionIfMissing(artists, this.editForm.get('artist')!.value)))
      .subscribe((artists: IArtist[]) => (this.artistsSharedCollection = artists));
  }

  protected createFromForm(): INewsItem {
    return {
      ...new NewsItem(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      artist: this.editForm.get(['artist'])!.value,
    };
  }
}
