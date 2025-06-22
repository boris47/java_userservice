import { contextBridge, ipcRenderer } from 'electron';

contextBridge.exposeInMainWorld('ICP_RendererInterface',
{
	notify: ( channel: string, ...data: any[] ) => ipcRenderer.send(channel, ...data),
	
	request: async ( channel: string, ...data: any[] ) => await ipcRenderer.invoke(channel, ...data),
});

contextBridge.exposeInMainWorld("env", 
{
	NODE_ENV: process.env.NODE_ENV
});
