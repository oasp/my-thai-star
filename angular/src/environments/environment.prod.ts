import { BackendType } from '../app/config';

export const environment: {production: boolean, backendType: BackendType, restPathRoot: string, restServiceRoot: string} = {
  production: true,
  backendType: BackendType.REST,
  restPathRoot: 'http://localhost:8081/mythaistar/',
  restServiceRoot: 'http://localhost:8081/mythaistar/services/rest/',
};