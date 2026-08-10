import { Component, OnInit } from '@angular/core';
import { User } from '../models/user.model';
import { UserService } from '../services/user.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
//import { HttpClientModule } from '@angular/common/http';
// adicionado em app.config.ts  provideHttpClient()

//import { NgIf } from '@angular/common'; 

@Component({
  selector: 'app-user-list',  
  imports: [CommonModule, RouterModule], // 1h28min20seg
  providers: [UserService],
  templateUrl: './user-list.html',
  styleUrl: './user-list.css',
})
export class UserList implements OnInit{

  

  users: User[] = [];

  constructor(private userService : UserService, private router : Router){}

  ngOnInit(): void {
    this.getUsers();
  }

  getUsers(){
    this.userService.getUsers().subscribe((data : User[])=> {
      this.users = data; // store the fetched data
      
      console.log(this.users)
    });
  }

  deleteUser(id: any){
    this.userService.deleteUser(id).subscribe(()=>{
      this.getUsers(); // list refresh after delete
    });

  }

  editUser(id: any){
    this.router.navigate([`/users/edit/${id}`]);

  }


}
