import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable, NgZone } from '@angular/core';
import { Observable } from 'rxjs';
import { APIResponse, AppLanguage } from '../models/currency.model';

@Injectable({
  providedIn: 'root',
})
export class CurrencyService {
  private readonly apiUrl = 'http://linedu.vsb.cz/~mor03/TAMZ/cnb_json.php';

  constructor(private readonly http: HttpClient, private readonly ngZone: NgZone) {}

  loadRates(options: {
    language: AppLanguage;
    useSse: boolean;
    date?: string | null;
  }): Observable<APIResponse> {
    if (options.date) {
      return this.loadHistoricalRates(options.language, options.date);
    }

    if (options.useSse && typeof EventSource !== 'undefined') {
      return this.loadLiveRatesViaSse(options.language);
    }

    return this.loadLiveRatesViaHttp(options.language);
  }

  private loadHistoricalRates(language: AppLanguage, date: string): Observable<APIResponse> {
    const params = new HttpParams().set('lang', language).set('date', date);
    return this.http.get<APIResponse>(this.apiUrl, { params });
  }

  private loadLiveRatesViaHttp(language: AppLanguage): Observable<APIResponse> {
    const params = new HttpParams().set('lang', language).set('sse', 'y');
    return this.http.get<APIResponse>(this.apiUrl, { params });
  }

  private loadLiveRatesViaSse(language: AppLanguage): Observable<APIResponse> {
    return new Observable<APIResponse>((observer) => {
      const url = `${this.apiUrl}?lang=${encodeURIComponent(language)}&sse=y`;
      const eventSource = new EventSource(url);
      let settled = false;

      eventSource.onmessage = (event) => {
        try {
          const payload = JSON.parse(event.data) as APIResponse;
          this.ngZone.run(() => observer.next(payload));
          settled = true;
        } catch (error) {
          this.ngZone.run(() => observer.error(error));
          eventSource.close();
        }
      };

      eventSource.onerror = (error) => {
        if (!settled) {
          this.ngZone.run(() => observer.error(error));
        }
        eventSource.close();
      };

      return () => eventSource.close();
    });
  }
}

