import { createAction, props } from '@ngrx/store';
import { UserData } from '../../shared/models/interfaces';

export const fetchFirstUsers = createAction(
  '[Users] Fetch First Users'
);

export const fetchUsers = createAction(
  '[Users] Fetch Users',
  props<{ page: number }>()
);

export const fetchUsersSuccess = createAction(
  '[Users] Fetch Users Success',
  props<{ usersList: UserData[]; currentPage: number; isLastPage: boolean }>()
);

export const deleteUser = createAction(
  '[Users] Delete User',
  props<{ id?: number }>()
)

export const deleteUserSuccess = createAction(
  '[Users] Delete User Success',
  props<{ id?: number }>()
);

export const storeUser = createAction(
  '[Users] Store User',
  props<{ payload: UserData }>()
)

export const storeUserSuccess = createAction(
  '[Users] Store User Success'
)

export const updateUser = createAction(
  '[Users] Update User',
  props<{ payload: UserData }>()
)

export const updateUserSuccess = createAction(
  '[Users] Update User Success',
  props<{ payload: UserData }>()
)

export const genericError = createAction(
  '[Users] API Error',
  props<{ error: any }>()
)
