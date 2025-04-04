import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { VehicleDetailComponent } from './vehicle-detail.component';

describe('Vehicle Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [VehicleDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: VehicleDetailComponent,
              resolve: { vehicle: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(VehicleDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load vehicle on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', VehicleDetailComponent);

      // THEN
      expect(instance.vehicle).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
