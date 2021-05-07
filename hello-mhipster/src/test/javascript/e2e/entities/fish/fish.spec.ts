import { browser, element, by } from 'protractor';

import NavBarPage from './../../page-objects/navbar-page';
import SignInPage from './../../page-objects/signin-page';
import FishComponentsPage from './fish.page-object';
import FishUpdatePage from './fish-update.page-object';
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

describe('Fish e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let fishComponentsPage: FishComponentsPage;
  let fishUpdatePage: FishUpdatePage;

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
    fishComponentsPage = new FishComponentsPage();
    fishComponentsPage = await fishComponentsPage.goToPage(navBarPage);
  });

  it('should load Fish', async () => {
    expect(await fishComponentsPage.title.getText()).to.match(/Fish/);
    expect(await fishComponentsPage.createButton.isEnabled()).to.be.true;
  });

  it('should create and delete Fish', async () => {
    const beforeRecordsCount = (await isVisible(fishComponentsPage.noRecords)) ? 0 : await getRecordsCount(fishComponentsPage.table);
    fishUpdatePage = await fishComponentsPage.goToCreateFish();
    await fishUpdatePage.enterData();

    expect(await fishComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilDisplayed(fishComponentsPage.table);
    await waitUntilCount(fishComponentsPage.records, beforeRecordsCount + 1);
    expect(await fishComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);

    await fishComponentsPage.deleteFish();
    if (beforeRecordsCount !== 0) {
      await waitUntilCount(fishComponentsPage.records, beforeRecordsCount);
      expect(await fishComponentsPage.records.count()).to.eq(beforeRecordsCount);
    } else {
      await waitUntilDisplayed(fishComponentsPage.noRecords);
    }
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
