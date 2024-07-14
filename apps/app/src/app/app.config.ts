import { ApplicationConfig, importProvidersFrom, provideZoneChangeDetection } from "@angular/core"
import { provideRouter } from "@angular/router"
import { appRoutes } from "./app.routes"
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { provideHttpClient } from "@angular/common/http";


export const appConfig: ApplicationConfig = {
  providers: [importProvidersFrom(BrowserAnimationsModule), provideZoneChangeDetection({ eventCoalescing: true }), provideRouter(appRoutes), provideHttpClient()],
}
