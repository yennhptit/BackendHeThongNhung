import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CheckinDetailComponent } from './checkin-detail.component';

describe('Checkin Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CheckinDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CheckinDetailComponent,
              resolve: { checkin: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(CheckinDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load checkin on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CheckinDetailComponent);

      // THEN
      expect(instance.checkin).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
