import { Route } from "@angular/router"
import { ChatHomeComponent } from "./chat/containers/chat-home/chat-home.component"

export const appRoutes: Route[] = [
    { path: '', component: ChatHomeComponent},
]
