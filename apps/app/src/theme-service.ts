import { Inject, Injectable } from '@angular/core';
import { DOCUMENT } from '@angular/common';

@Injectable({
    providedIn: 'root',
})
export class ThemeService {

    constructor(@Inject(DOCUMENT) private document: Document) {}

    lightTheme(){
        const themeLink = this.document.getElementById('app-theme') as HTMLLinkElement;

        if (themeLink) {
            themeLink.href = 'light-theme.css';
        }
    }

    darkTheme() {
        const themeLink = this.document.getElementById('app-theme') as HTMLLinkElement;

        if (themeLink) {
            themeLink.href = 'dark-theme.css';
        }
    }
    
}