import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Page, UserData } from '../interfaces';
import { USERS } from '../endpoints';

@Injectable({
  providedIn: 'root'
})

export class UsersService {

  constructor(private http: HttpClient) {}

  public storeUser(payload: UserData): Observable<void> {
    return this.http.post<void>(USERS.STORE, payload)
  }

  public getUsers(page: number): Observable<Page<UserData>> {
    return this.http.get<Page<UserData>>(USERS.FETCH(page));
  }

  public deleteUser(id?: number): Observable<void> {
    return this.http.delete<void>(USERS.DELETE(id));
  }

  public updateUser( payload: UserData): Observable<void> {
    return this.http.put<void>(USERS.UPDATE(payload.id), payload)
  }
}
