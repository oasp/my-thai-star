import { BackendType } from '../app/config';

export const environment: {production: boolean, backendType: BackendType, restPathRoot: string, restServiceRoot: string} = {
  production: true,
  backendType: BackendType.REST,
  restPathRoot: 'api/',
  restServiceRoot: 'api/services/rest/',
};