import { environment } from "./environment";

const apiUrl = environment.apiUrl;

const resources = {
    users: `${apiUrl}/users`
}

const usersUrl = resources.users;
export const USERS = {
    STORE: `${usersUrl}/storeUser`,
    FETCH: (page: number) => `${usersUrl}/fetchUsers?page=${page}`,
    DELETE: (id?: number) => `${usersUrl}/delete/${id}`,
    UPDATE: (id?: number) => `${usersUrl}/updateUser/${id}`
};
