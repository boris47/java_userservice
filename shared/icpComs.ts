import * as electron from 'electron';

export enum EComunicationsChannels
{
	/////////////////////////////////////////////////
	/////////////////  ELECTRON  ////////////////////
	/////////////////////////////////////////////////
	ELECTRON_PROPERTY 		= 'ELECTRON_PROPERTY',
	ELECTRON_CALL 			= 'ELECTRON_CALL',
	ELECTRON_PATH 			= 'ELECTRON_PATH',
	ELECTRON_MODAL_OPEN 	= 'ELECTRON_MODAL_OPEN',
}

type ComunicationInterfaceDefinition =
{
	[key in EComunicationsChannels]: { args: object, return: any; };
};

export type ElectronPath = 'home' | 'appData' | 'userData' | 'cache' | 'temp' | 'exe' | 'module' | 'desktop' | 'documents' | 'downloads' | 'music' | 'pictures' | 'videos' | 'recent' | 'logs' | 'pepperFlashSystemPlugin' | 'crashDumps';
export interface IComunications extends ComunicationInterfaceDefinition
{
	[EComunicationsChannels.ELECTRON_PROPERTY]		: { args: [propertyPath: string[]];								return: string | number | object;			};
	[EComunicationsChannels.ELECTRON_CALL]			: { args: [functionPath: string[], ...args:any[]];				return: string | number | object | null;	};
	[EComunicationsChannels.ELECTRON_PATH]			: { args: [path: ElectronPath]; 								return: string | Error; 					};
	[EComunicationsChannels.ELECTRON_MODAL_OPEN]	: {	args: [options: electron.OpenDialogOptions];				return: electron.OpenDialogReturnValue;		};
}




export enum EMessageContentType
{
	BOOLEAN = 'Boolean',
	NUMBER = 'Number',
	STRING = 'String',
	BUFFER = 'Uint8Array',
	OBJECT = 'Object',
	ARRAY = 'Array',
	ERROR = 'Error',
	NULL = 'Null',
	UNDEFINED = 'Undefined',
}

type MessageContentReturnMapDefinition =
{
	[key in EComunicationsChannels]: any | null;
};

export interface IMessageContentReturnTypeMap extends MessageContentReturnMapDefinition
{
	[EMessageContentType.BOOLEAN] 		: ( value: Boolean )		=> boolean | null;
	[EMessageContentType.NUMBER]		: ( value: Number )			=> number | null;
	[EMessageContentType.STRING]		: ( value: String )			=> string | null;
	[EMessageContentType.BUFFER]		: ( value: Uint8Array )		=> Buffer | null;
	[EMessageContentType.OBJECT]		: ( value: Object )			=> Object | null;
	[EMessageContentType.ARRAY]			: ( value: Array<any> )		=> Array<any> | null;
	[EMessageContentType.ERROR]			: ( value: Error )			=> Error | null;
	[EMessageContentType.NULL]			: ( value: null )			=> null;
	[EMessageContentType.UNDEFINED]		: ( value: undefined )		=> undefined;
}