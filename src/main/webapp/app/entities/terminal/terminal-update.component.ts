import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ITerminal, Terminal } from 'app/shared/model/terminal.model';
import { TerminalService } from './terminal.service';
import { IJoueur } from 'app/shared/model/joueur.model';
import { JoueurService } from 'app/entities/joueur';
import { IJeu } from 'app/shared/model/jeu.model';
import { JeuService } from 'app/entities/jeu';

@Component({
  selector: 'jhi-terminal-update',
  templateUrl: './terminal-update.component.html'
})
export class TerminalUpdateComponent implements OnInit {
  isSaving: boolean;

  joueurs: IJoueur[];

  jeuxencours: IJeu[];

  jeuprecedents: IJeu[];

  editForm = this.fb.group({
    id: [],
    message: [],
    joueur: [],
    jeuxEncour: [],
    jeuPrecedent: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected terminalService: TerminalService,
    protected joueurService: JoueurService,
    protected jeuService: JeuService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ terminal }) => {
      this.updateForm(terminal);
    });
    this.joueurService
      .query({ filter: 'terminal-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IJoueur[]>) => mayBeOk.ok),
        map((response: HttpResponse<IJoueur[]>) => response.body)
      )
      .subscribe(
        (res: IJoueur[]) => {
          if (!this.editForm.get('joueur').value || !this.editForm.get('joueur').value.id) {
            this.joueurs = res;
          } else {
            this.joueurService
              .find(this.editForm.get('joueur').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IJoueur>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IJoueur>) => subResponse.body)
              )
              .subscribe(
                (subRes: IJoueur) => (this.joueurs = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.jeuService
      .query({ filter: 'terminal-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IJeu[]>) => mayBeOk.ok),
        map((response: HttpResponse<IJeu[]>) => response.body)
      )
      .subscribe(
        (res: IJeu[]) => {
          if (!this.editForm.get('jeuxEncour').value || !this.editForm.get('jeuxEncour').value.id) {
            this.jeuxencours = res;
          } else {
            this.jeuService
              .find(this.editForm.get('jeuxEncour').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IJeu>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IJeu>) => subResponse.body)
              )
              .subscribe(
                (subRes: IJeu) => (this.jeuxencours = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.jeuService
      .query({ filter: 'terminal-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IJeu[]>) => mayBeOk.ok),
        map((response: HttpResponse<IJeu[]>) => response.body)
      )
      .subscribe(
        (res: IJeu[]) => {
          if (!this.editForm.get('jeuPrecedent').value || !this.editForm.get('jeuPrecedent').value.id) {
            this.jeuprecedents = res;
          } else {
            this.jeuService
              .find(this.editForm.get('jeuPrecedent').value.id)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IJeu>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IJeu>) => subResponse.body)
              )
              .subscribe(
                (subRes: IJeu) => (this.jeuprecedents = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  updateForm(terminal: ITerminal) {
    this.editForm.patchValue({
      id: terminal.id,
      message: terminal.message,
      joueur: terminal.joueur,
      jeuxEncour: terminal.jeuxEncour,
      jeuPrecedent: terminal.jeuPrecedent
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const terminal = this.createFromForm();
    if (terminal.id !== undefined) {
      this.subscribeToSaveResponse(this.terminalService.update(terminal));
    } else {
      this.subscribeToSaveResponse(this.terminalService.create(terminal));
    }
  }

  private createFromForm(): ITerminal {
    return {
      ...new Terminal(),
      id: this.editForm.get(['id']).value,
      message: this.editForm.get(['message']).value,
      joueur: this.editForm.get(['joueur']).value,
      jeuxEncour: this.editForm.get(['jeuxEncour']).value,
      jeuPrecedent: this.editForm.get(['jeuPrecedent']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITerminal>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackJoueurById(index: number, item: IJoueur) {
    return item.id;
  }

  trackJeuById(index: number, item: IJeu) {
    return item.id;
  }
}
