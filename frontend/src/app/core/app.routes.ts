import { DisplayUsersComponent } from '../components/display-users/display-users.component';
import { Routes } from '@angular/router';
import { HomeComponent } from '../components/home/home.component';
import { RegisterUserComponent } from '../components/register-user/register-user.component';

export const routes: Routes = [
    { path: 'home', component: HomeComponent },
    { path: 'users', component: DisplayUsersComponent },
    { path: 'registration', component: RegisterUserComponent },
    { path: '',   redirectTo: 'home', pathMatch: 'full' }
  ];

