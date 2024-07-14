import { Component } from "@angular/core"
import { RouterModule } from "@angular/router"
import { ThemeService } from "../theme-service";
import { SidebarModule } from 'primeng/sidebar';
import { ButtonModule } from "primeng/button";
import { ToolbarComponent } from "./core/containers/toolbar/toolbar.component";
import { HTTP_INTERCEPTORS } from "@angular/common/http";
import { ErrorInterceptor } from "./shared/interceptors/error/error.interceptor";
import { BaseInterceptor } from "./shared/interceptors/base/base.interceptor";

@Component({
  standalone: true,
  imports: [RouterModule, SidebarModule, ButtonModule,ToolbarComponent],
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrl: "./app.component.css",
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true },
    { provide: HTTP_INTERCEPTORS, useClass: BaseInterceptor, multi: true }  ],
})
export class AppComponent {

  constructor(private themeService: ThemeService) {}

  title = "app"
  sidebarVisible = false;

  changeTheme(lightMode: boolean) {
    if (lightMode) {
      this.themeService.lightTheme();
      return
    }
    this.themeService.darkTheme();
  }

  scrollToTop(): void {
    window.scrollTo({
      top:1000,
      behavior: 'smooth'
    });
  }
}
