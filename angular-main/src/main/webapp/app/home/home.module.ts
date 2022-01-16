import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { NbAlertModule, NbButtonModule, NbCardModule, NbCheckboxModule, NbIconModule, NbInputModule, NbSelectModule } from '@nebular/theme';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild([HOME_ROUTE]),
    NbCardModule,
    NbSelectModule,
    NbCheckboxModule,
    NbInputModule,
    NbButtonModule,
    NbAlertModule,
    NbIconModule,
  ],
  declarations: [HomeComponent],
})
export class HomeModule {}
