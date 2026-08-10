import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule  
    
  ],
  templateUrl: './user-create.html',
  styleUrl: './user-create.css',
  providers:[UserService]
})



export class UserCreate {

  userForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router) {

    this.userForm = this.fb.group({
      first_name: ['', Validators.required],
      last_name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
      image: [null]
    });
  }

  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.userForm.patchValue({ image: file });
      this.userForm.get('image')!.updateValueAndValidity();
    }
  }

  createUser(): void {
      if(this.userForm.invalid){return;}

      const formData = new FormData();
      formData.append('first_name', this.userForm.get('first_name')!.value);
      formData.append('last_name', this.userForm.get('last_name')!.value);
      formData.append('email', this.userForm.get('email')!.value);
      formData.append('password', this.userForm.get('password')!.value);

      const imageFile = this.userForm.get('image')!.value;
      if(imageFile){
        formData.append('image', imageFile, imageFile.name);
      }

      this.userService.createUser(formData).subscribe( ()=> {
        this.router.navigate(['/users']);
      });
  }

}
