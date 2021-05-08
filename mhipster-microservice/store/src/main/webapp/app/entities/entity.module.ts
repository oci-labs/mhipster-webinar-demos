import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'product',
        loadChildren: () => import('./crm/product/product.module').then(m => m.CrmProductModule),
      },
      {
        path: 'customer',
        loadChildren: () => import('./crm/customer/customer.module').then(m => m.CrmCustomerModule),
      },
      {
        path: 'product-order',
        loadChildren: () => import('./crm/product-order/product-order.module').then(m => m.CrmProductOrderModule),
      },
      {
        path: 'order-item',
        loadChildren: () => import('./crm/order-item/order-item.module').then(m => m.CrmOrderItemModule),
      },
      {
        path: 'invoice',
        loadChildren: () => import('./invoice/invoice/invoice.module').then(m => m.InvoiceInvoiceModule),
      },
      {
        path: 'shipment',
        loadChildren: () => import('./invoice/shipment/shipment.module').then(m => m.InvoiceShipmentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class StoreEntityModule {}
