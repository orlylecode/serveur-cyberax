import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMise } from 'app/shared/model/mise.model';

@Component({
  selector: 'jhi-mise-detail',
  templateUrl: './mise-detail.component.html'
})
export class MiseDetailComponent implements OnInit {
  mise: IMise;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ mise }) => {
      this.mise = mise;
    });
  }

  previousState() {
    window.history.back();
  }
}
