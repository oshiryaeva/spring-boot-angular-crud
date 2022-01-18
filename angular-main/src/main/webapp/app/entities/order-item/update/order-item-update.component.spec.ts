jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrderItemService } from '../service/order-item.service';
import { IOrderItem, OrderItem } from '../order-item.model';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';

import { OrderItemUpdateComponent } from './order-item-update.component';

describe('Component Tests', () => {
  describe('OrderItem Management Update Component', () => {
    let comp: OrderItemUpdateComponent;
    let fixture: ComponentFixture<OrderItemUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let orderItemService: OrderItemService;
    let itemService: ItemService;
    let orderService: OrderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrderItemUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrderItemUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrderItemUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      orderItemService = TestBed.inject(OrderItemService);
      itemService = TestBed.inject(ItemService);
      orderService = TestBed.inject(OrderService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Item query and add missing value', () => {
        const orderItem: IOrderItem = { id: 456 };
        const item: IItem = { id: 28532 };
        orderItem.item = item;

        const itemCollection: IItem[] = [{ id: 13732 }];
        jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
        const additionalItems = [item];
        const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
        jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderItem });
        comp.ngOnInit();

        expect(itemService.query).toHaveBeenCalled();
        expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(itemCollection, ...additionalItems);
        expect(comp.itemsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Order query and add missing value', () => {
        const orderItem: IOrderItem = { id: 456 };
        const order: IOrder = { id: 39120 };
        orderItem.order = order;

        const orderCollection: IOrder[] = [{ id: 6598 }];
        jest.spyOn(orderService, 'query').mockReturnValue(of(new HttpResponse({ body: orderCollection })));
        const additionalOrders = [order];
        const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
        jest.spyOn(orderService, 'addOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ orderItem });
        comp.ngOnInit();

        expect(orderService.query).toHaveBeenCalled();
        expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(orderCollection, ...additionalOrders);
        expect(comp.ordersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const orderItem: IOrderItem = { id: 456 };
        const item: IItem = { id: 86495 };
        orderItem.item = item;
        const order: IOrder = { id: 30033 };
        orderItem.order = order;

        activatedRoute.data = of({ orderItem });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(orderItem));
        expect(comp.itemsSharedCollection).toContain(item);
        expect(comp.ordersSharedCollection).toContain(order);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderItem>>();
        const orderItem = { id: 123 };
        jest.spyOn(orderItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orderItem }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(orderItemService.update).toHaveBeenCalledWith(orderItem);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderItem>>();
        const orderItem = new OrderItem();
        jest.spyOn(orderItemService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orderItem }));
        saveSubject.complete();

        // THEN
        expect(orderItemService.create).toHaveBeenCalledWith(orderItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<OrderItem>>();
        const orderItem = { id: 123 };
        jest.spyOn(orderItemService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderItem });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(orderItemService.update).toHaveBeenCalledWith(orderItem);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackItemById', () => {
        it('Should return tracked Item primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackItemById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackOrderById', () => {
        it('Should return tracked Order primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrderById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
