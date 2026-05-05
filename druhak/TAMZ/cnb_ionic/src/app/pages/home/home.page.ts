import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { APIResponse, AppLanguage, CurrencyData } from '../../models/currency.model';
import { CurrencyService } from '../../services/currency.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.scss'],
})
export class HomePage implements OnInit, OnDestroy {
  selectedLanguage: AppLanguage = 'en';
  customDateEnabled = false;
  selectedDate = this.getTodayIsoDate();
  minDate = '2000-01-01';
  maxDate = this.getTodayIsoDate();

  currencies: CurrencyData[] = [];
  apiResponse: APIResponse | null = null;
  selectedCurrency: CurrencyData | null = null;
  selectedCurrencyCode = '';

  amountCZK = 1;
  convertedAmount = 0;

  loading = false;
  error = '';

  private activeRequest?: Subscription;

  constructor(private readonly currencyService: CurrencyService) {}

  ngOnInit(): void {
    this.loadRates();
  }

  ngOnDestroy(): void {
    this.activeRequest?.unsubscribe();
  }

  onLanguageChange(language: AppLanguage): void {
    if (this.selectedLanguage === language) {
      return;
    }

    this.selectedLanguage = language;
    this.loadRates();
  }

  onCustomDateEnabledChange(enabled: boolean): void {
    this.customDateEnabled = enabled;
    this.loadRates();
  }

  onDateChange(date: string): void {
    this.selectedDate = date;
    if (this.customDateEnabled) {
      this.loadRates();
    }
  }

  onAmountChange(event: Event | CustomEvent<{ value?: string | number | null }>): void {
    const customEvent = event as CustomEvent<{ value?: string | number | null }>;
    const rawValue = customEvent.detail?.value;
    const parsed = Number(String(rawValue ?? '').replace(',', '.'));
    this.amountCZK = Number.isFinite(parsed) ? parsed : 0;
    this.recalculateConversion();
  }

  onCurrencySelected(currency: CurrencyData): void {
    this.selectedCurrency = currency;
    this.selectedCurrencyCode = currency.code;
    this.recalculateConversion();
  }

  refreshRates(): void {
    this.loadRates();
  }

  private loadRates(): void {
    this.loading = true;
    this.error = '';
    this.activeRequest?.unsubscribe();

    const date = this.customDateEnabled ? this.selectedDate : null;

    this.activeRequest = this.currencyService
      .loadRates({
        language: this.selectedLanguage,
        useSse: !this.customDateEnabled,
        date,
      })
      .subscribe({
        next: (response) => {
          this.apiResponse = response;
          this.currencies = response.data ?? [];
          this.loading = false;
          this.syncSelectedCurrency();
          this.recalculateConversion();
        },
        error: (error) => {
          console.error(error);
          this.loading = false;
          this.error = 'Unable to load currency data. Please try again.';
        },
      });
  }

  private syncSelectedCurrency(): void {
    if (!this.currencies.length) {
      this.selectedCurrency = null;
      this.selectedCurrencyCode = '';
      return;
    }

    const existing = this.currencies.find((currency) => currency.code === this.selectedCurrencyCode);
    this.selectedCurrency = existing ?? this.currencies[0];
    this.selectedCurrencyCode = this.selectedCurrency.code;
  }

  private recalculateConversion(): void {
    if (!this.selectedCurrency) {
      this.convertedAmount = 0;
      return;
    }

    const unit = this.toNumber(this.selectedCurrency.unit);
    const rate = this.toNumber(this.selectedCurrency.rate);

    if (!rate) {
      this.convertedAmount = 0;
      return;
    }

    this.convertedAmount = (this.amountCZK * unit) / rate;
  }

  private toNumber(value: string): number {
    const normalized = `${value ?? ''}`.replace(/\s/g, '').replace(',', '.');
    const parsed = Number(normalized);
    return Number.isFinite(parsed) ? parsed : 0;
  }

  private getTodayIsoDate(): string {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}

