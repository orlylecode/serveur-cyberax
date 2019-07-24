import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IListAttente } from 'app/shared/model/list-attente.model';

@Component({
  selector: 'jhi-list-attente-detail',
  templateUrl: './list-attente-detail.component.html'
})
export class ListAttenteDetailComponent implements OnInit {
  listAttente: IListAttente;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ listAttente }) => {
      this.listAttente = listAttente;
    });
  }

  previousState() {
    window.history.back();
  }
}
