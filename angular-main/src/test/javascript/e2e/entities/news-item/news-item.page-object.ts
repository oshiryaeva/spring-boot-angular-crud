import { element, by, ElementFinder } from 'protractor';

export class NewsItemComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('wyrgorod-news-item div table .btn-danger'));
  title = element.all(by.css('wyrgorod-news-item div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getText();
  }
}

export class NewsItemUpdatePage {
  pageTitle = element(by.id('wyrgorod-news-item-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  dateInput = element(by.id('field_date'));
  titleInput = element(by.id('field_title'));
  descriptionInput = element(by.id('field_description'));

  artistSelect = element(by.id('field_artist'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setDateInput(date: string): Promise<void> {
    await this.dateInput.sendKeys(date);
  }

  async getDateInput(): Promise<string> {
    return await this.dateInput.getAttribute('value');
  }

  async setTitleInput(title: string): Promise<void> {
    await this.titleInput.sendKeys(title);
  }

  async getTitleInput(): Promise<string> {
    return await this.titleInput.getAttribute('value');
  }

  async setDescriptionInput(description: string): Promise<void> {
    await this.descriptionInput.sendKeys(description);
  }

  async getDescriptionInput(): Promise<string> {
    return await this.descriptionInput.getAttribute('value');
  }

  async artistSelectLastOption(): Promise<void> {
    await this.artistSelect.all(by.tagName('option')).last().click();
  }

  async artistSelectOption(option: string): Promise<void> {
    await this.artistSelect.sendKeys(option);
  }

  getArtistSelect(): ElementFinder {
    return this.artistSelect;
  }

  async getArtistSelectedOption(): Promise<string> {
    return await this.artistSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class NewsItemDeleteDialog {
  private dialogTitle = element(by.id('wyrgorod-delete-newsItem-heading'));
  private confirmButton = element(by.id('wyrgorod-confirm-delete-newsItem'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
