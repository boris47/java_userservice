import { ComponentFixture, TestBed } from '@angular/core/testing';
import RootComponent from './root.component';

describe('RootComponent', () =>
{
	let component: RootComponent;
	let fixture: ComponentFixture<RootComponent>;

	beforeEach(async () =>
	{
		await TestBed.configureTestingModule({
			imports: [RootComponent],
		}).compileComponents();
	});

	beforeEach(() =>
	{
		fixture = TestBed.createComponent(RootComponent);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create the app', () =>
	{
		expect(component).toBeTruthy();
	});
});