import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGagnant } from 'app/shared/model/gagnant.model';
import { GagnantService } from './gagnant.service';

@Component({
  selector: 'jhi-gagnant-delete-dialog',
  templateUrl: './gagnant-delete-dialog.component.html'
})
export class GagnantDeleteDialogComponent {
  gagnant: IGagnant;

  constructor(protected gagnantService: GagnantService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.gagnantService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'gagnantListModification',
        content: 'Deleted an gagnant'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-gagnant-delete-popup',
  template: ''
})
export class GagnantDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ gagnant }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(GagnantDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.gagnant = gagnant;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/gagnant', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/gagnant', { outlets: { popup: null } }]);
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
