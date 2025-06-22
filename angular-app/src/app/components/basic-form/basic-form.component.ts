import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

import type ILoginCredentials from '@shared/models/login-credentials.model';
import CustomInputComponent from '../custom-input/custom-input.component';

@Component({
	selector: 'basic-form',
	standalone: true,
	imports: [CommonModule, FormsModule, CustomInputComponent],
	templateUrl: './basic-form.component.html'
})
export default class BasicFormComponent
{
	public readonly formData : ILoginCredentials = {
		username: '',
		password: ''
	};
	
	@Input() disabled = true;
	
	@Output() onFormDataSubmit = new EventEmitter<ILoginCredentials>();

	onFormSubmit()
	{
		this.onFormDataSubmit.emit(this.formData)
	}
}
