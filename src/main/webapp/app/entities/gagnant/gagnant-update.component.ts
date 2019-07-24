import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IGagnant, Gagnant } from 'app/shared/model/gagnant.model';
import { GagnantService } from './gagnant.service';
import { IJeu } from 'app/shared/model/jeu.model';
import { JeuService } from 'app/entities/jeu';

@Component({
  selector: 'jhi-gagnant-update',
  templateUrl: './gagnant-update.component.html'
})
export class GagnantUpdateComponent implements OnInit {
  isSaving: boolean;

  jeus: IJeu[];

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    telephone: [],
    position: [],
    dateGain: [],
    datePayment: [],
    jeu: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected gagnantService: GagnantService,
    protected jeuService: JeuService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ gagnant }) => {
      this.updateForm(gagnant);
    });
    this.jeuService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IJeu[]>) => mayBeOk.ok),
        map((response: HttpResponse<IJeu[]>) => response.body)
      )
      .subscribe((res: IJeu[]) => (this.jeus = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(gagnant: IGagnant) {
    this.editForm.patchValue({
      id: gagnant.id,
      nom: gagnant.nom,
      prenom: gagnant.prenom,
      telephone: gagnant.telephone,
      position: gagnant.position,
      dateGain: gagnant.dateGain != null ? gagnant.dateGain.format(DATE_TIME_FORMAT) : null,
      datePayment: gagnant.datePayment != null ? gagnant.datePayment.format(DATE_TIME_FORMAT) : null,
      jeu: gagnant.jeu
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const gagnant = this.createFromForm();
    if (gagnant.id !== undefined) {
      this.subscribeToSaveResponse(this.gagnantService.update(gagnant));
    } else {
      this.subscribeToSaveResponse(this.gagnantService.create(gagnant));
    }
  }

  private createFromForm(): IGagnant {
    return {
      ...new Gagnant(),
      id: this.editForm.get(['id']).value,
      nom: this.editForm.get(['nom']).value,
      prenom: this.editForm.get(['prenom']).value,
      telephone: this.editForm.get(['telephone']).value,
      position: this.editForm.get(['position']).value,
      dateGain: this.editForm.get(['dateGain']).value != null ? moment(this.editForm.get(['dateGain']).value, DATE_TIME_FORMAT) : undefined,
      datePayment:
        this.editForm.get(['datePayment']).value != null ? moment(this.editForm.get(['datePayment']).value, DATE_TIME_FORMAT) : undefined,
      jeu: this.editForm.get(['jeu']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGagnant>>) {
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

  trackJeuById(index: number, item: IJeu) {
    return item.id;
  }
}
