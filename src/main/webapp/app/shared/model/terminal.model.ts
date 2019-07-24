import { IJoueur } from 'app/shared/model/joueur.model';
import { IJeu } from 'app/shared/model/jeu.model';

export interface ITerminal {
  id?: number;
  message?: string;
  joueur?: IJoueur;
  jeuxEncour?: IJeu;
  jeuPrecedent?: IJeu;
}

export class Terminal implements ITerminal {
  constructor(public id?: number, public message?: string, public joueur?: IJoueur, public jeuxEncour?: IJeu, public jeuPrecedent?: IJeu) {}
}
