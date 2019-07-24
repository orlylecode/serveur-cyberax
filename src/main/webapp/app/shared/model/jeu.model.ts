import { Moment } from 'moment';
import { IMise } from 'app/shared/model/mise.model';
import { IGagnant } from 'app/shared/model/gagnant.model';

export interface IJeu {
  id?: number;
  dateCreation?: Moment;
  dateLancement?: Moment;
  dateCloture?: Moment;
  encour?: boolean;
  pourcentageMise?: number;
  pourcentageRebourt?: number;
  mises?: IMise[];
  gagnants?: IGagnant[];
}

export class Jeu implements IJeu {
  constructor(
    public id?: number,
    public dateCreation?: Moment,
    public dateLancement?: Moment,
    public dateCloture?: Moment,
    public encour?: boolean,
    public pourcentageMise?: number,
    public pourcentageRebourt?: number,
    public mises?: IMise[],
    public gagnants?: IGagnant[]
  ) {
    this.encour = this.encour || false;
  }
}
