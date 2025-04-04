import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../trip.test-samples';

import { TripFormService } from './trip-form.service';

describe('Trip Form Service', () => {
  let service: TripFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TripFormService);
  });

  describe('Service methods', () => {
    describe('createTripFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTripFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            status: expect.any(Object),
            driver: expect.any(Object),
            vehicle: expect.any(Object),
          }),
        );
      });

      it('passing ITrip should create a new form with FormGroup', () => {
        const formGroup = service.createTripFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            startTime: expect.any(Object),
            endTime: expect.any(Object),
            status: expect.any(Object),
            driver: expect.any(Object),
            vehicle: expect.any(Object),
          }),
        );
      });
    });

    describe('getTrip', () => {
      it('should return NewTrip for default Trip initial value', () => {
        const formGroup = service.createTripFormGroup(sampleWithNewData);

        const trip = service.getTrip(formGroup) as any;

        expect(trip).toMatchObject(sampleWithNewData);
      });

      it('should return NewTrip for empty Trip initial value', () => {
        const formGroup = service.createTripFormGroup();

        const trip = service.getTrip(formGroup) as any;

        expect(trip).toMatchObject({});
      });

      it('should return ITrip', () => {
        const formGroup = service.createTripFormGroup(sampleWithRequiredData);

        const trip = service.getTrip(formGroup) as any;

        expect(trip).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITrip should not enable id FormControl', () => {
        const formGroup = service.createTripFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTrip should disable id FormControl', () => {
        const formGroup = service.createTripFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
