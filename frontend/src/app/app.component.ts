import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { AppState } from './shared/models/interfaces';
import { spinnerState } from './store/users-store/users.selectors';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, AsyncPipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

export class AppComponent {
  title = 'frontend';
  spinner$: Observable<boolean>;

  constructor(private store: Store<AppState>) {
    this.spinner$ = this.store.select(spinnerState);
  }
}
