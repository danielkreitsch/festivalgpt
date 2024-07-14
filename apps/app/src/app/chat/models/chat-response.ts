import { Festival } from "./festival";

export interface ChatResponse {
    chatId: string;
    message: string;
    festivals: Festival[];
  }