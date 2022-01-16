import { element, by, ElementFinder } from 'protractor';

export class OrderItemComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('wyrgorod-order-item div table .btn-danger'));
  title = element.all(by.css('wyrgorod-order-item div h2#page-heading span')).first();
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

export class OrderItemUpdatePage {
  pageTitle = element(by.id('wyrgorod-order-item-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  idInput = element(by.id('field_id'));
  quantityInput = element(by.id('field_quantity'));

  itemSelect = element(by.id('field_item'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getText();
  }

  async setIdInput(id: string): Promise<void> {
    await this.idInput.sendKeys(id);
  }

  async getIdInput(): Promise<string> {
    return await this.idInput.getAttribute('value');
  }

  async setQuantityInput(quantity: string): Promise<void> {
    await this.quantityInput.sendKeys(quantity);
  }

  async getQuantityInput(): Promise<string> {
    return await this.quantityInput.getAttribute('value');
  }

  async itemSelectLastOption(): Promise<void> {
    await this.itemSelect.all(by.tagName('option')).last().click();
  }

  async itemSelectOption(option: string): Promise<void> {
    await this.itemSelect.sendKeys(option);
  }

  getItemSelect(): ElementFinder {
    return this.itemSelect;
  }

  async getItemSelectedOption(): Promise<string> {
    return await this.itemSelect.element(by.css('option:checked')).getText();
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

export class OrderItemDeleteDialog {
  private dialogTitle = element(by.id('wyrgorod-delete-orderItem-heading'));
  private confirmButton = element(by.id('wyrgorod-confirm-delete-orderItem'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getText();
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
