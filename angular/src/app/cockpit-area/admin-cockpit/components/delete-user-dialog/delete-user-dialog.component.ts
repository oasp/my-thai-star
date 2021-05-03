import { Component,Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ConfigService } from 'app/core/config/config.service';
import { TranslocoService } from '@ngneat/transloco';
import { UserInfo } from 'app/shared/backend-models/interfaces';

@Component({
  selector: 'app-delete-user-dialog',
  templateUrl: './delete-user-dialog.component.html',
  styleUrls: ['./delete-user-dialog.component.scss']
})
export class DeleteUserDialogComponent implements OnInit {
 
  user: any;
  constructor(
    private translocoService: TranslocoService,
    @Inject(MAT_DIALOG_DATA) dialogData: any,
    private configService: ConfigService,
  ) {
    this.user = dialogData;
  }
  

  ngOnInit(): void {
  }

  deleteUser(): void{

  }
}
