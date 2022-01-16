import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IItem, Item } from '../item.model';
import { ItemService } from '../service/item.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IArtist } from 'app/entities/artist/artist.model';
import { ArtistService } from 'app/entities/artist/service/artist.service';
import { IPublisher } from 'app/entities/publisher/publisher.model';
import { PublisherService } from 'app/entities/publisher/service/publisher.service';
import { IImage } from 'app/entities/image/image.model';
import { ImageService } from 'app/entities/image/service/image.service';
import { Medium } from 'app/entities/enumerations/medium.model';

@Component({
  selector: 'wyrgorod-item-update',
  templateUrl: './item-update.component.html',
})
export class ItemUpdateComponent implements OnInit {
  isSaving = false;
  mediumValues = Object.keys(Medium);

  artistsSharedCollection: IArtist[] = [];
  publishersSharedCollection: IPublisher[] = [];
  imagesCollection: IImage[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    description: [],
    price: [],
    medium: [],
    artist: [],
    publisher: [],
    image: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected itemService: ItemService,
    protected artistService: ArtistService,
    protected publisherService: PublisherService,
    protected imageService: ImageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ item }) => {
      this.updateForm(item);

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
    const item = this.createFromForm();
    if (item.id !== undefined) {
      this.subscribeToSaveResponse(this.itemService.update(item));
    } else {
      this.subscribeToSaveResponse(this.itemService.create(item));
    }
  }

  trackArtistById(index: number, item: IArtist): number {
    return item.id!;
  }

  trackPublisherById(index: number, item: IPublisher): number {
    return item.id!;
  }

  trackImageById(index: number, item: IImage): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IItem>>): void {
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

  protected updateForm(item: IItem): void {
    this.editForm.patchValue({
      id: item.id,
      title: item.title,
      description: item.description,
      price: item.price,
      medium: item.medium,
      artist: item.artist,
      publisher: item.publisher,
      image: item.image,
    });

    this.artistsSharedCollection = this.artistService.addArtistToCollectionIfMissing(this.artistsSharedCollection, item.artist);
    this.publishersSharedCollection = this.publisherService.addPublisherToCollectionIfMissing(
      this.publishersSharedCollection,
      item.publisher
    );
    this.imagesCollection = this.imageService.addImageToCollectionIfMissing(this.imagesCollection, item.image);
  }

  protected loadRelationshipsOptions(): void {
    this.artistService
      .query()
      .pipe(map((res: HttpResponse<IArtist[]>) => res.body ?? []))
      .pipe(map((artists: IArtist[]) => this.artistService.addArtistToCollectionIfMissing(artists, this.editForm.get('artist')!.value)))
      .subscribe((artists: IArtist[]) => (this.artistsSharedCollection = artists));

    this.publisherService
      .query()
      .pipe(map((res: HttpResponse<IPublisher[]>) => res.body ?? []))
      .pipe(
        map((publishers: IPublisher[]) =>
          this.publisherService.addPublisherToCollectionIfMissing(publishers, this.editForm.get('publisher')!.value)
        )
      )
      .subscribe((publishers: IPublisher[]) => (this.publishersSharedCollection = publishers));

    this.imageService
      .query({ filter: 'item-is-null' })
      .pipe(map((res: HttpResponse<IImage[]>) => res.body ?? []))
      .pipe(map((images: IImage[]) => this.imageService.addImageToCollectionIfMissing(images, this.editForm.get('image')!.value)))
      .subscribe((images: IImage[]) => (this.imagesCollection = images));
  }

  protected createFromForm(): IItem {
    return {
      ...new Item(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      price: this.editForm.get(['price'])!.value,
      medium: this.editForm.get(['medium'])!.value,
      artist: this.editForm.get(['artist'])!.value,
      publisher: this.editForm.get(['publisher'])!.value,
      image: this.editForm.get(['image'])!.value,
    };
  }
}
