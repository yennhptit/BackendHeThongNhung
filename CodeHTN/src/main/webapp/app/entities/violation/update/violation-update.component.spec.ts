import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ITrip } from 'app/entities/trip/trip.model';
import { TripService } from 'app/entities/trip/service/trip.service';
import { ViolationService } from '../service/violation.service';
import { IViolation } from '../violation.model';
import { ViolationFormService } from './violation-form.service';

import { ViolationUpdateComponent } from './violation-update.component';

describe('Violation Management Update Component', () => {
  let comp: ViolationUpdateComponent;
  let fixture: ComponentFixture<ViolationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let violationFormService: ViolationFormService;
  let violationService: ViolationService;
  let tripService: TripService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), ViolationUpdateComponent],
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
      .overrideTemplate(ViolationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ViolationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    violationFormService = TestBed.inject(ViolationFormService);
    violationService = TestBed.inject(ViolationService);
    tripService = TestBed.inject(TripService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Trip query and add missing value', () => {
      const violation: IViolation = { id: 456 };
      const trip: ITrip = { id: 19671 };
      violation.trip = trip;

      const tripCollection: ITrip[] = [{ id: 5692 }];
      jest.spyOn(tripService, 'query').mockReturnValue(of(new HttpResponse({ body: tripCollection })));
      const additionalTrips = [trip];
      const expectedCollection: ITrip[] = [...additionalTrips, ...tripCollection];
      jest.spyOn(tripService, 'addTripToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ violation });
      comp.ngOnInit();

      expect(tripService.query).toHaveBeenCalled();
      expect(tripService.addTripToCollectionIfMissing).toHaveBeenCalledWith(
        tripCollection,
        ...additionalTrips.map(expect.objectContaining),
      );
      expect(comp.tripsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const violation: IViolation = { id: 456 };
      const trip: ITrip = { id: 19189 };
      violation.trip = trip;

      activatedRoute.data = of({ violation });
      comp.ngOnInit();

      expect(comp.tripsSharedCollection).toContain(trip);
      expect(comp.violation).toEqual(violation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IViolation>>();
      const violation = { id: 123 };
      jest.spyOn(violationFormService, 'getViolation').mockReturnValue(violation);
      jest.spyOn(violationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ violation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: violation }));
      saveSubject.complete();

      // THEN
      expect(violationFormService.getViolation).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(violationService.update).toHaveBeenCalledWith(expect.objectContaining(violation));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IViolation>>();
      const violation = { id: 123 };
      jest.spyOn(violationFormService, 'getViolation').mockReturnValue({ id: null });
      jest.spyOn(violationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ violation: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: violation }));
      saveSubject.complete();

      // THEN
      expect(violationFormService.getViolation).toHaveBeenCalled();
      expect(violationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IViolation>>();
      const violation = { id: 123 };
      jest.spyOn(violationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ violation });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(violationService.update).toHaveBeenCalled();
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
