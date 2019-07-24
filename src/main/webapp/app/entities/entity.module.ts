import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'joueur',
        loadChildren: './joueur/joueur.module#ServeurCyberaxJoueurModule'
      },
      {
        path: 'mise',
        loadChildren: './mise/mise.module#ServeurCyberaxMiseModule'
      },
      {
        path: 'jeu',
        loadChildren: './jeu/jeu.module#ServeurCyberaxJeuModule'
      },
      {
        path: 'gagnant',
        loadChildren: './gagnant/gagnant.module#ServeurCyberaxGagnantModule'
      },
      {
        path: 'list-attente',
        loadChildren: './list-attente/list-attente.module#ServeurCyberaxListAttenteModule'
      },
      {
        path: 'terminal',
        loadChildren: './terminal/terminal.module#ServeurCyberaxTerminalModule'
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServeurCyberaxEntityModule {}
