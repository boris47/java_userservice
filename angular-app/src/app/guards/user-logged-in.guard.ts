import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router, UrlTree } from '@angular/router';

@Injectable({ providedIn: 'root' })
export default class UserLoggedInGuard implements CanActivate
{
	constructor(private router: Router) { }
	
	canActivate(_: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean | UrlTree
	{
		let url: string = state.url;
		return this.checkLogin(url);
	}
	
	private checkLogin(url: string): true | UrlTree
	{
		//console.log("AuthGuard:URL -> " + url);
		
		// Check if sessionStorage is available (only in the browser)
		if (typeof window !== 'undefined' && window.sessionStorage)
		{
			let val = sessionStorage.getItem('isUserLoggedIn');
			
			// Check if the value is 'true'
			if (val === "true" && val != null)
			{
				// If the user is already logged in and trying to access the login page, redirect to /expenses
				if (url === "/login-page")
				{
					return this.router.parseUrl('/contacts-page');
				}
				else
				{
					return true; // User is allowed to proceed
				}
			}
			else
			{
				// If the user is not logged in, redirect to /login
				return this.router.parseUrl('/login-page');
			}
		}
		
		// In case sessionStorage isn't available (for SSR)
		return this.router.parseUrl('/login-page');
	}
}