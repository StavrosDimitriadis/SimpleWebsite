import { createReducer, on } from '@ngrx/store';
import { UserData } from '../interfaces';
import { deleteUser, deleteUserSuccess, fetchUsers, fetchUsersSuccess, genericError, storeUser, storeUserSuccess, updateUser, updateUserSuccess } from './users.actions';

export interface UsersState {
  usersList: UserData[],
  currentPage: number,
  isLastPage: boolean,
  loadingRows: boolean,
  spinner: boolean
}

const initialUsersState: UsersState = {
  usersList: [],
  currentPage: 0,
  isLastPage: false,
  loadingRows: false,
  spinner: false
};

export const usersReducer = createReducer(

  initialUsersState,

  on(fetchUsers, (state) => ({
    ...state,
    loadingRows: true,
    spinner: true
  })),

  on(fetchUsersSuccess, (state, { usersList, currentPage, isLastPage }) => ({
    ...state,
    usersList: [...state.usersList, ...usersList],
    currentPage,
    isLastPage,
    loadingRows: false,
    spinner: false
  })),

  on(deleteUser, (state, { id }) => ({
    ...state,
    spinner: true
  })),

  on(deleteUserSuccess, (state, { id }) => ({
    ...state,
    usersList: state.usersList.filter(user => user.id != id),
    spinner: false
  })),

  on(updateUser, (state, { payload }) => ({
    ...state,
    spinner: true
  })),

  on(updateUserSuccess, (state, { payload }) => ({
    ...state,
    usersList: state.usersList.map(user => user.id == payload.id ? payload : user),
    spinner: false
  })),

  on(storeUser, (state) => ({
    ...state,
    spinner: true
  })),

  on(storeUserSuccess, (state) => ({
    ...state,
    spinner: false
  })),

  on(genericError, (state) => ({
    ...state,
    spinner: false
  })),
);
