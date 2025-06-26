import { HomeComponent } from './home/home.component';
import { DisplayUsersComponent } from './display-users/display-users.component';
import { RegisterUserComponent } from './register-user/register-user.component';
import { Routes } from '@angular/router';

export const routes: Routes = [
    { path: 'home', component: HomeComponent },
    { path: 'users', component: DisplayUsersComponent },
    { path: 'registration', component: RegisterUserComponent },
    { path: '',   redirectTo: 'home', pathMatch: 'full' }
  ];

