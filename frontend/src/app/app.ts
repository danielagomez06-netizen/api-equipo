
import { Component, inject, signal } from '@angular/core';
import Keycloak from 'keycloak-js';

type Pantalla =
  | 'inicio'
  | 'programas'
  | 'salas'
  | 'franjas'
  | 'docentes'
  | 'cursos'
  | 'preferencias'
  | 'preparar'
  | 'resultados'
  | 'conflictos'
  | 'revision'
  | 'asignacion'
  | 'mis-cursos'
  | 'mi-horario'
  | 'explicacion';

@Component({
  selector: 'app-root',
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  private readonly keycloak = inject(Keycloak);

  protected readonly pantalla = signal<Pantalla>('inicio');
  protected readonly menuAbierto = signal(false);
  protected readonly semestre = signal('2026-2');
  protected readonly diasConfigurados = signal<'laborables' | 'sabado' | 'personalizado'>('laborables');
  protected readonly diasPersonalizados = signal<string[]>([
    'Lunes',
    'Martes',
    'Miércoles',
    'Jueves',
    'Viernes',
  ]);
  protected readonly vistaCalendarioDias = signal(false);
  protected readonly inicio = signal('07:00');
  protected readonly fin = signal('19:00');
  protected readonly duracion = signal('2');
  protected readonly salaSeleccionada = signal('LAB-204');
  protected readonly aplicacion = signal('Todas las semanas de 2026-2');
  protected readonly guardado = signal(false);
  protected readonly bloqueos = signal<Record<string, string>>({
    'Miércoles-3': 'Mantenimiento preventivo',
  });
  protected readonly diaSeleccionado = signal('Miércoles');
  protected readonly franjaSeleccionada = signal(3);

  private readonly diasLaborables = [
    'Lunes',
    'Martes',
    'Miércoles',
    'Jueves',
    'Viernes',
  ];

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

  protected diasVisibles(): string[] {
    if (this.diasConfigurados() === 'personalizado') {
      return this.diasPersonalizados();
    }

    return this.diasConfigurados() === 'sabado'
      ? [...this.diasLaborables, 'Sábado']
      : this.diasLaborables;
  }

  protected franjasHorario(): string[] {
    const horaInicio = Number(this.inicio().slice(0, 2));
    const horaFin = Number(this.fin().slice(0, 2));
    const horasPorFranja = Number(this.duracion());
    const franjas: string[] = [];

    for (
      let hora = horaInicio;
      hora + horasPorFranja <= horaFin;
      hora += horasPorFranja
    ) {
      franjas.push(
        `${this.formatearHora(hora)}-${this.formatearHora(hora + horasPorFranja)}`,
      );
    }

    return franjas;
  }

  protected totalFranjas(): number {
    return this.diasVisibles().length * this.franjasHorario().length;
  }

  protected totalBloqueadas(): number {
    return this.diasVisibles().reduce(
      (total, dia) =>
        total +
        this.franjasHorario().filter((_, indice) =>
          Boolean(this.bloqueos()[`${dia}-${indice}`]),
        ).length,
      0,
    );
  }

  protected estadoFranja(dia: string, indice: number): string | undefined {
    return this.bloqueos()[`${dia}-${indice}`];
  }

  protected horaSeleccionada(): string {
    return (
      this.franjasHorario()[this.franjaSeleccionada()] ??
      this.franjasHorario()[0] ??
      'Sin franjas'
    );
  }

  protected seleccionarFranja(dia: string, indice: number): void {
    this.diaSeleccionado.set(dia);
    this.franjaSeleccionada.set(indice);
    this.guardado.set(false);
  }

  protected alternarFranja(dia: string, indice: number): void {
    this.seleccionarFranja(dia, indice);
    const clave = `${dia}-${indice}`;
    const nuevosBloqueos = { ...this.bloqueos() };

    if (nuevosBloqueos[clave]) {
      delete nuevosBloqueos[clave];
    } else {
      nuevosBloqueos[clave] = 'Bloqueo manual';
    }

    this.bloqueos.set(nuevosBloqueos);
  }

  protected actualizarSemestre(evento: Event): void {
    const valor = this.valorSeleccionado(evento);
    this.semestre.set(valor);
    this.aplicacion.set(`Todas las semanas de ${valor}`);
    this.guardado.set(false);
  }

  protected actualizarDias(evento: Event): void {
    const valor = this.valorSeleccionado(evento) as 'laborables' | 'sabado' | 'personalizado';
    this.diasConfigurados.set(valor);
    this.guardado.set(false);

    if (valor === 'personalizado' && this.diasPersonalizados().length === 0) {
      this.diasPersonalizados.set([...this.diasLaborables]);
    }
  }

  protected alternarDiaHabilitado(dia: string): void {
    const actuales = [...this.diasPersonalizados()];
    const existe = actuales.includes(dia);

    const siguiente = existe
      ? actuales.filter((item) => item !== dia)
      : [...actuales, dia];

    this.diasPersonalizados.set(siguiente);
    this.guardado.set(false);
  }

  protected abrirCalendarioDias(): void {
    this.vistaCalendarioDias.set(true);
  }

  protected cerrarCalendarioDias(): void {
    this.vistaCalendarioDias.set(false);
  }

  protected actualizarInicio(evento: Event): void {
    this.inicio.set(this.valorSeleccionado(evento));
    this.guardado.set(false);
  }

  protected actualizarFin(evento: Event): void {
    this.fin.set(this.valorSeleccionado(evento));
    this.guardado.set(false);
  }

  protected actualizarDuracion(evento: Event): void {
    this.duracion.set(this.valorSeleccionado(evento));
    this.guardado.set(false);
  }

  protected actualizarSala(evento: Event): void {
    this.salaSeleccionada.set(this.valorSeleccionado(evento));
    this.guardado.set(false);
  }

  protected actualizarAplicacion(evento: Event): void {
    this.aplicacion.set(this.valorSeleccionado(evento));
    this.guardado.set(false);
  }

  protected guardarConfiguracion(): void {
    this.guardado.set(true);
  }

  protected restablecerConfiguracion(): void {
    this.semestre.set('2026-2');
    this.diasConfigurados.set('laborables');
    this.diasPersonalizados.set([...this.diasLaborables]);
    this.vistaCalendarioDias.set(false);
    this.inicio.set('07:00');
    this.fin.set('19:00');
    this.duracion.set('2');
    this.salaSeleccionada.set('LAB-204');
    this.aplicacion.set('Todas las semanas de 2026-2');
    this.bloqueos.set({ 'Miércoles-3': 'Mantenimiento preventivo' });
    this.diaSeleccionado.set('Miércoles');
    this.franjaSeleccionada.set(3);
    this.guardado.set(false);
  }

  private valorSeleccionado(evento: Event): string {
    const elemento = evento.target;
    return elemento instanceof HTMLSelectElement ? elemento.value : '';
  }

  private formatearHora(hora: number): string {
    return `${String(hora).padStart(2, '0')}:00`;
  }

  // Redirige al inicio de sesión de Keycloak.
  protected async iniciarSesion(): Promise<void> {
    await this.keycloak.login({
      redirectUri: 'http://localhost:4201/',
    });
  }

  // Cierra la sesión y regresa a la aplicación.
  protected async cerrarSesion(): Promise<void> {
    await this.keycloak.logout({
      redirectUri: 'http://localhost:4201/',
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
