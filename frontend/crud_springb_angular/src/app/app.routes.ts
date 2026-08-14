import { Routes } from '@angular/router';

import { UserList } from './user-list/user-list';
import { UserUpdate } from './user-update/user-update';
import { UserCreate } from './user-create/user-create';

import { Login } from './auth/login/login';
import { authGuard } from './auth/auth-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  { path: 'login', component: Login },

  {
    path: 'users',
    component: UserList,
    canActivate: [authGuard]
  },
  {
    path: 'users/create',
    component: UserCreate,
    canActivate: [authGuard]
  },
  {
    path: 'users/edit/:id',
    component: UserUpdate,
    canActivate: [authGuard]
  }

];