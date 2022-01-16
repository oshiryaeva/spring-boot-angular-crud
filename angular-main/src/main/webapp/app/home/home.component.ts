import { Component, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';


@Component({
  selector: 'wyrgorod-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnDestroy {

  private readonly destroy$ = new Subject<void>();

  constructor(private router: Router) {}





  toArtists(): void {
    this.router.navigate(['/artist']);
  }

  toItems(): void {
    this.router.navigate(['/item']);
  }

  toNewsItems(): void {
    this.router.navigate(['/news-item']);
  }

  toPublishers(): void {
    this.router.navigate(['/publisher']);
  }

  toOrders(): void {
    this.router.navigate(['/order']);
  }

  toCustomers(): void {
    this.router.navigate(['/customer']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}
