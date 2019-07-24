import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGagnant } from 'app/shared/model/gagnant.model';

@Component({
  selector: 'jhi-gagnant-detail',
  templateUrl: './gagnant-detail.component.html'
})
export class GagnantDetailComponent implements OnInit {
  gagnant: IGagnant;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ gagnant }) => {
      this.gagnant = gagnant;
    });
  }

  previousState() {
    window.history.back();
  }
}
