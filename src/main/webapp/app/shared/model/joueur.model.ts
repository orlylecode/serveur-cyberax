import { IMise } from 'app/shared/model/mise.model';

export interface IJoueur {
  id?: number;
  nom?: string;
  prenom?: string;
  telephone?: string;
  mises?: IMise[];
}

export class Joueur implements IJoueur {
  constructor(public id?: number, public nom?: string, public prenom?: string, public telephone?: string, public mises?: IMise[]) {}
}
