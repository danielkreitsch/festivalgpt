import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Festival} from '../../models/festival';
import {environment} from "../../../../environment";

@Injectable({
  providedIn: 'root'
})
export class EventService {
  constructor(private http: HttpClient) {
  }

  autocompleteFestivals(query: string): Observable<Festival[]> {
    const params = new HttpParams().set('query', query)
    return this.http.get<Festival[]>(
      `${environment.apiUrl}/festivals/autocomplete`,
      {params}
    )
  }

  sendChatMessage(message: string): Observable<any> {
    return this.http.post<any>(
      `${environment.apiUrl}/chat/message`,
      {message}
    )
  }
}
