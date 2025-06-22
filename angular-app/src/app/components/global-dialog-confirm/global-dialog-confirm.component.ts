import { CommonModule } from "@angular/common";
import { Component } from "@angular/core";

import { type IDialogBaseData, BaseDialogComponent } from "@components/global-dialog/global-dialog.component";

interface IConfirmDialog extends Omit<Omit<IDialogBaseData, 'escapeClose'>, 'outsideClickClose'>
{
	onConfirm(): any;
	onCancel(): any;
}

@Component({
	selector: 'global-modal-confirm',
	standalone: true,
	imports: [CommonModule],
	templateUrl: 'global-dialog-confirm.component.html',
})
export default class GlobalDialogConfirmComponent extends BaseDialogComponent<IConfirmDialog, boolean>
{
	onConfirm?: () => any;
	onCancel?: () => any;
	
	protected override onClose(result?: boolean): void
	{
		BaseDialogComponent.logMessages && console.log("GlobalDialogConfirm:onClose");
		
		result ? this.onConfirm?.() : this.onCancel?.();
	}
}
