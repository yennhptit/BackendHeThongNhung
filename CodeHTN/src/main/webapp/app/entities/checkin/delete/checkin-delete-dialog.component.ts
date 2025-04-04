import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICheckin } from '../checkin.model';
import { CheckinService } from '../service/checkin.service';

@Component({
  standalone: true,
  templateUrl: './checkin-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CheckinDeleteDialogComponent {
  checkin?: ICheckin;

  constructor(
    protected checkinService: CheckinService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.checkinService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
