import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IJoueur, Joueur } from 'app/shared/model/joueur.model';
import { JoueurService } from './joueur.service';

@Component({
  selector: 'jhi-joueur-update',
  templateUrl: './joueur-update.component.html'
})
export class JoueurUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    nom: [],
    prenom: [],
    telephone: []
  });

  constructor(protected joueurService: JoueurService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ joueur }) => {
      this.updateForm(joueur);
    });
  }

  updateForm(joueur: IJoueur) {
    this.editForm.patchValue({
      id: joueur.id,
      nom: joueur.nom,
      prenom: joueur.prenom,
      telephone: joueur.telephone
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const joueur = this.createFromForm();
    if (joueur.id !== undefined) {
      this.subscribeToSaveResponse(this.joueurService.update(joueur));
    } else {
      this.subscribeToSaveResponse(this.joueurService.create(joueur));
    }
  }

  private createFromForm(): IJoueur {
    return {
      ...new Joueur(),
      id: this.editForm.get(['id']).value,
      nom: this.editForm.get(['nom']).value,
      prenom: this.editForm.get(['prenom']).value,
      telephone: this.editForm.get(['telephone']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IJoueur>>) {
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
