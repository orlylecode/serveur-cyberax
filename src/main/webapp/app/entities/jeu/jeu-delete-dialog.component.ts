import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IJeu } from 'app/shared/model/jeu.model';
import { JeuService } from './jeu.service';

@Component({
  selector: 'jhi-jeu-delete-dialog',
  templateUrl: './jeu-delete-dialog.component.html'
})
export class JeuDeleteDialogComponent {
  jeu: IJeu;

  constructor(protected jeuService: JeuService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.jeuService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'jeuListModification',
        content: 'Deleted an jeu'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-jeu-delete-popup',
  template: ''
})
export class JeuDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ jeu }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(JeuDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.jeu = jeu;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/jeu', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/jeu', { outlets: { popup: null } }]);
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
