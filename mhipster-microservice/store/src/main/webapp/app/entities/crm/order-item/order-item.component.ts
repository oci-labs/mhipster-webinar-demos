import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderItem } from 'app/shared/model/crm/order-item.model';
import { OrderItemService } from './order-item.service';
import { OrderItemDeleteDialogComponent } from './order-item-delete-dialog.component';

@Component({
  selector: 'jhi-order-item',
  templateUrl: './order-item.component.html',
})
export class OrderItemComponent implements OnInit, OnDestroy {
  orderItems?: IOrderItem[];
  eventSubscriber?: Subscription;

  constructor(protected orderItemService: OrderItemService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.orderItemService.query().subscribe((res: HttpResponse<IOrderItem[]>) => (this.orderItems = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInOrderItems();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IOrderItem): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInOrderItems(): void {
    this.eventSubscriber = this.eventManager.subscribe('orderItemListModification', () => this.loadAll());
  }

  delete(orderItem: IOrderItem): void {
    const modalRef = this.modalService.open(OrderItemDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orderItem = orderItem;
  }
}
