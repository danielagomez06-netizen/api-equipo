import {
  ApplicationConfig,
  provideBrowserGlobalErrorListeners,
} from '@angular/core';

import { provideRouter } from '@angular/router';
import { provideKeycloak } from 'keycloak-angular';
import { routes } from './app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideKeycloak({
      config: {
        url: 'http://localhost:8080',
        realm: 'asignador_salas',
        clientId: 'asignador_frontend',
      },
      initOptions: {
        onLoad: 'check-sso',
        pkceMethod: 'S256',
        checkLoginIframe: false,
      },
    }),
  ],
};
