import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Store } from '@ngrx/store';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Subscription } from 'rxjs';
import { AppState, UserData } from '../../shared/models/interfaces';
import { pastOrPresentDate } from '../../shared/validators/pastOrPresentDate.validator';
import { selectUserById } from '../../store/users-store/users.selectors';
import { deleteUser, updateUser } from '../../store/users-store/users.actions';

@Component({
  selector: 'app-info-modal',
  imports: [ReactiveFormsModule],
  templateUrl: './info-modal.component.html',
  styleUrl: './info-modal.component.css',
})
export class InfoModalComponent implements OnInit, OnDestroy {
  @Input() user!: UserData;

  public userForm!: FormGroup;
  public editMode: boolean = false;
  private subscription: Subscription = new Subscription();

  constructor(
    private fb: FormBuilder,
    private store: Store<AppState>,
    private modalService: NgbActiveModal
  ) { }

  ngOnInit() {
    this.userForm = this.fb.group({

      id: [this.user.id],
      name: [this.user.name, [Validators.required, Validators.maxLength(20)]],
      surname: [
        this.user.surname,
        [Validators.required, Validators.maxLength(20)],
      ],
      gender: [this.user.gender],
      birthdate: [this.user.birthdate, [Validators.required, pastOrPresentDate()]],
      workAddress: [this.user?.workAddress, Validators.maxLength(100)],
      homeAddress: [this.user?.homeAddress, Validators.maxLength(100)],
    });

    this.subscription.add(
      this.store.select(selectUserById(this.user.id)).subscribe((user) => {
        if (user) {
          this.user = user;
        }
      })
    );
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  public deleteUser(id?: number) {
    this.store.dispatch(deleteUser({ id }));
    this.modalService.close();
  }

  public updateUser() {
    this.editMode = !this.editMode;
    if (!this.editMode) {
      this.store.dispatch(updateUser({ payload: this.userForm.value }));
    }
  }
}
