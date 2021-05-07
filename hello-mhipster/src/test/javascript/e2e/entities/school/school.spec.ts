import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import SchoolComponentsPage from './school.page-object';
import SchoolUpdatePage from './school-update.page-object';
import {
  waitUntilDisplayed,
  waitUntilAnyDisplayed,
  click,
  getRecordsCount,
  waitUntilHidden,
  waitUntilCount,
  isVisible,
} from '../../util/utils';

const expect = chai.expect;

describe('School e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let schoolComponentsPage: SchoolComponentsPage;
  let schoolUpdatePage: SchoolUpdatePage;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
    await waitUntilDisplayed(navBarPage.adminMenu);
    await waitUntilDisplayed(navBarPage.accountMenu);
  });

  beforeEach(async () => {
    await browser.get('/');
    await waitUntilDisplayed(navBarPage.entityMenu);
    schoolComponentsPage = new SchoolComponentsPage();
    schoolComponentsPage = await schoolComponentsPage.goToPage(navBarPage);
  });

  it('should load Schools', async () => {
    expect(await schoolComponentsPage.title.getText()).to.match(/Schools/);
    expect(await schoolComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete Schools', async () => {
    const beforeRecordsCount = (await isVisible(schoolComponentsPage.noRecords)) ? 0 : await getRecordsCount(schoolComponentsPage.table);
    schoolUpdatePage = await schoolComponentsPage.goToCreateSchool();
    await schoolUpdatePage.enterData();

    expect(await schoolComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(schoolComponentsPage.table);
    await waitUntilCount(schoolComponentsPage.records, beforeRecordsCount + 1);
    expect(await schoolComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await schoolComponentsPage.deleteSchool();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(schoolComponentsPage.records, beforeRecordsCount);
      expect(await schoolComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(schoolComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
