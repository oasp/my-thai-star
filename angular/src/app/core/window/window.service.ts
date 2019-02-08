import { Injectable } from '@angular/core';

function getWindow(): Window {
  return window;
}

@Injectable()
export class WindowService {
  get nativeWindow(): Window {
    return getWindow();
  }

  /* @returns {string}
   * @memberof WindowService
   */
  responsiveWidth(): string {
    return getWindow().innerWidth > 800 ? '40%' : '80%';
  }
}
