export interface UserForm {
  firstname: string;
  lastname: string;
  email: string;
  password: string;
  active: boolean | null;
  profileImage: File | null;
}