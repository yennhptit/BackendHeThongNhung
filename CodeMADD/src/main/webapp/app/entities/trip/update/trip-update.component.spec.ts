import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IDriver } from 'app/entities/driver/driver.model';
import { DriverService } from 'app/entities/driver/service/driver.service';
import { IVehicle } from 'app/entities/vehicle/vehicle.model';
import { VehicleService } from 'app/entities/vehicle/service/vehicle.service';
import { ITrip } from '../trip.model';
import { TripService } from '../service/trip.service';
import { TripFormService } from './trip-form.service';

import { TripUpdateComponent } from './trip-update.component';

describe('Trip Management Update Component', () => {
  let comp: TripUpdateComponent;
  let fixture: ComponentFixture<TripUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let tripFormService: TripFormService;
  let tripService: TripService;
  let driverService: DriverService;
  let vehicleService: VehicleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), TripUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(TripUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(TripUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    tripFormService = TestBed.inject(TripFormService);
    tripService = TestBed.inject(TripService);
    driverService = TestBed.inject(DriverService);
    vehicleService = TestBed.inject(VehicleService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Driver query and add missing value', () => {
      const trip: ITrip = { id: 456 };
      const driver: IDriver = { id: 6291 };
      trip.driver = driver;

      const driverCollection: IDriver[] = [{ id: 21799 }];
      jest.spyOn(driverService, 'query').mockReturnValue(of(new HttpResponse({ body: driverCollection })));
      const additionalDrivers = [driver];
      const expectedCollection: IDriver[] = [...additionalDrivers, ...driverCollection];
      jest.spyOn(driverService, 'addDriverToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trip });
      comp.ngOnInit();

      expect(driverService.query).toHaveBeenCalled();
      expect(driverService.addDriverToCollectionIfMissing).toHaveBeenCalledWith(
        driverCollection,
        ...additionalDrivers.map(expect.objectContaining),
      );
      expect(comp.driversSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Vehicle query and add missing value', () => {
      const trip: ITrip = { id: 456 };
      const vehicle: IVehicle = { id: 20575 };
      trip.vehicle = vehicle;

      const vehicleCollection: IVehicle[] = [{ id: 18758 }];
      jest.spyOn(vehicleService, 'query').mockReturnValue(of(new HttpResponse({ body: vehicleCollection })));
      const additionalVehicles = [vehicle];
      const expectedCollection: IVehicle[] = [...additionalVehicles, ...vehicleCollection];
      jest.spyOn(vehicleService, 'addVehicleToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ trip });
      comp.ngOnInit();

      expect(vehicleService.query).toHaveBeenCalled();
      expect(vehicleService.addVehicleToCollectionIfMissing).toHaveBeenCalledWith(
        vehicleCollection,
        ...additionalVehicles.map(expect.objectContaining),
      );
      expect(comp.vehiclesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const trip: ITrip = { id: 456 };
      const driver: IDriver = { id: 22309 };
      trip.driver = driver;
      const vehicle: IVehicle = { id: 26640 };
      trip.vehicle = vehicle;

      activatedRoute.data = of({ trip });
      comp.ngOnInit();

      expect(comp.driversSharedCollection).toContain(driver);
      expect(comp.vehiclesSharedCollection).toContain(vehicle);
      expect(comp.trip).toEqual(trip);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrip>>();
      const trip = { id: 123 };
      jest.spyOn(tripFormService, 'getTrip').mockReturnValue(trip);
      jest.spyOn(tripService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trip });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trip }));
      saveSubject.complete();

      // THEN
      expect(tripFormService.getTrip).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(tripService.update).toHaveBeenCalledWith(expect.objectContaining(trip));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrip>>();
      const trip = { id: 123 };
      jest.spyOn(tripFormService, 'getTrip').mockReturnValue({ id: null });
      jest.spyOn(tripService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trip: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: trip }));
      saveSubject.complete();

      // THEN
      expect(tripFormService.getTrip).toHaveBeenCalled();
      expect(tripService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITrip>>();
      const trip = { id: 123 };
      jest.spyOn(tripService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ trip });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(tripService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDriver', () => {
      it('Should forward to driverService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(driverService, 'compareDriver');
        comp.compareDriver(entity, entity2);
        expect(driverService.compareDriver).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareVehicle', () => {
      it('Should forward to vehicleService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(vehicleService, 'compareVehicle');
        comp.compareVehicle(entity, entity2);
        expect(vehicleService.compareVehicle).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
