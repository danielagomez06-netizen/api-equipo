
import { Component, inject, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import Keycloak from 'keycloak-js';

type Pantalla =
  | 'inicio'
  | 'salas'
  | 'revision'
  | 'asignacion'
  | 'mis-cursos'
  | 'mi-horario'
  | 'explicacion';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  private readonly keycloak = inject(Keycloak);

  protected readonly pantalla = signal<Pantalla>('inicio');
  protected readonly menuAbierto = signal(false);

  // Obtiene el nombre del usuario autenticado.
  protected readonly usuario = (): string =>
    (this.keycloak.tokenParsed?.['preferred_username'] as string) ?? '';

  // Comprueba si existe una sesión autenticada.
  protected readonly autenticado = (): boolean =>
    this.keycloak.authenticated === true;

  // Cambia de pantalla y cierra el menú móvil.
  protected cambiarPantalla(nuevaPantalla: Pantalla): void {
    this.pantalla.set(nuevaPantalla);
    this.menuAbierto.set(false);
  }

  // Redirige al inicio de sesión de Keycloak.
  protected async iniciarSesion(): Promise<void> {
    await this.keycloak.login({
      redirectUri: 'http://localhost:4200/',
    });
  }

  // Cierra la sesión y regresa a la aplicación.
  protected async cerrarSesion(): Promise<void> {
    await this.keycloak.logout({
      redirectUri: 'http://localhost:4200/',
    });
  }

  // Obtiene los roles asignados desde el token de Keycloak.
  protected obtenerRoles(): string[] {
    const token = this.keycloak.tokenParsed as
      | {
          realm_access?: {
            roles?: string[];
          };
          resource_access?: {
            [cliente: string]: {
              roles?: string[];
            };
          };
        }
      | undefined;

    const rolesRealm = token?.realm_access?.roles ?? [];

    const rolesCliente =
      token?.resource_access?.['asignador_frontend']?.roles ?? [];

    // Evita devolver roles duplicados.
    return [...new Set([...rolesRealm, ...rolesCliente])];
  }

  // Comprueba si el usuario tiene un rol específico.
  protected tieneRol(rol: string): boolean {
    return this.obtenerRoles().includes(rol);
  }

  // Comprueba si el usuario es coordinador.
  protected esCoordinador(): boolean {
    return this.tieneRol('COORDINADOR');
  }

  // Comprueba si el usuario es docente.
  protected esDocente(): boolean {
    return this.tieneRol('DOCENTE');
  }
}
