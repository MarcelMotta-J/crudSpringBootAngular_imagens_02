import { Component, OnInit } from '@angular/core';
import { User } from '../models/user.model';
import { UserService } from '../services/user.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
//import { HttpClientModule } from '@angular/common/http';
// adicionado em app.config.ts  provideHttpClient()

//import { NgIf } from '@angular/common'; 

import { FormsModule } from '@angular/forms';

import { Subject, of, catchError } from 'rxjs';
import {
  debounceTime,
  distinctUntilChanged,
  switchMap
} from 'rxjs/operators';

import { PageResponse } from '../models/page-response.model';


@Component({
  selector: 'app-user-list',
  imports: [
    CommonModule,
    RouterModule,
    FormsModule
  ],
  providers: [UserService],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',

})
export class UserList implements OnInit {



  users: User[] = [];

  filteredUsers: User[] = [];
  searchTerm = '';

  currentPage = 0;
  pageSize = 5;
  totalPages = 0;
  totalElements = 0;

  private searchSubject = new Subject<string>();

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.getUsers();

    this.searchSubject.pipe(
      debounceTime(400),
      distinctUntilChanged(),

      switchMap(term => {

        const query = term.trim();

        if (!query) {
          return this.userService.getUsersPage(
            this.currentPage,
            this.pageSize
          );
        }

        return this.userService.searchUsers(
          query,
          this.currentPage,
          this.pageSize
        ).pipe(
          catchError(error => {
            console.error('Error searching users:', error);

            return of({
              content: [],
              totalElements: 0,
              totalPages: 0,
              size: this.pageSize,
              number: 0,
              first: true,
              last: true,
              numberOfElements: 0,
              empty: true
            } as PageResponse<User>);
          })
        );
      })

    ).subscribe((data: PageResponse<User>) => {
      this.filteredUsers = data.content;
      this.currentPage = data.number;
      this.totalPages = data.totalPages;
      this.totalElements = data.totalElements;
    });
  }

  getUsers() {
    this.userService
      .getUsersPage(this.currentPage, this.pageSize)
      .subscribe((data: PageResponse<User>) => {

        this.users = data.content;
        this.filteredUsers = data.content;

        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      });
  }

  deleteUser(id: any) {
    this.userService.deleteUser(id).subscribe(() => {
      this.getUsers(); // list refresh after delete
    });

  }

  editUser(id: any) {
    this.router.navigate([`/users/edit/${id}`]);

  }

  /*
  // search users frontend
  searchUsers(): void {

    const term = this.searchTerm
      .trim()
      .toLowerCase();

    if (!term) {
      this.filteredUsers = this.users;
      return;
    }

    this.filteredUsers = this.users.filter(user => {

      const firstname = user.firstname?.toLowerCase() ?? '';
      const lastname = user.lastname?.toLowerCase() ?? '';
      const email = user.email?.toLowerCase() ?? '';

      return (
        firstname.includes(term) ||
        lastname.includes(term) ||
        email.includes(term)
      );
    });
  }
  */

  // search users backend
 searchUsers(): void {
  this.currentPage = 0;
  this.searchSubject.next(this.searchTerm);
}

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.loadCurrentPage();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.loadCurrentPage();
    }
  }

  loadCurrentPage(): void {

  const query = this.searchTerm.trim();

  if (query) {
    this.userService
      .searchUsers(query, this.currentPage, this.pageSize)
      .subscribe((data: PageResponse<User>) => {
        this.filteredUsers = data.content;
        this.currentPage = data.number;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
      });

    return;
  }

  this.getUsers();
}
}
