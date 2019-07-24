import { IMise } from 'app/shared/model/mise.model';

export interface IListAttente {
  id?: number;
  version?: number;
  dateCreation?: number;
  mises?: IMise[];
}

export class ListAttente implements IListAttente {
  constructor(public id?: number, public version?: number, public dateCreation?: number, public mises?: IMise[]) {}
}
