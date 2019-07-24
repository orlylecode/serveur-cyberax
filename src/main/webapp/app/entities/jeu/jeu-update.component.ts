import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IJeu, Jeu } from 'app/shared/model/jeu.model';
import { JeuService } from './jeu.service';

@Component({
  selector: 'jhi-jeu-update',
  templateUrl: './jeu-update.component.html'
})
export class JeuUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    dateCreation: [],
    dateLancement: [],
    dateCloture: [],
    encour: [],
    pourcentageMise: [],
    pourcentageRebourt: []
  });

  constructor(protected jeuService: JeuService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ jeu }) => {
      this.updateForm(jeu);
    });
  }

  updateForm(jeu: IJeu) {
    this.editForm.patchValue({
      id: jeu.id,
      dateCreation: jeu.dateCreation != null ? jeu.dateCreation.format(DATE_TIME_FORMAT) : null,
      dateLancement: jeu.dateLancement != null ? jeu.dateLancement.format(DATE_TIME_FORMAT) : null,
      dateCloture: jeu.dateCloture != null ? jeu.dateCloture.format(DATE_TIME_FORMAT) : null,
      encour: jeu.encour,
      pourcentageMise: jeu.pourcentageMise,
      pourcentageRebourt: jeu.pourcentageRebourt
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const jeu = this.createFromForm();
    if (jeu.id !== undefined) {
      this.subscribeToSaveResponse(this.jeuService.update(jeu));
    } else {
      this.subscribeToSaveResponse(this.jeuService.create(jeu));
    }
  }

  private createFromForm(): IJeu {
    return {
      ...new Jeu(),
      id: this.editForm.get(['id']).value,
      dateCreation:
        this.editForm.get(['dateCreation']).value != null ? moment(this.editForm.get(['dateCreation']).value, DATE_TIME_FORMAT) : undefined,
      dateLancement:
        this.editForm.get(['dateLancement']).value != null
          ? moment(this.editForm.get(['dateLancement']).value, DATE_TIME_FORMAT)
          : undefined,
      dateCloture:
        this.editForm.get(['dateCloture']).value != null ? moment(this.editForm.get(['dateCloture']).value, DATE_TIME_FORMAT) : undefined,
      encour: this.editForm.get(['encour']).value,
      pourcentageMise: this.editForm.get(['pourcentageMise']).value,
      pourcentageRebourt: this.editForm.get(['pourcentageRebourt']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJeu>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
