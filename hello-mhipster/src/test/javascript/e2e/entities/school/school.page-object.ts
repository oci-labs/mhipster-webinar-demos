import { element, by, ElementFinder, ElementArrayFinder } from 'protractor';

import { waitUntilAnyDisplayed, waitUntilDisplayed, click, waitUntilHidden, isVisible } from '../../util/utils';

import NavBarPage from './../../page-objects/navbar-page';

import SchoolUpdatePage from './school-update.page-object';

const expect = chai.expect;
export class SchoolDeleteDialog {
  deleteModal = element(by.className('modal'));
  private dialogTitle: ElementFinder = element(by.id('helloMhipsterApp.school.delete.question'));
  private confirmButton = element(by.id('jhi-confirm-delete-school'));

  getDialogTitle() {
    return this.dialogTitle;
  }

  async clickOnConfirmButton() {
    await this.confirmButton.click();
  }
}

export default class SchoolComponentsPage {
  createButton: ElementFinder = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('div table .btn-danger'));
  title: ElementFinder = element(by.id('school-heading'));
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
    await navBarPage.getEntityPage('school');
    await waitUntilAnyDisplayed([this.noRecords, this.table]);
    return this;
  }

  async goToCreateSchool() {
    await this.createButton.click();
    return new SchoolUpdatePage();
  }

  async deleteSchool() {
    const deleteButton = this.getDeleteButton(this.records.last());
    await click(deleteButton);

    const schoolDeleteDialog = new SchoolDeleteDialog();
    await waitUntilDisplayed(schoolDeleteDialog.deleteModal);
    expect(await schoolDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/helloMhipsterApp.school.delete.question/);
    await schoolDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(schoolDeleteDialog.deleteModal);

    expect(await isVisible(schoolDeleteDialog.deleteModal)).to.be.false;
  }
}
