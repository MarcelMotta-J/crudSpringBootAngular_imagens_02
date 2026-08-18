import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../services/user.service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-user-update',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    RouterModule

  ],
  providers: [UserService],
  templateUrl: './user-update.html',
  styleUrl: './user-update.css',
})
export class UserUpdate implements OnInit {

  imagePreview: string | ArrayBuffer | null = null;
  userForm!: FormGroup;
  userId!: number;

  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router) {

    this.userForm = this.fb.group({
      first_name: ['', Validators.required],
      last_name: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      cpf: ['', [
        Validators.required,
        Validators.pattern(/^\d{11}$/)
      ]],
      password: [''],
      image: [null]
    });
  }


  ngOnInit(): void {
    this.userId = Number(this.route.snapshot.paramMap.get('id'));
    if (this.userId) {
      this.userService.getUserById(this.userId).subscribe(user => {
        this.userForm.patchValue({
          first_name: user.firstname,
          last_name: user.lastname,
          email: user.email,
          cpf: user.cpf,
          password: '',
          profileImage: user.profileImage
        });
        // 👇 show existing image
        if (user.profileImage) {
          this.imagePreview = `http://localhost:8081/uploads/${user.profileImage}`;
        }
      });
    }


  }


  onFileSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.userForm.patchValue({ image: file });
      this.userForm.get('image')!.updateValueAndValidity();
    }

    // preview
    const reader = new FileReader();
    reader.onload = () => {
      this.imagePreview = reader.result;
    };
    reader.readAsDataURL(file);
  }

  updateUser() {
    if (this.userForm.invalid) { return; }

    const formData = new FormData();
    formData.append('first_name', this.userForm.get('first_name')!.value);
    formData.append('last_name', this.userForm.get('last_name')!.value);
    formData.append('email', this.userForm.get('email')!.value);
    formData.append('cpf', this.userForm.get('cpf')!.value);

    const password = this.userForm.get('password')!.value;

    if (password?.trim()) {
      formData.append('password', password);
    }

    const imageFile = this.userForm.get('image')!.value;
    if (imageFile) {
      formData.append('image', imageFile, imageFile.name);
    }

    this.errorMessage = '';

    this.userService.updateUser(this.userId, formData).subscribe({

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

        console.error('Error updating user:', error);
      }
    });
  }
}
