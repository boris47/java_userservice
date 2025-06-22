import * as electron from 'electron';

import { EComunicationsChannels, ElectronPath, IComunications } from '../../shared/icpComs';

//
const GetElectronProperty = (funcPath: string[]): any | null =>
{
	let electronField = electron as any;
	try { while (funcPath.length > 0 && electronField) electronField = electronField[funcPath.shift()!]; }
	catch (ex) { console.error(ex); electronField = null; }
	return electronField || null;
};


const MappedHandlers : { [key in EComunicationsChannels]:( event: Electron.IpcMainInvokeEvent, ...args: IComunications[key]['args'] ) => (IComunications[key]['return'] | null) | Promise<IComunications[key]['return'] | null> } = 
{
	[EComunicationsChannels.ELECTRON_PROPERTY]: ( event: electron.IpcMainInvokeEvent, propertyPath: string[] ): string | number | object | null =>
	{
		return GetElectronProperty(propertyPath);
	},

	[EComunicationsChannels.ELECTRON_CALL]: ( event: electron.IpcMainInvokeEvent, functionPath: string[], ...args: any[] ): string | number | object | null =>
	{
		let func: (...args: any[]) => any | null;
		const result: string | number | object | null = (typeof (func = GetElectronProperty(functionPath)) === 'function' ? func(...args) : null);
		return result;
	},

	[EComunicationsChannels.ELECTRON_PATH]: ( event: electron.IpcMainInvokeEvent, path: ElectronPath ): string | Error =>
	{
		let result: string | Error = '';
		try
		{
			// A path to a special directory or file associated with name. On failure, an Error is thrown.
			result = electron.app.getPath(path as any);
		}
		catch (err)
		{
			result = err as Error;
		}
		return result;
	},

	[EComunicationsChannels.ELECTRON_MODAL_OPEN]: ( event: electron.IpcMainInvokeEvent, options: electron.OpenDialogOptions ): Promise<electron.OpenDialogReturnValue> =>
	{
		const window: electron.BaseWindow = electron.BrowserWindow.fromWebContents(event.sender)!;
		return electron.dialog.showOpenDialog(window, options);
	}
};

export function SetupMainHandlers(): void
{
	for (const [channel, callback] of Object.entries<((...args: any) => any | Promise<any>)>(MappedHandlers))
	{
		console.log("Registering channel", channel);
		
		electron.ipcMain.handle(channel, async (event: electron.IpcMainInvokeEvent, ...args: any[]): Promise<any> =>
		{
			//	console.log('ICP_MAIN: Received',channel, comFlowManagerId);
			return await Promise.resolve((callback)(event, ...args));
		});
	}
}