import { Component, OnInit } from '@angular/core';
import { User } from '../models/user.model';
import { UserService } from '../services/user.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
//import { HttpClientModule } from '@angular/common/http';
// adicionado em app.config.ts  provideHttpClient()

//import { NgIf } from '@angular/common'; 

import { FormsModule } from '@angular/forms';



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

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers() {
    this.userService.getUsers().subscribe((data: User[]) => {
      this.users = data; // store the fetched data
      this.filteredUsers = data;

      console.log(this.users)
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


}
