import { ErrorHandler, Injectable } from '@angular/core';

@Injectable()
export class CustomErrorHandler extends ErrorHandler {
    constructor() {
        super();
    }

    public handleError(error: any): void {
        // You can add your own logic here.
        // It is not required to delegate to the original implementation
        super.handleError(error);
    }
}