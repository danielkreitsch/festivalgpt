import { Component } from "@angular/core"
import { CommonModule } from "@angular/common"
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from "@angular/forms";
import { AutoCompleteCompleteEvent, AutoCompleteModule } from 'primeng/autocomplete';
import { EventService } from "../../services/event/event.service";
import { Festival } from "../../models/festival";
import { CardModule } from 'primeng/card';
import { ButtonModule } from "primeng/button";

@Component({
  selector: "app-chat-home",
  standalone: true,
  imports: [CommonModule, InputTextModule, FormsModule, AutoCompleteModule, CardModule, ButtonModule],
  providers: [EventService],
  templateUrl: "./chat-home.component.html",
  styleUrl: "./chat-home.component.css",
})
export class ChatHomeComponent {
  festival: Festival | undefined;
  inputText: string | undefined;
  suggestions!: unknown[];

  constructor(private eventService: EventService) {

  }

  search(): void {
    const tempFestival: Festival = {message: this.inputText!, userId: '3fa85f64-5717-4562-b3fc-2c963f66afa6', chatId: '3fa85f64-5717-4562-b3fc-2c963f66afa6'}
    
    this.eventService.getFestivals(tempFestival).subscribe({
      next: (festival) => {
        this.suggestions = [(festival as any).message.slice(0, 15)]
        console.log(festival)
        this.festival = festival
      },
      error: () =>  {
        console.log("error")
      }
    })
  }

  searchFestival(event: AutoCompleteCompleteEvent): void {
    if (!this.inputText  || !event.originalEvent) return;

    const tempFestival: Festival = {message: this.inputText, userId: '3fa85f64-5717-4562-b3fc-2c963f66afa6', chatId: '3fa85f64-5717-4562-b3fc-2c963f66afa6'}
    
    this.eventService.getFestivals(tempFestival).subscribe({
      next: (festival) => {
        this.suggestions = [...(festival as any).message]
      },
      error: () =>  {
        console.log("error")
      }
    })
  }
}
