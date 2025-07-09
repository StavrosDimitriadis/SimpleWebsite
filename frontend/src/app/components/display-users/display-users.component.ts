import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Store } from '@ngrx/store';
import { selectUsersState } from '../../store/users-store/users.selectors';
import { InfiniteScrollDirective } from 'ngx-infinite-scroll';
import { UsersState } from '../../store/users-store/users.reducers';
import { fetchFirstUsers, fetchUsers } from '../../store/users-store/users.actions';
import { CommonModule } from '@angular/common';
import { Subscription } from 'rxjs';
import { AppState, UserData } from '../../shared/models/interfaces';
import { InfoModalComponent } from '../info-modal/info-modal.component';
import { TranslateModule } from '@ngx-translate/core';

@Component({
  selector: 'app-display-users',
  imports: [CommonModule, InfiniteScrollDirective, TranslateModule],
  templateUrl: './display-users.component.html',
  styleUrl: './display-users.component.css'
})

export class DisplayUsersComponent implements OnInit, OnDestroy {

  public usersState: UsersState = {
    usersList: [],
    currentPage: 0,
    isLastPage: false,
    loadingRows: false,
    spinner: false
  };
  private subscription: Subscription = new Subscription();

  constructor(private store: Store<AppState>, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.subscription.add(
      this.store.select(selectUsersState).subscribe(
        (state) => {
          this.usersState = state;
          if (!this.usersState.isLastPage && this.usersState.usersList.length==0) {
            this.store.dispatch(fetchFirstUsers());
          }
        }
      )
    )
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  public openModal(user: UserData) {
    const modalRef = this.modalService.open(InfoModalComponent, { size: 'lg', centered: true, animation: true });
    modalRef.componentInstance.user = user;
  }

  public onScroll() {
    if (this.usersState.loadingRows || this.usersState.isLastPage) {
      return;
    }
    this.store.dispatch(fetchUsers({ page: this.usersState.currentPage + 1 }));
  }
}
