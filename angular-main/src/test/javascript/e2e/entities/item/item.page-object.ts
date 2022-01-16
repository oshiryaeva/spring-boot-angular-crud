import { element, by, ElementFinder } from 'protractor';

export class ItemComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('wyrgorod-item div table .btn-danger'));
  title = element.all(by.css('wyrgorod-item div h2#page-heading span')).first();
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

export class ItemUpdatePage {
  pageTitle = element(by.id('wyrgorod-item-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  titleInput = element(by.id('field_title'));
  descriptionInput = element(by.id('field_description'));
  priceInput = element(by.id('field_price'));
  mediumSelect = element(by.id('field_medium'));

  artistSelect = element(by.id('field_artist'));
  publisherSelect = element(by.id('field_publisher'));
  imageSelect = element(by.id('field_image'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
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

  async setPriceInput(price: string): Promise<void> {
    await this.priceInput.sendKeys(price);
  }

  async getPriceInput(): Promise<string> {
    return await this.priceInput.getAttribute('value');
  }

  async setMediumSelect(medium: string): Promise<void> {
    await this.mediumSelect.sendKeys(medium);
  }

  async getMediumSelect(): Promise<string> {
    return await this.mediumSelect.element(by.css('option:checked')).getText();
  }

  async mediumSelectLastOption(): Promise<void> {
    await this.mediumSelect.all(by.tagName('option')).last().click();
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

  async publisherSelectLastOption(): Promise<void> {
    await this.publisherSelect.all(by.tagName('option')).last().click();
  }

  async publisherSelectOption(option: string): Promise<void> {
    await this.publisherSelect.sendKeys(option);
  }

  getPublisherSelect(): ElementFinder {
    return this.publisherSelect;
  }

  async getPublisherSelectedOption(): Promise<string> {
    return await this.publisherSelect.element(by.css('option:checked')).getText();
  }

  async imageSelectLastOption(): Promise<void> {
    await this.imageSelect.all(by.tagName('option')).last().click();
  }

  async imageSelectOption(option: string): Promise<void> {
    await this.imageSelect.sendKeys(option);
  }

  getImageSelect(): ElementFinder {
    return this.imageSelect;
  }

  async getImageSelectedOption(): Promise<string> {
    return await this.imageSelect.element(by.css('option:checked')).getText();
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

export class ItemDeleteDialog {
  private dialogTitle = element(by.id('wyrgorod-delete-item-heading'));
  private confirmButton = element(by.id('wyrgorod-confirm-delete-item'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
