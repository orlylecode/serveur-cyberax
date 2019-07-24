import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IMise } from 'app/shared/model/mise.model';
import { MiseService } from './mise.service';

@Component({
  selector: 'jhi-mise-delete-dialog',
  templateUrl: './mise-delete-dialog.component.html'
})
export class MiseDeleteDialogComponent {
  mise: IMise;

  constructor(protected miseService: MiseService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.miseService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'miseListModification',
        content: 'Deleted an mise'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-mise-delete-popup',
  template: ''
})
export class MiseDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ mise }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(MiseDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.mise = mise;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/mise', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/mise', { outlets: { popup: null } }]);
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
