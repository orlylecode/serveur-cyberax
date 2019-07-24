import { Moment } from 'moment';
import { IJeu } from 'app/shared/model/jeu.model';

export interface IGagnant {
  id?: number;
  nom?: string;
  prenom?: string;
  telephone?: string;
  position?: number;
  dateGain?: Moment;
  datePayment?: Moment;
  jeu?: IJeu;
}

export class Gagnant implements IGagnant {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public telephone?: string,
    public position?: number,
    public dateGain?: Moment,
    public datePayment?: Moment,
    public jeu?: IJeu
  ) {}
}
