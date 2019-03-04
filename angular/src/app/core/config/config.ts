export enum BackendType {
  IN_MEMORY,
  REST,
  GRAPHQL,
}

interface Role {
  name: string;
  permission: number;
}

interface Lang {
  label: string;
  value: string;
}

export interface Config {
  version: string;
  backendType: BackendType;
  restPathRoot: string;
  restServiceRoot: string;
  pageSizes: number[];
  pageSizesDialog: number[];
  roles: Role[];
  langs: Lang[];
}

export const config: Config = {
  version: 'dev',
  backendType: BackendType.REST,
  restPathRoot: 'http://localhost:8081/mythaistar/',
  restServiceRoot: 'http://localhost:8081/mythaistar/services/rest/',
  pageSizes: [8, 16, 24],
  pageSizesDialog: [4, 8, 12],
  roles: [
    { name: 'CUSTOMER', permission: 0 },
    { name: 'WAITER', permission: 1 },
  ],
  langs: [
    { label: 'English', value: 'en' },
    { label: 'Deutsch', value: 'de' },
    { label: 'Español', value: 'es' },
    { label: 'Català', value: 'ca' },
    { label: 'Français', value: 'fr' },
    { label: 'Nederlands', value: 'nl' },
    { label: 'हिन्दी', value: 'hi' },
    { label: 'Polski', value: 'pl' },
    { label: 'Русский', value: 'ru' },
    { label: 'български', value: 'bg' },
  ],
};
