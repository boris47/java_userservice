import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormsModule } from '@angular/forms';
import { AppComponent } from './app.component';

describe('AppComponent', () =>
{
	let fixture: ComponentFixture<AppComponent>;
	let component: AppComponent;

	beforeEach(async () =>
	{
		await TestBed.configureTestingModule({
			imports: [FormsModule, AppComponent]
		}).compileComponents();

		fixture = TestBed.createComponent(AppComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create the app', () =>
	{
		expect(component).toBeTruthy();
	});

	it('should update formData when input changes', async () =>
	{
		const compiled = fixture.nativeElement as HTMLElement;
		const nameInput = compiled.querySelector('input[name="name"]') as HTMLInputElement;
		const emailInput = compiled.querySelector('input[name="email"]') as HTMLInputElement;

		// Simula input
		nameInput.value = 'Roberto';
		nameInput.dispatchEvent(new Event('input', { bubbles: true }));

		emailInput.value = 'roberto@example.com';
		emailInput.dispatchEvent(new Event('input', { bubbles: true }));

		await fixture.whenStable();
		fixture.detectChanges();

		expect(component.formData.name).toBe('Roberto');
		expect(component.formData.email).toBe('roberto@example.com');
	});
});
