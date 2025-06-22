import { CommonModule } from "@angular/common";
import { type OnDestroy, type OnInit, Component } from "@angular/core";
import { type IDialogBaseData, BaseDialogComponent } from "@components/global-dialog/global-dialog.component";

interface IInfoDialog extends IDialogBaseData
{
	outsideClickClose?: boolean;
	escapeClose?: boolean;
}

@Component({
	selector: 'global-modal-confirm',
	standalone: true,
	imports: [CommonModule],
	templateUrl: 'global-dialog-infos.component.html',
})
export default class GlobalDialogInfosComponent extends BaseDialogComponent<IInfoDialog, void> implements OnInit, OnDestroy
{
	private escapeClose: boolean = false;
	private outsideClickClose: boolean = false;
	
	ngOnInit()
	{
		window.addEventListener('keydown', this.onKeyDown);
	}

	ngOnDestroy()
	{
		window.removeEventListener('keydown', this.onKeyDown);
	}
	
	public override onOutsideClick(): void
	{
		this.outsideClickClose && this.close();
	}
	
	protected override onClose(result?: void): void
	{
		BaseDialogComponent.logMessages && console.log("GlobalDialogInfos:onClose");
	}
	
	private onKeyDown = (e: KeyboardEvent) =>
	{
		//console.log("escape", this.escapeClose, this.isOpened, e.key)
		if (this.escapeClose && e.key === 'Escape')
		{
			// This way the correct closing sequence is respected
			this.close();
		}
	};
}
