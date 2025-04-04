import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDriver } from '../driver.model';
import { DriverService } from '../service/driver.service';

@Component({
  standalone: true,
  templateUrl: './driver-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DriverDeleteDialogComponent {
  driver?: IDriver;

  constructor(
    protected driverService: DriverService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.driverService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
