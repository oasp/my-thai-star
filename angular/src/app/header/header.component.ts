import { Component, Output, EventEmitter } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { Router } from '@angular/router';

import { AuthService } from '../core/authentication/auth.service';
import { SidenavService } from '../sidenav/shared/sidenav.service';
import { UserAreaService } from '../user-area/shared/user-area.service';
import { WindowService } from '../core/windowService/windowService.service';

import { LoginDialogComponent } from '../user-area/login-dialog/login-dialog.component';
import { PasswordDialogComponent } from '../user-area/password-dialog/password-dialog.component';
import { TwitterDialogComponent } from '../user-area/twitter-dialog/twitter-dialog.component';
import { TranslateService } from '@ngx-translate/core';
import { DateTimeAdapter } from 'ng-pick-datetime';
import { config } from '../config';

@Component({
  selector: 'public-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss'],
})
export class HeaderComponent {
  selectableLangs: any[];
  flag: string;

  @Output('openCloseSidenavMobile')
  sidenavNavigationEmitter: EventEmitter<any> = new EventEmitter();

  constructor(
    public window: WindowService,
    public translate: TranslateService,
    public router: Router,
    public sidenav: SidenavService,
    public dialog: MatDialog,
    public auth: AuthService,
    public userService: UserAreaService,
    public dateTimeAdapter: DateTimeAdapter<any>,
  ) {
    this.selectableLangs = config.langs;
    this.getFlag(this.translate.currentLang);
    this.dateTimeAdapter.setLocale(this.translate.currentLang);
  }

  openCloseSideNav(sidenavOpened: boolean): void {
    sidenavOpened ? this.sidenav.closeSideNav() : this.sidenav.openSideNav();
  }

  openCloseNavigationSideNav(): void {
    this.sidenavNavigationEmitter.emit();
  }

  navigateTo(route: string): void {
    this.router.navigate([route]);
    this.sidenavNavigationEmitter.emit();
  }

  changeLanguage(lang: string): void {
    this.translate.use(lang);
    this.dateTimeAdapter.setLocale(lang);
    this.getFlag(lang);
  }

  getFlag(lang: string): void {
    switch (lang) {
      case 'ca':
        this.flag = 'es';
        break;
      case 'en':
        this.flag = 'gb';
        break;
      case 'hi':
        this.flag = 'in';
        break;

      default:
        this.flag = lang;
        break;
    }
  }

  openLoginDialog(): void {
    const dialogRef: MatDialogRef<LoginDialogComponent> = this.dialog.open(
      LoginDialogComponent,
      {
        width: this.window.responsiveWidth(),
      },
    );
    dialogRef.afterClosed().subscribe((result: any) => {
      if (result) {
        if (result.email) {
          this.userService.register(result.email, result.username, result.password, result.idrole);
        } else {
          this.userService.login(result.username, result.password);
        }
      }
    });
  }

  openResetDialog(): void {
    const dialogRef: MatDialogRef<PasswordDialogComponent> = this.dialog.open(
      PasswordDialogComponent,
      {
        width: this.window.responsiveWidth(),
      },
    );
    dialogRef.afterClosed().subscribe((result: any) => {
      // TODO: manage user input
    });
  }

  openTwitterDialog(): void {
    const dialogRef: MatDialogRef<TwitterDialogComponent> = this.dialog.open(
      TwitterDialogComponent,
      {
        width: this.window.responsiveWidth(),
      },
    );
    dialogRef.afterClosed().subscribe((result: any) => {
      // TODO: manage user input
    });
  }

  logout(): void {
    this.userService.logout();
    this.router.navigate(['restaurant']);
  }
}
