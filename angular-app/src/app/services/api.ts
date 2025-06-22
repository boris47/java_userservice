import axios, { type Axios } from 'axios';

import type ILoginCredentials from '@shared/models/login-credentials.model';
import type ILoginReturn from '@shared/models/login-return.model';

class _AxiosInstance
{
	private axiosInstance: Axios;
	
	constructor(baseURL: string)
	{
		this.axiosInstance = axios.create(
		{
			baseURL, withCredentials: true,
			timeout: 30 * 1000,
			headers: { "Content-Type": "application/json" }
		});
	}
	
	public async TryLogin({ username, password }: ILoginCredentials): Promise<ILoginReturn>
	{
		const response = await axios.post("/login", undefined,
		{
			baseURL: this.axiosInstance.defaults.baseURL,
			auth: { username, password },
			withCredentials: true
		});
		return response.data;
	}
}

const AxiosInstance = new _AxiosInstance('http://localhost:8081/api');

export default AxiosInstance;