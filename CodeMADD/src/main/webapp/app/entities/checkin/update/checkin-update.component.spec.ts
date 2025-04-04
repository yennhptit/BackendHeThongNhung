import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITrip } from 'app/entities/trip/trip.model';
import { TripService } from 'app/entities/trip/service/trip.service';
import { CheckinService } from '../service/checkin.service';
import { ICheckin } from '../checkin.model';
import { CheckinFormService } from './checkin-form.service';

import { CheckinUpdateComponent } from './checkin-update.component';

describe('Checkin Management Update Component', () => {
  let comp: CheckinUpdateComponent;
  let fixture: ComponentFixture<CheckinUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let checkinFormService: CheckinFormService;
  let checkinService: CheckinService;
  let tripService: TripService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), CheckinUpdateComponent],
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
      .overrideTemplate(CheckinUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CheckinUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    checkinFormService = TestBed.inject(CheckinFormService);
    checkinService = TestBed.inject(CheckinService);
    tripService = TestBed.inject(TripService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Trip query and add missing value', () => {
      const checkin: ICheckin = { id: 456 };
      const trip: ITrip = { id: 8080 };
      checkin.trip = trip;

      const tripCollection: ITrip[] = [{ id: 10797 }];
      jest.spyOn(tripService, 'query').mockReturnValue(of(new HttpResponse({ body: tripCollection })));
      const additionalTrips = [trip];
      const expectedCollection: ITrip[] = [...additionalTrips, ...tripCollection];
      jest.spyOn(tripService, 'addTripToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ checkin });
      comp.ngOnInit();

      expect(tripService.query).toHaveBeenCalled();
      expect(tripService.addTripToCollectionIfMissing).toHaveBeenCalledWith(
        tripCollection,
        ...additionalTrips.map(expect.objectContaining),
      );
      expect(comp.tripsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const checkin: ICheckin = { id: 456 };
      const trip: ITrip = { id: 2666 };
      checkin.trip = trip;

      activatedRoute.data = of({ checkin });
      comp.ngOnInit();

      expect(comp.tripsSharedCollection).toContain(trip);
      expect(comp.checkin).toEqual(checkin);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICheckin>>();
      const checkin = { id: 123 };
      jest.spyOn(checkinFormService, 'getCheckin').mockReturnValue(checkin);
      jest.spyOn(checkinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ checkin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: checkin }));
      saveSubject.complete();

      // THEN
      expect(checkinFormService.getCheckin).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(checkinService.update).toHaveBeenCalledWith(expect.objectContaining(checkin));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICheckin>>();
      const checkin = { id: 123 };
      jest.spyOn(checkinFormService, 'getCheckin').mockReturnValue({ id: null });
      jest.spyOn(checkinService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ checkin: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: checkin }));
      saveSubject.complete();

      // THEN
      expect(checkinFormService.getCheckin).toHaveBeenCalled();
      expect(checkinService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICheckin>>();
      const checkin = { id: 123 };
      jest.spyOn(checkinService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ checkin });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(checkinService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTrip', () => {
      it('Should forward to tripService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(tripService, 'compareTrip');
        comp.compareTrip(entity, entity2);
        expect(tripService.compareTrip).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
