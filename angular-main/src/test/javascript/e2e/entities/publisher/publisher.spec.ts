import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { PublisherComponentsPage, PublisherDeleteDialog, PublisherUpdatePage } from './publisher.page-object';

const expect = chai.expect;

describe('Publisher e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let publisherComponentsPage: PublisherComponentsPage;
  let publisherUpdatePage: PublisherUpdatePage;
  let publisherDeleteDialog: PublisherDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Publishers', async () => {
    await navBarPage.goToEntity('publisher');
    publisherComponentsPage = new PublisherComponentsPage();
    await browser.wait(ec.visibilityOf(publisherComponentsPage.title), 5000);
    expect(await publisherComponentsPage.getTitle()).to.eq('Publishers');
    await browser.wait(ec.or(ec.visibilityOf(publisherComponentsPage.entities), ec.visibilityOf(publisherComponentsPage.noResult)), 1000);
  });

  it('should load create Publisher page', async () => {
    await publisherComponentsPage.clickOnCreateButton();
    publisherUpdatePage = new PublisherUpdatePage();
    expect(await publisherUpdatePage.getPageTitle()).to.eq('Create or edit a Publisher');
    await publisherUpdatePage.cancel();
  });

  it('should create and save Publishers', async () => {
    const nbButtonsBeforeCreate = await publisherComponentsPage.countDeleteButtons();

    await publisherComponentsPage.clickOnCreateButton();

    await promise.all([publisherUpdatePage.setNameInput('name')]);

    await publisherUpdatePage.save();
    expect(await publisherUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await publisherComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Publisher', async () => {
    const nbButtonsBeforeDelete = await publisherComponentsPage.countDeleteButtons();
    await publisherComponentsPage.clickOnLastDeleteButton();

    publisherDeleteDialog = new PublisherDeleteDialog();
    expect(await publisherDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Publisher?');
    await publisherDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(publisherComponentsPage.title), 5000);

    expect(await publisherComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
