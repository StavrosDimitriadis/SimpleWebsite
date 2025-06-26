import { Component } from '@angular/core';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Store } from '@ngrx/store';
import { storeUser } from '../users-store/users.actions';
import { pastOrPresentDate } from '../custom.validators';

@Component({
  selector: 'app-register-user',
  templateUrl: './register-user.component.html',
  styleUrls: ['./register-user.component.css'],
  imports: [ReactiveFormsModule]
})

export class RegisterUserComponent {

  public registerForm: FormGroup;

  constructor(private fb: FormBuilder, private store: Store) {

    this.registerForm = this.fb.group({

      name: ['', [Validators.required, Validators.maxLength(20)]],
      surname: ['', [Validators.required, Validators.maxLength(20)]],
      gender: ['', Validators.required],
      birthdate: ['', [Validators.required, pastOrPresentDate()]],
      workAddress: [null, Validators.maxLength(100)],
      homeAddress: [null, Validators.maxLength(100)]

    });
  }

  public onSubmit(): void {
    this.store.dispatch(storeUser({ payload: this.registerForm.value }));
  }
}