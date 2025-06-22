import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import AuthService from '@services/auth';


@Component({
	selector: 'app-logout',
	standalone: true,
	imports: [],
	template: ''
})
export default class LogoutComponent implements OnInit
{
	constructor(private authService: AuthService, private router: Router) { }
	
	ngOnInit()
	{
		
	}
	
	public LogOut(): void
	{
		this.authService.logout();
		
		this.router.navigate(['/']);	
	}
}