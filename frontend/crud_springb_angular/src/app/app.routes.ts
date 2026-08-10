import { provideRouter, Routes } from '@angular/router';
import { UserList } from './user-list/user-list';
import { UserUpdate } from './user-update/user-update';
import { UserCreate } from './user-create/user-create';
import { bootstrapApplication } from '@angular/platform-browser';
import { App } from './app';

export const routes: Routes = [
    {path: '', redirectTo: 'users', pathMatch: 'full'},
    {path: 'users', component: UserList},
    {path:'users/create', component: UserCreate},
    {path: 'users/edit/:id', component: UserUpdate},
];

export const AppRoutes = provideRouter(routes);

bootstrapApplication(App, {providers: [AppRoutes] });


