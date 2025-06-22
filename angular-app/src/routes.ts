import type { Routes } from '@angular/router';

import UserLoggedInGuard from './app/guards/user-logged-in.guard';
import LogoutComponent from './app/pages/logout.component';

export const routes: Routes =
[
	{ path: '', loadComponent: () => import('./app/pages/homePage/home.component'), },
	{ path: 'login-page', loadComponent: () => import('./app/pages/loginPage/login.component') },
	{ path: 'logout', component: LogoutComponent },
	{ canActivate: [UserLoggedInGuard], path: 'contacts-page', loadComponent: () => import('./app/pages/contanctsPage/contacts.component') },
	{ path: '**', loadComponent: () => import('./app/pages/page-not-found.component') }
];