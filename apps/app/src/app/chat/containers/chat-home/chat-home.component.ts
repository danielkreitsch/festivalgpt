import { Component } from "@angular/core"
import { CommonModule } from "@angular/common"
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from "@angular/forms";
import { AutoCompleteModule } from 'primeng/autocomplete';
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
  festivals: Festival[] = [];
  inputText: string | undefined;
  suggestions!: unknown[];

  constructor(private eventService: EventService) {

  }

  search(): void {
    this.eventService.autocompleteFestivals(this.inputText!).subscribe({
      next: (festivals) => {
        this.suggestions = festivals.map((festival) => festival.name)
        console.log(festivals)
        // TODO: Ausgewähltes Festival zu this.festivals hinzufügen
        // TODO: Wenn mehrere Festivals angezeigt werden, ist die Anzeige buggy
        // this.festivals = festivals
      },
      error: () =>  {
        console.log("error")
      }
    })
  }
}
