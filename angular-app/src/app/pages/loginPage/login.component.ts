import { Component, effect } from "@angular/core";
import { Router } from "@angular/router";

import AxiosInstance from '@services/api';
import AuthService from "@services/auth";

import type ILoginCredentials from "@shared/models/login-credentials.model";
import BasicFormComponent from "@components/basic-form/basic-form.component";
import DialogService from "@services/globalDialog";
import GlobalDialogConfirmComponent from "@components/global-dialog-confirm/global-dialog-confirm.component";
import GlobalDialogInfosComponent from "@components/global-dialog-infos/global-dialog-infos.component";

@Component({
	standalone: true,
	selector: 'login-page',
	templateUrl: './login.component.html',
	imports: [BasicFormComponent],
})
export default class LoginPage
{
	isProcessing: boolean = false;
	
	constructor(private auth: AuthService, private router: Router, private dialogService: DialogService)
	{
		// this.dialogService.open(GlobalDialogInfos, {
		// 	title: "asd", message: "aldooooooo", escapeClose: true, outsideClickClose: true
		// });
		this.dialogService.open(GlobalDialogConfirmComponent, {
			title: 'Autologin',
			message: 'You want to try auto-login?',
			onCancel: () => { console.log("LoginPage: canceled"); },
			onConfirm: () =>
			{
				console.log("LoginPage: confirmed");
				if (this.auth.isLoggedIn())
					this.router.navigate(['/contacts-page']);
				else
					alert("No login detected");
			},
		});
	}

	public async tryLogin(arg: ILoginCredentials): Promise<void>
	{
		if (!this.isProcessing && this.CheckForm(arg))
		{
			this.isProcessing = true;
			{
				try
				{
					const { token } = await AxiosInstance.TryLogin(arg);
					this.auth.login(token);
					this.router.navigate(['/contacts-page']);
				}
				catch (error)
				{
					alert("Login failed"); // TODO Handle error/exception
				}
			}
			this.isProcessing = false;
		}
	}
	
	private CheckForm(arg: ILoginCredentials): boolean
	{
		return arg
		&&	arg.username?.length > 0
		&&	arg.password?.length > 0
		;
	}
}