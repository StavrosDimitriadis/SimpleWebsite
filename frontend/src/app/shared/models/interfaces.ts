import { deleteUserSuccess, storeUserSuccess, updateUserSuccess } from "../../store/users-store/users.actions"
import { UsersState } from "../../store/users-store/users.reducers"

export interface UserData {
  id?: number,
  name: string,
  surname: string,
  gender: string,
  birthdate: Date,
  homeAddress?: string,
  workAddress?: string
}

export interface Pageable {
  pageNumber: number,
  pageSize: number,
  sort: Sort,
  offset: number,
  paged: boolean,
  unpaged: boolean
}

export interface Sort {
  sorted: boolean,
  empty: boolean,
  unsorted: boolean
}

export interface Page<T> {
  content: T[],
  pageable: Pageable,
  totalPages: number,
  totalElements: number,
  last: boolean,
  size: number,
  number: number,
  sort: Sort,
  numberOfElements: number,
  first: boolean,
  empty: boolean
}

export interface AppState {
  users: UsersState
}

export const successMessages: Record<string, string> = {
  [storeUserSuccess.type]: 'USER_CREATED_SUCCESSFULLY',
  [updateUserSuccess.type]: 'USER_UPDATED_SUCCESSFULLY',
  [deleteUserSuccess.type]: 'USER_DELETED_SUCCESSFULLY',
};

export const LANGUAGES = {
  English: 'en',
  Greek: 'el'
};
