import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ArtistComponent } from '../list/artist.component';
import { ArtistDetailComponent } from '../detail/artist-detail.component';
import { ArtistUpdateComponent } from '../update/artist-update.component';
import { ArtistRoutingResolveService } from './artist-routing-resolve.service';

const artistRoute: Routes = [
  {
    path: '',
    component: ArtistComponent,
    data: {
      defaultSort: 'id,asc',
    }
  },
  {
    path: ':id/view',
    component: ArtistDetailComponent,
    resolve: {
      artist: ArtistRoutingResolveService,
    }
  },
  {
    path: 'new',
    component: ArtistUpdateComponent,
    resolve: {
      artist: ArtistRoutingResolveService,
    }
  },
  {
    path: ':id/edit',
    component: ArtistUpdateComponent,
    resolve: {
      artist: ArtistRoutingResolveService,
    }
  },
];

@NgModule({
  imports: [RouterModule.forChild(artistRoute)],
  exports: [RouterModule],
})
export class ArtistRoutingModule {}
