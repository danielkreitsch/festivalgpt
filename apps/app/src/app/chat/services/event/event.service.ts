import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Festival } from '../../models/festival';
import {environment} from "../../../../environment";

@Injectable({
  providedIn: 'root'
})
export class EventService {
  constructor(private http: HttpClient) {
  }

  getFestivals(searchText: Festival): Observable<Festival> {
    return this.http.post<Festival>(`${environment.apiUrl}/chat/message`, searchText)
  }
}
