import { MatDialog } from '@angular/material/dialog';
import { Store } from '@ngrx/store';
import { State } from '../../store';
import { ConfigService } from '../../core/config/config.service';
import { WaiterCockpitService } from '../services/waiter-cockpit.service';
import { config } from '../../core/config/config';
import {
  TestBed,
  ComponentFixture,
  async,
  fakeAsync,
  tick,
} from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';
import { TranslocoService } from '@ngneat/transloco';
import { of } from 'rxjs/internal/observable/of';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { getTranslocoModule } from '../../transloco-testing.module';
import { CoreModule } from '../../core/core.module';
import { PageEvent } from '@angular/material/paginator';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { click } from '../../shared/common/test-utils';
import { ReservationCockpitComponent } from './reservation-cockpit.component';
import { reservation } from '../../../in-memory-test-data/db-reservation';
import { ascSortReservation } from '../../../in-memory-test-data/db-reservation.-asc-sort';

const mockDialog = {
  open: jasmine.createSpy('open').and.returnValue({
    afterClosed: () => of(true),
  }),
};

const translocoServiceStub = {
  selectTranslateObject: of({
    reservationDateH: 'Reservation Date',
    emailH: 'Email',
    bookingTokenH: 'Reference Number',
    ownerH: 'Owner',
    tableH: 'Table',
    creationDateH: 'Creation date',
    statusH: 'Status',//abd
  } as any),
};

const waiterCockpitServiceStub = {
  getReservations: jasmine
    .createSpy('getReservations')
    .and.returnValue(of(reservation)),
};

const waiterCockpitServiceSortStub = {
  getReservations: jasmine
    .createSpy('getReservations')
    .and.returnValue(of(ascSortReservation)),
};

class TestBedSetUp {
  static loadWaiterCockpitServiceStud(waiterCockpitStub: any): any {
    const initialState = { config };
    return TestBed.configureTestingModule({
      declarations: [ReservationCockpitComponent],
      providers: [
        { provide: MatDialog, useValue: mockDialog },
        { provide: WaiterCockpitService, useValue: waiterCockpitStub },
        TranslocoService,
        ConfigService,
        provideMockStore({ initialState }),
      ],
      imports: [
        BrowserAnimationsModule,
        ReactiveFormsModule,
        getTranslocoModule(),
        CoreModule,
      ],
    });
  }
}

describe('ReservationCockpitComponent', () => {
  let component: ReservationCockpitComponent;
  let fixture: ComponentFixture<ReservationCockpitComponent>;
  let store: Store<State>;
  let initialState;
  let waiterCockpitService: WaiterCockpitService;
  let dialog: MatDialog;
  let translocoService: TranslocoService;
  let configService: ConfigService;
  let el: DebugElement;

  beforeEach(async(() => {
    initialState = { config };
    TestBedSetUp.loadWaiterCockpitServiceStud(waiterCockpitServiceStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(ReservationCockpitComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        waiterCockpitService = TestBed.inject(WaiterCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  it('should create component and verify content and total records of ReservationCockpit', fakeAsync(() => {
    spyOn(translocoService, 'selectTranslateObject').and.returnValue(
      translocoServiceStub.selectTranslateObject,
    );
    fixture.detectChanges();
    tick();
    expect(component).toBeTruthy();
    expect(component.reservations).toEqual(reservation.content);
    expect(component.totalReservations).toBe(5);
  }));

  it('should go to next page of ReservationCockpit', () => {
    component.page({
      pageSize: 100,
      pageIndex: 2,
      length: 50,
    });
    expect(component.reservations).toEqual(reservation.content);
    expect(component.totalReservations).toBe(5);
  });

  it('should clear form and reset', fakeAsync(() => {
    const clearFilter = el.query(By.css('.reservationsClearFilters'));
    click(clearFilter);
    fixture.detectChanges();
    tick();
    expect(component.reservations).toEqual(reservation.content);
    expect(component.totalReservations).toBe(5);
  }));

  it('should open ReservationDialogComponent dialog on click of row', fakeAsync(() => {
    fixture.detectChanges();
    const clearFilter = el.queryAll(By.css('.mat-row'));
    click(clearFilter[0]);
    tick();
    expect(dialog.open).toHaveBeenCalled();
  }));

  it('should filter the reservation table on click of submit', fakeAsync(() => {
    fixture.detectChanges();
    const submit = el.query(By.css('.reservationApplyFilters'));
    click(submit);
    tick();
    expect(component.reservations).toEqual(reservation.content);
    expect(component.totalReservations).toBe(5);
  }));
});

describe('TestingReservationCockpitComponentWithSortOrderData', () => {
  let component: ReservationCockpitComponent;
  let fixture: ComponentFixture<ReservationCockpitComponent>;
  let store: Store<State>;
  let initialState;
  let waiterCockpitService: WaiterCockpitService;
  let dialog: MatDialog;
  let translocoService: TranslocoService;
  let configService: ConfigService;
  let el: DebugElement;

  beforeEach(async(() => {
    initialState = { config };
    TestBedSetUp.loadWaiterCockpitServiceStud(waiterCockpitServiceSortStub)
      .compileComponents()
      .then(() => {
        fixture = TestBed.createComponent(ReservationCockpitComponent);
        component = fixture.componentInstance;
        el = fixture.debugElement;
        store = TestBed.inject(Store);
        configService = new ConfigService(store);
        waiterCockpitService = TestBed.inject(WaiterCockpitService);
        dialog = TestBed.inject(MatDialog);
        translocoService = TestBed.inject(TranslocoService);
      });
  }));

  it('should sort records of ReservationCockpit', () => {
    component.sort({
      active: 'Reservation Date',
      direction: 'asc',
    });
    expect(component.reservations).toEqual(ascSortReservation.content);
    expect(component.totalReservations).toBe(5);
  });
});
