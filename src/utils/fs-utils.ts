import * as fs from 'fs';
import * as path from 'path';
import { Yieldable } from './genericUtils';


export default class FSUtils
{
	/**  */
	public static GetUserDataFolder(): string
	{
		return process.env.APPDATA || path.join(process.env.HOME!, process.platform === 'darwin' ? '/Library/Preferences' : "/.local/share");
	}

	/**  */
	public static async MakeDirectoryAsync(dirPath: string): Promise<boolean>
	{
		if ((FSUtils.IsDirectorySafe(dirPath))) return true;
		return new Promise((resolve) =>
		{
			fs.mkdir(dirPath, (err: NodeJS.ErrnoException | null, path?: string) => resolve(!err));
		});
	}

	/**  */
	public static IsDirectorySafe(directoryPath: string): boolean
	{
		let result = false;
		try { result = fs.lstatSync(directoryPath).isDirectory(); } catch (e) { }
		return result;
	}


	/** Ensure that a folder exist, creating all directory tree if needed */
	public static async EnsureDirectoryExistence(filePath: string): Promise<void>
	{
		const dirNames: string[] = path.normalize(filePath).split(path.sep).filter(p => p);
		for (let index = 0; index < dirNames.length; index++)
		{
			await Yieldable(() =>
			{
				const dirPath = dirNames.slice(0, index + 1).join(path.sep);
				return FSUtils.MakeDirectoryAsync(dirPath);
			});
		};
	}
}