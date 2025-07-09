import { Injectable, inject } from '@angular/core';
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { of } from 'rxjs';
import { map, exhaustMap, catchError, tap } from 'rxjs/operators';
import { UsersService } from './users.service';
import {
  deleteUser,
  deleteUserSuccess,
  fetchFirstUsers,
  fetchUsers,
  fetchUsersSuccess,
  genericError,
  storeUser,
  storeUserSuccess,
  updateUser,
  updateUserSuccess,
} from './users.actions';
import { NotificationsComponent } from '../../components/notifications/notifications.component';
import { successMessages } from '../../shared/models/interfaces';

@Injectable()
export class UsersEffects {
  private actions$ = inject(Actions);
  private usersService = inject(UsersService);
  private notificationService = inject(NotificationsComponent);

  loadFirstUsers$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(fetchFirstUsers),
      exhaustMap(() =>
        this.usersService.getUsers(0).pipe(
          map((page) =>
            fetchUsersSuccess({
              usersList: page.content,
              currentPage: page.number,
              isLastPage: page.last,
            })
          ),
          catchError((error) => {
            console.error('Failed to fetch first page of users', error);
            return of(genericError({ error }));
          })
        )
      )
    );
  });

  loadUsers$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(fetchUsers),
      exhaustMap((action) =>
        this.usersService.getUsers(action.page).pipe(
          map((page) =>
            fetchUsersSuccess({
              usersList: page.content,
              currentPage: page.number,
              isLastPage: page.last,
            })
          ),
          catchError((error) => {
            console.error('Failed to fetch users', error);
            return of(genericError({ error }));
          })
        )
      )
    );
  });

  deleteUser$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(deleteUser),
      exhaustMap(({ id }) =>
        this.usersService.deleteUser(id).pipe(
          map(() => deleteUserSuccess({ id })),
          catchError((error) => {
            console.error('Failed to delete user', error);
            return of(genericError({ error }));
          })
        )
      )
    );
  });

  storeUser$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(storeUser),
      exhaustMap(({ payload }) =>
        this.usersService.storeUser(payload).pipe(
          map(() => storeUserSuccess()),
          catchError((error) => {
            console.error('Failed to store user', error);
            return of(genericError({ error }));
          })
        )
      )
    );
  });

  updateUser$ = createEffect(() => {
    return this.actions$.pipe(
      ofType(updateUser),
      exhaustMap(({ payload }) =>
        this.usersService.updateUser(payload).pipe(
          map(() => {
            return updateUserSuccess({ payload });
          }),
          catchError((error) => {
            console.error('Failed to update user', error);
            return of(genericError({ error }));
          })
        )
      )
    );
  });

  notificationError$ = createEffect(
    () =>
      this.actions$.pipe(
        ofType(genericError),
        tap(() => {
          this.notificationService.error('ERROR_TITLE_MESSAGE', 'AN_UNEXPECTED_ERROR_OCCURED');
        })
      ),
    { dispatch: false }
  );

  notificationSuccess$ = createEffect(() =>
    this.actions$.pipe(
      ofType(
        storeUserSuccess,
        updateUserSuccess,
        deleteUserSuccess
      ),
      tap(action => {
        this.notificationService.success('SUCCESS_TITLE_MESSAGE', successMessages[action.type]);
      })
    ),
    { dispatch: false }
  );
}
