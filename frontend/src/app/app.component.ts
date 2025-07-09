import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { Store } from '@ngrx/store';
import { Observable } from 'rxjs';
import { AsyncPipe } from '@angular/common';
import { AppState, LANGUAGES } from './shared/models/interfaces';
import { spinnerState } from './store/users-store/users.selectors';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive, AsyncPipe, FormsModule, TranslateModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})

export class AppComponent {
  public title: string = 'frontend';
  public spinner$: Observable<boolean>;
  public selectedLang: string = LANGUAGES.English;

  constructor(private store: Store<AppState>, private translate: TranslateService) {
    translate.addLangs([LANGUAGES.English, LANGUAGES.Greek]);
    translate.setDefaultLang(LANGUAGES.English);
    translate.use(LANGUAGES.English);

    this.spinner$ = this.store.select(spinnerState);
  }

  public switchLang() {
    this.translate.use(this.selectedLang);
  }
}
