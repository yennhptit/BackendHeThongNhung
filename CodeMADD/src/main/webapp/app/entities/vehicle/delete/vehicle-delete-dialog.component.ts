import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IVehicle } from '../vehicle.model';
import { VehicleService } from '../service/vehicle.service';

@Component({
  standalone: true,
  templateUrl: './vehicle-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class VehicleDeleteDialogComponent {
  vehicle?: IVehicle;

  constructor(
    protected vehicleService: VehicleService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.vehicleService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
