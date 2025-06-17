import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';

declare const window: any;

@Component({
	standalone: true,
	selector: 'app-root',
	imports: [FormsModule],
	templateUrl: './app.component.html'
})
export class AppComponent
{
	formData = {
		name: '',
		email: ''
	};

	onSubmit()
	{
		// Invia al processo main di Electron
		if (window.electronAPI)
		{
			window.electronAPI.sendFormData(this.formData);
		}
		else
		{
			console.error('electronAPI non definito');
		}
	}
}
