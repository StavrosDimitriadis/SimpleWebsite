import { createSelector } from '@ngrx/store';
import { AppState } from '../interfaces';
import { UsersState } from './users.reducers';

export const usersSlice = (state: AppState) => state.users;

export const selectUsersState = createSelector(
  usersSlice,
  (state: UsersState) => state
);

export const selectUserById = (id?: number) => createSelector(
  usersSlice,
  (state: UsersState) => state.usersList.find(user => user.id == id)
);

export const spinnerState = createSelector(
  usersSlice,
  (state: UsersState) => state.spinner
)
