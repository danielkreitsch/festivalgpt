import {Component} from "@angular/core"
import {CommonModule} from "@angular/common"
import {InputTextModule} from 'primeng/inputtext';
import {FormsModule} from "@angular/forms";
import {AutoCompleteModule} from 'primeng/autocomplete';
import {EventService} from "../../services/event/event.service";
import {Festival} from "../../models/festival";
import {CardModule} from 'primeng/card';
import {ButtonModule} from "primeng/button";

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

  autocompleteFestivals(): void {
    setTimeout(() => {
      this.eventService.autocompleteFestivals(this.inputText!).subscribe({
        next: (festivals) => {
          this.suggestions = festivals.map((festival) => festival.name)
          console.log(festivals)
          if (festivals.some((festival) => festival.name === this.inputText)) {
            this.eventService.sendChatMessage(`Ich möchte zum Festival ${this.inputText} gehen!`).subscribe({
              next: (response: any) => {
                console.log(response.message)
                this.festival = festivals.find((festival) => festival.name === this.inputText)
                this.festival!!.description = response.message
              },
              error: () => {
                console.log("error")
              }
            })
          }
        },
        error: () => {
          console.log("error")
        }
      })
    }, 1)
  }
}
