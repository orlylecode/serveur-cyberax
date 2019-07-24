import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITerminal } from 'app/shared/model/terminal.model';

@Component({
  selector: 'jhi-terminal-detail',
  templateUrl: './terminal-detail.component.html'
})
export class TerminalDetailComponent implements OnInit {
  terminal: ITerminal;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ terminal }) => {
      this.terminal = terminal;
    });
  }

  previousState() {
    window.history.back();
  }
}
