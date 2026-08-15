import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs";
import { User } from "../models/user.model";

@Injectable({
    providedIn: "root" //standalone service
})
export class UserService {

    private apiUrl = "http://localhost:8081/api/"; // url for backend



    constructor(private http: HttpClient) { }

    // get all users
    getUsers(): Observable<User[]> {
        return this.http.get<User[]>(this.apiUrl + 'users');
    }

    // create a new user
    createUser(user: FormData): Observable<any> {
        return this.http.post(this.apiUrl + 'users', user);
    }

    // get a single user by id
    getUserById(id: number): Observable<User> {
        return this.http.get<User>(`${this.apiUrl}users/${id}`);
    }

    // update an existing user
    updateUser(id: number, user: FormData): Observable<any> {
        return this.http.put(`${this.apiUrl}users/${id}`, user);
    }

    // delete user
    deleteUser(id: number): Observable<any> {
        return this.http.delete(`${this.apiUrl}users/${id}`);
    }

    // search users
    searchUsers(query: string): Observable<User[]> {
        return this.http.get<User[]>(
            `${this.apiUrl}users/search?query=${encodeURIComponent(query)}`
        );
    }
}
