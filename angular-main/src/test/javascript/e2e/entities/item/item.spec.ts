import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { ItemComponentsPage, ItemDeleteDialog, ItemUpdatePage } from './item.page-object';

const expect = chai.expect;

describe('Item e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let itemComponentsPage: ItemComponentsPage;
  let itemUpdatePage: ItemUpdatePage;
  let itemDeleteDialog: ItemDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Items', async () => {
    await navBarPage.goToEntity('item');
    itemComponentsPage = new ItemComponentsPage();
    await browser.wait(ec.visibilityOf(itemComponentsPage.title), 5000);
    expect(await itemComponentsPage.getTitle()).to.eq('Items');
    await browser.wait(ec.or(ec.visibilityOf(itemComponentsPage.entities), ec.visibilityOf(itemComponentsPage.noResult)), 1000);
  });

  it('should load create Item page', async () => {
    await itemComponentsPage.clickOnCreateButton();
    itemUpdatePage = new ItemUpdatePage();
    expect(await itemUpdatePage.getPageTitle()).to.eq('Create or edit a Item');
    await itemUpdatePage.cancel();
  });

  it('should create and save Items', async () => {
    const nbButtonsBeforeCreate = await itemComponentsPage.countDeleteButtons();

    await itemComponentsPage.clickOnCreateButton();

    await promise.all([
      itemUpdatePage.setTitleInput('title'),
      itemUpdatePage.setDescriptionInput('description'),
      itemUpdatePage.setPriceInput('5'),
      itemUpdatePage.mediumSelectLastOption(),
      itemUpdatePage.artistSelectLastOption(),
      itemUpdatePage.publisherSelectLastOption(),
      itemUpdatePage.imageSelectLastOption(),
    ]);

    await itemUpdatePage.save();
    expect(await itemUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await itemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Item', async () => {
    const nbButtonsBeforeDelete = await itemComponentsPage.countDeleteButtons();
    await itemComponentsPage.clickOnLastDeleteButton();

    itemDeleteDialog = new ItemDeleteDialog();
    expect(await itemDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this Item?');
    await itemDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(itemComponentsPage.title), 5000);

    expect(await itemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
