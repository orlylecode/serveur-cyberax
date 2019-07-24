import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IListAttente } from 'app/shared/model/list-attente.model';
import { ListAttenteService } from './list-attente.service';

@Component({
  selector: 'jhi-list-attente-delete-dialog',
  templateUrl: './list-attente-delete-dialog.component.html'
})
export class ListAttenteDeleteDialogComponent {
  listAttente: IListAttente;

  constructor(
    protected listAttenteService: ListAttenteService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.listAttenteService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'listAttenteListModification',
        content: 'Deleted an listAttente'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-list-attente-delete-popup',
  template: ''
})
export class ListAttenteDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ listAttente }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ListAttenteDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.listAttente = listAttente;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/list-attente', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/list-attente', { outlets: { popup: null } }]);
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
