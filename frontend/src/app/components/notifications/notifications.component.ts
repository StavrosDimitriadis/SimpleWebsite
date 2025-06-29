import { Component, Injectable } from '@angular/core';
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

  constructor(private toastr: ToastrService) { }

  success(title: string, message: string) {
    this.toastr.success(message, title);
  }

  error(title: string, message: string) {
    this.toastr.error(message, title);
  }
}
