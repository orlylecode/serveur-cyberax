import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IJeu } from 'app/shared/model/jeu.model';

@Component({
  selector: 'jhi-jeu-detail',
  templateUrl: './jeu-detail.component.html'
})
export class JeuDetailComponent implements OnInit {
  jeu: IJeu;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ jeu }) => {
      this.jeu = jeu;
    });
  }

  previousState() {
    window.history.back();
  }
}
