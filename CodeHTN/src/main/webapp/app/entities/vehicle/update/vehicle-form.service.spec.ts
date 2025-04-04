import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../vehicle.test-samples';

import { VehicleFormService } from './vehicle-form.service';

describe('Vehicle Form Service', () => {
  let service: VehicleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(VehicleFormService);
  });

  describe('Service methods', () => {
    describe('createVehicleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createVehicleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            licensePlate: expect.any(Object),
            model: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });

      it('passing IVehicle should create a new form with FormGroup', () => {
        const formGroup = service.createVehicleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            licensePlate: expect.any(Object),
            model: expect.any(Object),
            status: expect.any(Object),
          }),
        );
      });
    });

    describe('getVehicle', () => {
      it('should return NewVehicle for default Vehicle initial value', () => {
        const formGroup = service.createVehicleFormGroup(sampleWithNewData);

        const vehicle = service.getVehicle(formGroup) as any;

        expect(vehicle).toMatchObject(sampleWithNewData);
      });

      it('should return NewVehicle for empty Vehicle initial value', () => {
        const formGroup = service.createVehicleFormGroup();

        const vehicle = service.getVehicle(formGroup) as any;

        expect(vehicle).toMatchObject({});
      });

      it('should return IVehicle', () => {
        const formGroup = service.createVehicleFormGroup(sampleWithRequiredData);

        const vehicle = service.getVehicle(formGroup) as any;

        expect(vehicle).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IVehicle should not enable id FormControl', () => {
        const formGroup = service.createVehicleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewVehicle should disable id FormControl', () => {
        const formGroup = service.createVehicleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
