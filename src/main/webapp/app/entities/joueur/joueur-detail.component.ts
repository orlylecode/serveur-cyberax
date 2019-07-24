import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJoueur } from 'app/shared/model/joueur.model';

@Component({
  selector: 'jhi-joueur-detail',
  templateUrl: './joueur-detail.component.html'
})
export class JoueurDetailComponent implements OnInit {
  joueur: IJoueur;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ joueur }) => {
      this.joueur = joueur;
    });
  }

  previousState() {
    window.history.back();
  }
}
