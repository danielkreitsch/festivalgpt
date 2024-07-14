import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Festival } from '../../models/festival';

@Injectable({
  providedIn: 'root'
})
export class EventService {

  constructor(private http: HttpClient) { 
  }

  getFestivals(searchText: Festival): Observable<Festival> {
    return this.http.post<Festival>('https://festivalgpt-backend.s1.glowdragon.de/api/chat/message', searchText)
  }
}
