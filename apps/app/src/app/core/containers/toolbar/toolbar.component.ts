import { Component, EventEmitter, HostListener, Output } from "@angular/core"
import { CommonModule } from "@angular/common"
import { ToolbarModule } from 'primeng/toolbar';
import { ToggleButtonChangeEvent, ToggleButtonModule } from 'primeng/togglebutton';

@Component({
  selector: "app-toolbar",
  standalone: true,
  imports: [CommonModule, ToolbarModule, ToggleButtonModule],
  templateUrl: "./toolbar.component.html",
  styleUrl: "./toolbar.component.css",
})
export class ToolbarComponent {
  @Output() lightMode = new EventEmitter<boolean>();

  onChangeTheme(event: ToggleButtonChangeEvent): void{
    this.lightMode.emit(event.checked)
  }
  
  @HostListener('window:scroll', ['$event'])
  onWindowScroll() {
      const element = document.querySelector('.navbar') as HTMLElement;
      if (window.pageYOffset > 900) {
        element.classList.remove('bg-transparent');
      } else {
        element.classList.add('bg-transparent');
      }
    }
}
