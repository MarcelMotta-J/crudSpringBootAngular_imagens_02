import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes'; 

import { provideHttpClient } from '@angular/common/http';   // adicionar aqui   provideHttpClient

//import {  HttpClientModule} from '@angular/common/http'; // DEPRECATED SUBSTITUIDO POR provideHttpClient

import { ReactiveFormsModule, FormsModule } from '@angular/forms';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient()   // adicionar aqui provideHttpClient
  ]
};
