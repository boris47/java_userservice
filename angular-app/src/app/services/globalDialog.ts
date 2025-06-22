import { Injectable, type Type } from "@angular/core";

import { BaseDialogComponent, type IDialogBaseData } from "@components/global-dialog/global-dialog.component";
import type GlobalDialogComponent from "@components/global-dialog/global-dialog.component";


@Injectable({ providedIn: 'root' })
export default class DialogService
{
	private dialogRef!: GlobalDialogComponent;
	private dialogOpen = false;
	
	register(dialog: GlobalDialogComponent)
	{
		this.dialogRef = dialog;
	}
	
	open
	<T extends IDialogBaseData, Dialog extends BaseDialogComponent<T>, Data extends Dialog['data']>
	(component: Type<Dialog>, data: Data): void
	{
		if (this.dialogOpen)
		{
			alert("Global Dialog already open !!");
			return;
		}
		
		const dialogInstance = this.dialogRef.open(component, data);
		if (dialogInstance)
		{
			this.dialogOpen = true;
			const prevClose = dialogInstance.close;
			dialogInstance.close = (args?: any) =>
			{
				BaseDialogComponent.logMessages && console.log("DialogService:open (dialogInstance.close)", args);
				this.dialogOpen = false;
				prevClose.call(dialogInstance, args);
			}
		}
	}
	
	isDialogOpen(): boolean
	{
		return this.dialogOpen;
	}
}
