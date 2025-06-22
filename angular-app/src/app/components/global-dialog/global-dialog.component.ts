import { CommonModule } from "@angular/common";
import { Component, type Type, ViewContainerRef, viewChild, type OnDestroy, type OnInit, ChangeDetectorRef } from "@angular/core";

export interface IDialogBaseData
{
	title: string;
	message: string;
}

export abstract class BaseDialogComponent<TInput = void, TResult = any> implements IDialogBaseData
{
	static logMessages: boolean = false;
	
	title: string = '';
	message: string = '';
	data!: TInput;
	
	public close(result?: TResult): void
	{
		this.onClose(result);
	}
	
	/**
	 * @virtual
	 */
	public onOutsideClick(): void
	{
		
	}
	
	/**
	 * @virtual
	 */
	protected onClose(result?: TResult): void { }
}

@Component({
	selector: 'app-global-dialog',
	template: `
		<div
			[class.hidden]="!isOpened"
        	class="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50 backdrop-blur-sm"
			(click)="onOutsideClick($event)"
		>
			<div class="bg-white p-6 rounded shadow-lg max-w-md w-full">
				<ng-container #globalDialogContainer />
			</div>
		</div>
	`,
	imports: [CommonModule]
})
export default class GlobalDialogComponent implements OnInit, OnDestroy
{
	containerRef = viewChild.required('globalDialogContainer', { read: ViewContainerRef });
	
	public isOpened = false;
	
	private currentDialog: BaseDialogComponent | null = null;
	
	constructor(private cdr: ChangeDetectorRef) {}

	ngOnInit()
	{ }

	ngOnDestroy()
	{ }
	
	open
	<T extends IDialogBaseData, Dialog extends BaseDialogComponent<T>, Data extends Dialog['data']>
	(component: Type<Dialog>, data: Data): Dialog | null
	{
		const container = this.containerRef();
		console.assert(container, "GlobalDialogComponent.open: container is null");
		if (container)
		{
			container.clear();
			const ref = container.createComponent(component);
			console.assert(ref, "GlobalDialogComponent.open: ref is null");
			if (ref)
			{
				this.isOpened = true;
				document.body.classList.add('overflow-hidden'); // lock the scroll
				
				// Assign vars values
				for(const [key, val] of Object.entries(data))
				{
					(ref.instance as any)[key] = val;
				}
				
				const prevClose = ref.instance.close;
				ref.instance.close = (args?: any) =>
				{
					BaseDialogComponent.logMessages && console.log("GlobalDialogComponent (ref.instance.close)", args);
					
					document.body.classList.remove('overflow-hidden'); // unlock the scroll
					
					this.isOpened = false;
					
					container.clear();
					
					// Since isOpened has been changed force changes detection
					// again to avoid ExpressionChangedAfterItHasBeenCheckedError
					this.cdr.detectChanges();
					
					prevClose.call(ref.instance, args);
				}
				
				return (this.currentDialog as any) = ref.instance;
			}
		}
		return null;
	}
	
	onOutsideClick(_?: any): void
	{
		this.currentDialog?.onOutsideClick();
	}
}
