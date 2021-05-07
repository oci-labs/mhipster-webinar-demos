import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import FishUpdatePage from './fish-update.page-object';

const expect = chai.expect;
export class FishDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('helloMhipsterApp.fish.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-fish'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class FishComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('fish-heading'));
  noRecords: ElementFinder = element(by.css('#app-view-container .table-responsive div.alert.alert-warning'));
  table: ElementFinder = element(by.css('#app-view-container div.table-responsive > table'));

  records: ElementArrayFinder = this.table.all(by.css('tbody tr'));

  getDetailsButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-info.btn-sm'));
  }

  getEditButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-primary.btn-sm'));
  }

  getDeleteButton(record: ElementFinder) {
    return record.element(by.css('a.btn.btn-danger.btn-sm'));
  }

  async goToPage(navBarPage: NavBarPage) {
    await navBarPage.getEntityPage('fish');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateFish() {
    await this.createButton.click();
    return new FishUpdatePage();
  }

  async deleteFish() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const fishDeleteDialog = new FishDeleteDialog();
    await waitUntilDisplayed(fishDeleteDialog.deleteModal);
    expect(await fishDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/helloMhipsterApp.fish.delete.question/);
    await fishDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(fishDeleteDialog.deleteModal);

    expect(await isVisible(fishDeleteDialog.deleteModal)).to.be.false;
  }
}
