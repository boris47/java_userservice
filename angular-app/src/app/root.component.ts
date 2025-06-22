import { type AfterViewInit, Component, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CommonModule } from '@angular/common';

import { environment } from './../environments/environment';
import GlobalDialogComponent from '@components/global-dialog/global-dialog.component';
import DialogService from '@services/globalDialog';

@Component({
	selector: 'app-root',
	standalone: true,
	templateUrl: './root.component.html',
	imports: [CommonModule, RouterOutlet, GlobalDialogComponent],
})
export default class RootComponent implements AfterViewInit
{
	@ViewChild('dialogRef') private dialogRef!: GlobalDialogComponent;
	
	constructor(private dialogService: DialogService)
	{
		//console.log(environment.production); // Logs false for development environment
	}
	
	ngAfterViewInit()
	{
		this.dialogService.register(this.dialogRef);
	}
}
