import { computed, Injectable, signal } from '@angular/core';

@Injectable({ providedIn: 'root' })
export default class AuthService
{
	private token: string = '';
	private isAuthenticatedSignal = signal(false);
	
	public isLoggedIn = computed(() => this.isAuthenticatedSignal());
	public get Token() : string { return this.token; }
	
	
	constructor()
	{
		// Only initialize sessionStorage on the client-side (browser)
		if (typeof window !== 'undefined' && window.sessionStorage)
		{
			const storedLoginState = sessionStorage.getItem('isUserLoggedIn') === 'true';
			
			this.isAuthenticatedSignal.set(storedLoginState);
		}
	}
	
	login(token: string)
	{
		this.token = token;
		if (typeof window !== 'undefined' && window.sessionStorage)
		{
			sessionStorage.setItem('isUserLoggedIn', 'true');
		}
		
		this.isAuthenticatedSignal.set(true);
	}

	logout()
	{
		if (typeof window !== 'undefined' && window.sessionStorage)
		{
			sessionStorage.removeItem('isUserLoggedIn');
		}
		
		this.isAuthenticatedSignal.set(false);
	}
}
