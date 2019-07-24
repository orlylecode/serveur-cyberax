import { Moment } from 'moment';
import { IJoueur } from 'app/shared/model/joueur.model';
import { IJeu } from 'app/shared/model/jeu.model';
import { IListAttente } from 'app/shared/model/list-attente.model';

export interface IMise {
  id?: number;
  montant?: number;
  dateMise?: Moment;
  dateValidation?: Moment;
  etat?: number;
  positionClic?: number;
  joueur?: IJoueur;
  jeu?: IJeu;
  listAttente?: IListAttente;
}

export class Mise implements IMise {
  constructor(
    public id?: number,
    public montant?: number,
    public dateMise?: Moment,
    public dateValidation?: Moment,
    public etat?: number,
    public positionClic?: number,
    public joueur?: IJoueur,
    public jeu?: IJeu,
    public listAttente?: IListAttente
  ) {}
}
