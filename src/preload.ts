import { contextBridge, ipcRenderer } from 'electron';

contextBridge.exposeInMainWorld('electronAPI',
{
  sendFormData: (data: any) => ipcRenderer.send('form-data', data)
});
