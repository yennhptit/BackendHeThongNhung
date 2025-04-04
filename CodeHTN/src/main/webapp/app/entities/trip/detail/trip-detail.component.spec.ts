import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { TripDetailComponent } from './trip-detail.component';

describe('Trip Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TripDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: TripDetailComponent,
              resolve: { trip: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(TripDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load trip on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', TripDetailComponent);

      // THEN
      expect(instance.trip).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
