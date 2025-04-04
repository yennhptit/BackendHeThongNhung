import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ITrip } from '../trip.model';
import { TripService } from '../service/trip.service';

@Component({
  standalone: true,
  templateUrl: './trip-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class TripDeleteDialogComponent {
  trip?: ITrip;

  constructor(
    protected tripService: TripService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.tripService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
