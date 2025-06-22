
import type { ICP_Service as ICP_Service_ } from '@services/ipc';
import { EComunicationsChannels } from '@shared/icpComs';

declare global
{
	/**
	 * The Comunication Protocol betweeen renderer and electron main process
	 */
	const ICP_Service: typeof ICP_Service_;
	
	/**
	 * The registered channels to be used for ICP_Service
	 */
	const ComunicationChannels: typeof EComunicationsChannels;
}


export { }; // This makes sure the file is treated as a module, avoiding global pollution