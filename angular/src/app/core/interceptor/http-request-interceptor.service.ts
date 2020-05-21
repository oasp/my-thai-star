import {
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Store } from '@ngrx/store';
import * as fromApp from 'app/store/reducers';
import * as fromAuth from 'app/user-area/store/selectors/auth.selectors';
import { first, flatMap } from 'rxjs/operators';

@Injectable()
export class HttpRequestInterceptorService implements HttpInterceptor {
  constructor(private store: Store<fromApp.State>) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    return this.store.select(fromAuth.getToken).pipe(
      first(),
      flatMap((token) => {
        token = token.replace('Bearer', '');
        const authReq = !!token
          ? req.clone({
              setHeaders: { Authorization: 'Bearer ' + token },
            })
          : req;
        return next.handle(authReq);
      }),
    );
  }
}
