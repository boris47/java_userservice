import { Component, OnInit  } from "@angular/core";
import { Router } from "@angular/router";

import AuthService from "@services/auth";

@Component({
	standalone: true,
	selector: 'home-page',
	templateUrl:'./home.component.html',
	imports: [],
})
export default class HomePage implements OnInit
{
	constructor(private auth: AuthService, private router: Router)
	{
		if (auth.isLoggedIn())
		{
			this.router.navigate(['/contacts-page']);
		}
	}
	
	/* interface OnInit */ngOnInit()
	{
		if (ICP_Service.IsElectron())
		{
			// ICP_Service.Request(ComunicationChannels.ELECTRON_MODAL_OPEN, {})
			// .then(console.dir)
		}
	}
	
	goToLoginPage()
	{
		this.router.navigate(['/login-page']);
	}
}