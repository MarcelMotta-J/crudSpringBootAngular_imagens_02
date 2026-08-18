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
    FormsModule,
    RouterModule
  ],
  templateUrl: './user-create.html',
  styleUrl: './user-create.css',
  providers: [UserService]
})



export class UserCreate {

  userForm!: FormGroup;

  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private router: Router) {

    this.userForm = this.fb.group({
      first_name: ['', Validators.required],
      last_name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      cpf: ['', [
        Validators.required,
        Validators.pattern(/^\d{11}$/)
      ]],
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
    if (this.userForm.invalid) { return; }

    const formData = new FormData();
    formData.append('first_name', this.userForm.get('first_name')!.value);
    formData.append('last_name', this.userForm.get('last_name')!.value);
    formData.append('email', this.userForm.get('email')!.value);
    formData.append('cpf', this.userForm.get('cpf')!.value);
    formData.append('password', this.userForm.get('password')!.value);

    const imageFile = this.userForm.get('image')!.value;
    if (imageFile) {
      formData.append('image', imageFile, imageFile.name);
    }

    this.errorMessage = '';

    this.userService.createUser(formData).subscribe({

      next: () => {
        this.router.navigate(['/users']);
      },

      error: (error) => {

        if (error.status === 400) {
          this.errorMessage = error.error?.message ?? 'Invalid CPF';
        }
        else if (error.status === 409) {
          this.errorMessage =
            error.error?.message ?? 'CPF or email already registered';
        }
        else {
          this.errorMessage = 'An unexpected error occurred';
        }

        console.error('Error creating user:', error);
      }
    });
  }
}
