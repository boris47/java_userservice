import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
	selector: 'custom-input',
	templateUrl: './custom-input.component.html'
})
export default class CustomInputComponent
{
	@Input() label: string = '';
	@Input() name: string = '';
	@Input() type: string = 'text';
	@Input() placeholder: string = '';
	
	@Input() model: string = '';
	@Output() modelChange = new EventEmitter<string>();

	handleInput(event: Event): void
	{
		const input = event.target as HTMLInputElement;
		this.model = input.value;
		this.modelChange.emit(this.model);
	}
	
	// this is a bi-directional approch.
	
	// if there is no interest in parent initialize child values then
	
	// in child:
	/*
	// Template
		<input (input)="onInput($event)" .../>
	
	
	// Code
		@Output() valueChanged = new EventEmitter<string>(); // Just the event emitter
		
		onInput(event: Event): void
		{
			const input = event.target as HTMLInputElement;
			this.valueChanged.emit(input.value);
		}
	*/
	
	// in parent
	/*
	// Template
		<custom-input (valueChanged)="STRUCT.FIELD = $event"></custom-input>
	*/
}
