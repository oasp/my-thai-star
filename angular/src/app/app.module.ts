import { environment } from '../environments/environment';
import { ConfigModule } from './core/config/config.module';
import { ServiceWorkerModule } from '@angular/service-worker';
import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { SidenavModule } from './sidenav/sidenav.module';
import { BookTableModule } from './book-table/book-table.module';
import { WaiterCockpitModule } from './cockpit-area/cockpit.module';
import { UserAreaModule } from './user-area/user-area.module';
import { EmailConfirmationModule } from './email-confirmations/email-confirmations.module';
import { HeaderModule } from './header/header.module';
import { HomeModule } from './home/home.module';
import { MenuModule } from './menu/menu.module';
import { CoreModule } from './core/core.module';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';

import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { HttpClient } from '@angular/common/http';

import { ElectronService } from './shared/electron/electron.service';
import { WebviewDirective } from './shared/directives/webview.directive';
import { CustomErrorHandler } from './core/error/custom-error-handler';

// AoT requires an exported function for factories
export function HttpLoaderFactory(http: HttpClient): TranslateHttpLoader {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [AppComponent, WebviewDirective],
  imports: [
    ConfigModule,
    BrowserModule,
    HeaderModule,
    HomeModule,
    MenuModule,
    BookTableModule,
    SidenavModule,
    WaiterCockpitModule,
    UserAreaModule,
    CoreModule,
    EmailConfirmationModule,
    AppRoutingModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient],
      },
    }),
    ServiceWorkerModule.register('./ngsw-worker.js', {
      enabled: environment.production,
    }),
  ],
  providers: [
    ElectronService,
    { provide: ErrorHandler, useClass: CustomErrorHandler }
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
