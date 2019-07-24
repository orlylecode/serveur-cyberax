import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IListAttente, ListAttente } from 'app/shared/model/list-attente.model';
import { ListAttenteService } from './list-attente.service';

@Component({
  selector: 'jhi-list-attente-update',
  templateUrl: './list-attente-update.component.html'
})
export class ListAttenteUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    version: [],
    dateCreation: []
  });

  constructor(protected listAttenteService: ListAttenteService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ listAttente }) => {
      this.updateForm(listAttente);
    });
  }

  updateForm(listAttente: IListAttente) {
    this.editForm.patchValue({
      id: listAttente.id,
      version: listAttente.version,
      dateCreation: listAttente.dateCreation
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const listAttente = this.createFromForm();
    if (listAttente.id !== undefined) {
      this.subscribeToSaveResponse(this.listAttenteService.update(listAttente));
    } else {
      this.subscribeToSaveResponse(this.listAttenteService.create(listAttente));
    }
  }

  private createFromForm(): IListAttente {
    return {
      ...new ListAttente(),
      id: this.editForm.get(['id']).value,
      version: this.editForm.get(['version']).value,
      dateCreation: this.editForm.get(['dateCreation']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IListAttente>>) {
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
