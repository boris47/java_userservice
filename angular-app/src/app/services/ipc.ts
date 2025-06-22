
import { EMessageContentType, EComunicationsChannels, IComunications, IMessageContentReturnTypeMap } from '@shared/icpComs';


const MappedOps : { [key in EMessageContentType]: IMessageContentReturnTypeMap[key]; } =
{
	[EMessageContentType.BOOLEAN] 		: ( value: Boolean )		=> typeof value === 'boolean'		? value						: null,
	[EMessageContentType.NUMBER]		: ( value: Number )			=> typeof value === 'number'		? value						: null,
	[EMessageContentType.STRING]		: ( value: String )			=> typeof value === 'string'		? value						: null,
	[EMessageContentType.BUFFER]		: ( value: Uint8Array )		=> value && value.length > 0 		? Buffer.from(value) 		: null,
	[EMessageContentType.OBJECT]		: ( value: Object )			=> typeof value === 'object' 		? value						: null,
	[EMessageContentType.ARRAY]			: ( value: Array<any> )		=> Array.isArray(value)				? value						: null,
	[EMessageContentType.ERROR]			: ( value: Error )			=> value instanceof Error 			? value						: null,
	[EMessageContentType.NULL]			: ( value: null )			=> value,
	[EMessageContentType.UNDEFINED]		: ( value: undefined )		=> value,
};

export namespace ICP_Service
{
	export function IsElectron(): boolean
	{
		return !!(window as any).ICP_RendererInterface;
	}
	
	/** */
	export function Notify(channel: string, ...args: any[]): void
	{
		(window as any).ICP_RendererInterface.notify(channel, ...args);
	}

	/** Allow async comunication to main process
	 * @param channel An `EComunications` channel, Ex: EComunications.ELECTRON_PATH
	 * @param args Arguments to pass to the caller 
	 */
	export async function Request<T extends keyof IComunications>(channel: T, ...args: IComunications[T]['args']): Promise<IComunications[T]['return'] | null | undefined>
	{
		const result = await Promise.resolve((window as any).ICP_RendererInterface.request(channel, ...args));
		const typeString = Object.prototype.toString.call(result);
		const type = typeString.substring('[object '.length, typeString.length - 1);
	//	console.log( 'ICP_RendererComs:Invoke', channel, args, typeString, type, result )
		
		const ctor: (arg: any) => any = MappedOps[type as EMessageContentType];
		if ( ctor )
		{
			return ctor(result);
		}
		console.error( `RendererComs:Invoke: Unrecognized/unsupported type received at channel ${channel} with args: (${args}), type is ${type}` );
		return null;
	}

}


export default async function InitializeRenderer(): Promise<void>
{
	await new Promise<void>(resolve => document.readyState === 'loading'
		? document.addEventListener('DOMContentLoaded', () => resolve()) : resolve());
	
	// @ts-ignore
	(globalThis as any).ICP_Service = ICP_Service;
	
	// @ts-ignore
	(globalThis as any).ComunicationChannels = EComunicationsChannels;
	
	if (ICP_Service.IsElectron())
	{
		// notify Main that Renderer is ready
		ICP_Service.Notify('rendererReady');
	}
}