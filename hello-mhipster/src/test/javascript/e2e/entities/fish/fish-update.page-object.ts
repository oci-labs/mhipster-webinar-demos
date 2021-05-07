import { element, by, ElementFinder } from 'protractor';
import { waitUntilDisplayed, waitUntilHidden, isVisible } from '../../util/utils';

const expect = chai.expect;

export default class FishUpdatePage {
  pageTitle: ElementFinder = element(by.id('helloMhipsterApp.fish.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  nameInput: ElementFinder = element(by.css('input#fish-name'));
  ageInput: ElementFinder = element(by.css('input#fish-age'));
  waterTypeSelect: ElementFinder = element(by.css('select#fish-waterType'));
  schoolSelect: ElementFinder = element(by.css('select#fish-school'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setNameInput(name) {
    await this.nameInput.sendKeys(name);
  }

  async getNameInput() {
    return this.nameInput.getAttribute('value');
  }

  async setAgeInput(age) {
    await this.ageInput.sendKeys(age);
  }

  async getAgeInput() {
    return this.ageInput.getAttribute('value');
  }

  async setWaterTypeSelect(waterType) {
    await this.waterTypeSelect.sendKeys(waterType);
  }

  async getWaterTypeSelect() {
    return this.waterTypeSelect.element(by.css('option:checked')).getText();
  }

  async waterTypeSelectLastOption() {
    await this.waterTypeSelect.all(by.tagName('option')).last().click();
  }
  async schoolSelectLastOption() {
    await this.schoolSelect.all(by.tagName('option')).last().click();
  }

  async schoolSelectOption(option) {
    await this.schoolSelect.sendKeys(option);
  }

  getSchoolSelect() {
    return this.schoolSelect;
  }

  async getSchoolSelectedOption() {
    return this.schoolSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }

  async enterData() {
    await waitUntilDisplayed(this.saveButton);
    await this.setNameInput('name');
    expect(await this.getNameInput()).to.match(/name/);
    await waitUntilDisplayed(this.saveButton);
    await this.setAgeInput('5');
    expect(await this.getAgeInput()).to.eq('5');
    await waitUntilDisplayed(this.saveButton);
    await this.waterTypeSelectLastOption();
    await this.schoolSelectLastOption();
    await this.save();
    await waitUntilHidden(this.saveButton);
    expect(await isVisible(this.saveButton)).to.be.false;
  }
}
