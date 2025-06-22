import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { EventEmitter } from '@angular/core';

import CustomInputComponent from './custom-input.component';

describe('CustomInputComponent', () => 
{
	let component: CustomInputComponent;
	let fixture: ComponentFixture<CustomInputComponent>;

	beforeEach(async () => 
	{
		await TestBed.configureTestingModule({
			imports: [CustomInputComponent],
		}).compileComponents();

		fixture = TestBed.createComponent(CustomInputComponent);
		component = fixture.componentInstance;
	});

	it('should create the component', () => 
	{
		expect(component).toBeTruthy();
	});

	it('should display the placeholder', () => 
	{
		component.placeholder = 'Enter some text';
		fixture.detectChanges();

		const input = fixture.debugElement.query(el => el.name === 'input').nativeElement;
		expect(input.placeholder).toBe('Enter some text');
	});

	it('should bind the input to the model value', () => 
	{
		component.model = 'hello';
		fixture.detectChanges();

		const input = fixture.debugElement.query(el => el.name === 'input').nativeElement;
		expect(input.value).toBe('hello');
	});

	it('should emit modelChange on input event', () => 
	{
		jest.spyOn(component.modelChange, 'emit');
		const input = fixture.debugElement.query(el => el.name === 'input').nativeElement;

		input.value = 'test';
		input.dispatchEvent(new Event('input'));
		fixture.detectChanges();

		expect(component.modelChange.emit).toHaveBeenCalledWith('test');
	});

//	it('should emit modelChange on blur event', () => 
//	{
//		component.model = 'blurTest';
//		fixture.detectChanges();
//
//		jest.spyOn(component.modelChange, 'emit');
//		const input = fixture.debugElement.query(el => el.name === 'input').nativeElement;
//
//		input.dispatchEvent(new Event('blur'));
//		fixture.detectChanges();
//
//		expect(component.modelChange.emit).toHaveBeenCalledWith('blurTest');
//	});
});
