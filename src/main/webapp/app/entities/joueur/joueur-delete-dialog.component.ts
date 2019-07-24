import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJoueur } from 'app/shared/model/joueur.model';
import { JoueurService } from './joueur.service';

@Component({
  selector: 'jhi-joueur-delete-dialog',
  templateUrl: './joueur-delete-dialog.component.html'
})
export class JoueurDeleteDialogComponent {
  joueur: IJoueur;

  constructor(protected joueurService: JoueurService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.joueurService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'joueurListModification',
        content: 'Deleted an joueur'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-joueur-delete-popup',
  template: ''
})
export class JoueurDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ joueur }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(JoueurDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.joueur = joueur;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/joueur', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/joueur', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
