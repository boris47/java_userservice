import { app, BrowserWindow } from 'electron';
import * as path from 'path';
import * as url from 'url';

function createWindow()
{
    const win = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            contextIsolation: true,
            preload: path.join(__dirname, 'preload.js')
        }
    });

    const indexPath = path.join(
        app.getAppPath(),
        'angular-app',
        'dist',
        'browser',
        'index.html'
    );

    win.loadURL(url.pathToFileURL(indexPath).toString());
}

app.whenReady().then(createWindow);

app.on('window-all-closed', () =>
{
    if (process.platform !== 'darwin')
    {
        app.quit();
    }
});







import { ipcMain } from 'electron';

ipcMain.on('form-data', (event, data) =>
{
    console.log('Dati ricevuti dal form:', data);
    // qui puoi salvarli, scrivere su file, ecc.
});
