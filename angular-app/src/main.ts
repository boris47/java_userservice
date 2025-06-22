/// <reference types="@angular/localize" />

import { bootstrapApplication } from '@angular/platform-browser';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { provideBrowserGlobalErrorListeners, provideZonelessChangeDetection } from '@angular/core';

import InitializeRenderer from './app/services/ipc';
import RootComponent from './app/root.component';
import { routes } from './routes';


InitializeRenderer()

.then(() =>
{
	bootstrapApplication(RootComponent, {
		providers: [
			provideRouter(routes, withComponentInputBinding()),
			provideBrowserGlobalErrorListeners(),
			provideZonelessChangeDetection(),
		]
	})
	.catch(err => console.error(err));
})

.catch(function (error: Error)
{
	alert(error);
	close();
});



