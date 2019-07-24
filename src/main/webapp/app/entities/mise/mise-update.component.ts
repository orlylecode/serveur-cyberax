import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IMise, Mise } from 'app/shared/model/mise.model';
import { MiseService } from './mise.service';
import { IJoueur } from 'app/shared/model/joueur.model';
import { JoueurService } from 'app/entities/joueur';
import { IJeu } from 'app/shared/model/jeu.model';
import { JeuService } from 'app/entities/jeu';
import { IListAttente } from 'app/shared/model/list-attente.model';
import { ListAttenteService } from 'app/entities/list-attente';

@Component({
  selector: 'jhi-mise-update',
  templateUrl: './mise-update.component.html'
})
export class MiseUpdateComponent implements OnInit {
  isSaving: boolean;

  joueurs: IJoueur[];

  jeus: IJeu[];

  listattentes: IListAttente[];

  editForm = this.fb.group({
    id: [],
    montant: [],
    dateMise: [],
    dateValidation: [],
    etat: [],
    positionClic: [],
    joueur: [],
    jeu: [],
    listAttente: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected miseService: MiseService,
    protected joueurService: JoueurService,
    protected jeuService: JeuService,
    protected listAttenteService: ListAttenteService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ mise }) => {
      this.updateForm(mise);
    });
    this.joueurService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IJoueur[]>) => mayBeOk.ok),
        map((response: HttpResponse<IJoueur[]>) => response.body)
      )
      .subscribe((res: IJoueur[]) => (this.joueurs = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.jeuService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IJeu[]>) => mayBeOk.ok),
        map((response: HttpResponse<IJeu[]>) => response.body)
      )
      .subscribe((res: IJeu[]) => (this.jeus = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.listAttenteService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IListAttente[]>) => mayBeOk.ok),
        map((response: HttpResponse<IListAttente[]>) => response.body)
      )
      .subscribe((res: IListAttente[]) => (this.listattentes = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(mise: IMise) {
    this.editForm.patchValue({
      id: mise.id,
      montant: mise.montant,
      dateMise: mise.dateMise != null ? mise.dateMise.format(DATE_TIME_FORMAT) : null,
      dateValidation: mise.dateValidation != null ? mise.dateValidation.format(DATE_TIME_FORMAT) : null,
      etat: mise.etat,
      positionClic: mise.positionClic,
      joueur: mise.joueur,
      jeu: mise.jeu,
      listAttente: mise.listAttente
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const mise = this.createFromForm();
    if (mise.id !== undefined) {
      this.subscribeToSaveResponse(this.miseService.update(mise));
    } else {
      this.subscribeToSaveResponse(this.miseService.create(mise));
    }
  }

  private createFromForm(): IMise {
    return {
      ...new Mise(),
      id: this.editForm.get(['id']).value,
      montant: this.editForm.get(['montant']).value,
      dateMise: this.editForm.get(['dateMise']).value != null ? moment(this.editForm.get(['dateMise']).value, DATE_TIME_FORMAT) : undefined,
      dateValidation:
        this.editForm.get(['dateValidation']).value != null
          ? moment(this.editForm.get(['dateValidation']).value, DATE_TIME_FORMAT)
          : undefined,
      etat: this.editForm.get(['etat']).value,
      positionClic: this.editForm.get(['positionClic']).value,
      joueur: this.editForm.get(['joueur']).value,
      jeu: this.editForm.get(['jeu']).value,
      listAttente: this.editForm.get(['listAttente']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMise>>) {
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

  trackListAttenteById(index: number, item: IListAttente) {
    return item.id;
  }
}
