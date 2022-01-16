import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { NewsItemComponentsPage, NewsItemDeleteDialog, NewsItemUpdatePage } from './news-item.page-object';

const expect = chai.expect;

describe('NewsItem e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let newsItemComponentsPage: NewsItemComponentsPage;
  let newsItemUpdatePage: NewsItemUpdatePage;
  let newsItemDeleteDialog: NewsItemDeleteDialog;
  const username = process.env.E2E_USERNAME ?? 'admin';
  const password = process.env.E2E_PASSWORD ?? 'admin';

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing(username, password);
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load NewsItems', async () => {
    await navBarPage.goToEntity('news-item');
    newsItemComponentsPage = new NewsItemComponentsPage();
    await browser.wait(ec.visibilityOf(newsItemComponentsPage.title), 5000);
    expect(await newsItemComponentsPage.getTitle()).to.eq('News Items');
    await browser.wait(ec.or(ec.visibilityOf(newsItemComponentsPage.entities), ec.visibilityOf(newsItemComponentsPage.noResult)), 1000);
  });

  it('should load create NewsItem page', async () => {
    await newsItemComponentsPage.clickOnCreateButton();
    newsItemUpdatePage = new NewsItemUpdatePage();
    expect(await newsItemUpdatePage.getPageTitle()).to.eq('Create or edit a News Item');
    await newsItemUpdatePage.cancel();
  });

  it('should create and save NewsItems', async () => {
    const nbButtonsBeforeCreate = await newsItemComponentsPage.countDeleteButtons();

    await newsItemComponentsPage.clickOnCreateButton();

    await promise.all([
      newsItemUpdatePage.setDateInput('2000-12-31'),
      newsItemUpdatePage.setTitleInput('title'),
      newsItemUpdatePage.setDescriptionInput('description'),
      newsItemUpdatePage.artistSelectLastOption(),
    ]);

    await newsItemUpdatePage.save();
    expect(await newsItemUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await newsItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last NewsItem', async () => {
    const nbButtonsBeforeDelete = await newsItemComponentsPage.countDeleteButtons();
    await newsItemComponentsPage.clickOnLastDeleteButton();

    newsItemDeleteDialog = new NewsItemDeleteDialog();
    expect(await newsItemDeleteDialog.getDialogTitle()).to.eq('Are you sure you want to delete this News Item?');
    await newsItemDeleteDialog.clickOnConfirmButton();
    await browser.wait(ec.visibilityOf(newsItemComponentsPage.title), 5000);

    expect(await newsItemComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
