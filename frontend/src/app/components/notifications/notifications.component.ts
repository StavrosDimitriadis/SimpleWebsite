import { Component, Injectable } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-notifications',
  imports: [],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css'
})

@Injectable({
  providedIn: 'root'
})

export class NotificationsComponent {

  constructor(private toastr: ToastrService, private translateService: TranslateService) { }

  success(title: string, message: string) {
    const translatedTitle = this.translateService.instant(title);
    const translatedMessage = this.translateService.instant(message);
    this.toastr.success(translatedMessage, translatedTitle);
  }

  error(title: string, message: string) {
    const translatedTitle = this.translateService.instant(title);
    const translatedMessage = this.translateService.instant(message);
    this.toastr.error(translatedMessage, translatedTitle);
  }
}
