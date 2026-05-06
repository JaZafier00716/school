import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { APIResponse, AppLanguage, CurrencyData } from '../../models/currency.model';
import { CurrencyService } from '../../services/currency.service';

const COPY = {
  en: {
    appTitle: 'Currency Converter',
    heroTitle: 'CZK exchange rates',
    heroSubtitle: 'Convert Czech koruna into live or historical foreign currencies.',
    amountLabel: 'Amount in CZK',
    historicalDateChip: 'Historical date:',
    liveModeChip: 'Live SSE mode',
    dataDateChip: 'Data date:',
    errorTitle: 'Could not load exchange rates',
    errorMessage: 'Unable to load currency data. Please try again.',
    errorRetry: 'Retry',
    loadingMessage: 'Loading exchange rates...',
    loadingSubMessage: 'Live rates are streamed with SSE when custom date is disabled.',
    language: {
      title: 'Language',
      english: 'English',
      czech: 'Čeština',
    },
    date: {
      title: 'Date mode',
      customLabel: 'Use custom date',
      description: 'Disable this to stream live CNB rates via SSE.',
      liveMode: 'Live SSE mode',
      pickDate: 'Pick a date',
    },
    currencySelector: {
      title: 'Choose currency',
      summaryEmpty: 'No currency selected',
      summaryHint: 'Open the search to pick a currency from the CNB list.',
      helperText: 'Search by country, currency label, code, unit, or rate.',
      modalTitle: 'Select currency',
      close: 'Close',
      searchPlaceholder: 'Search currencies',
      emptyResults: 'No currencies matched your search.',
      searchButton: 'Search',
    },
    conversionResult: {
      title: 'Conversion result',
      emptyState: 'Choose a currency to see the converted amount.',
      country: 'Country',
      currency: 'Currency',
      code: 'Code',
      exchangeRate: 'Exchange rate',
      date: 'Date',
      cached: 'Cached',
      yes: 'Yes',
      no: 'No',
    },
    currencyGrid: {
      title: 'All currencies',
      subtitle: 'Tap a tile to select the target currency.',
      empty: 'No currencies loaded yet.',
    },
  },
  cs: {
    appTitle: 'Převodník měn',
    heroTitle: 'Kurzovní lístek ČNB',
    heroSubtitle: 'Převod českých korun na zahraniční měny v přímém nebo historickém režimu.',
    amountLabel: 'Částka v CZK',
    historicalDateChip: 'Historické datum:',
    liveModeChip: 'Režim živého SSE',
    dataDateChip: 'Datum dat:',
    errorTitle: 'Kurzovní data se nepodařilo načíst',
    errorMessage: 'Nepodařilo se načíst měnová data. Zkuste to prosím znovu.',
    errorRetry: 'Zkusit znovu',
    loadingMessage: 'Načítám kurzovní data...',
    loadingSubMessage: 'Při vypnutém vlastním datu se používají živá SSE data.',
    language: {
      title: 'Jazyk',
      english: 'Angličtina',
      czech: 'Čeština',
    },
    date: {
      title: 'Režim data',
      customLabel: 'Použít vlastní datum',
      description: 'Vypněte pro načítání živých kurzů ČNB přes SSE.',
      liveMode: 'Živý SSE režim',
      pickDate: 'Vybrat datum',
    },
    currencySelector: {
      title: 'Vybrat měnu',
      summaryEmpty: 'Není vybrána žádná měna',
      summaryHint: 'Otevřete vyhledávání a vyberte měnu ze seznamu ČNB.',
      helperText: 'Hledejte podle země, názvu měny, kódu, jednotky nebo kurzu.',
      modalTitle: 'Výběr měny',
      close: 'Zavřít',
      searchPlaceholder: 'Hledat měny',
      emptyResults: 'Žádné měny neodpovídají hledání.',
      searchButton: 'Vyhledat',
    },
    conversionResult: {
      title: 'Výsledek převodu',
      emptyState: 'Vyberte měnu pro zobrazení přepočtené částky.',
      country: 'Země',
      currency: 'Měna',
      code: 'Kód',
      exchangeRate: 'Kurz',
      date: 'Datum',
      cached: 'Uloženo v mezipaměti',
      yes: 'Ano',
      no: 'Ne',
    },
    currencyGrid: {
      title: 'Všechny měny',
      subtitle: 'Klepnutím na dlaždici vyberte cílovou měnu.',
      empty: 'Zatím nebyly načteny žádné měny.',
    },
  },
} as const;

@Component({
  selector: 'app-home',
  templateUrl: './home.page.html',
  styleUrls: ['./home.page.scss'],
  standalone: false,
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

  constructor(
    private readonly currencyService: CurrencyService,
    private readonly cdr: ChangeDetectorRef,
  ) {}

  get copy() {
    return COPY[this.selectedLanguage];
  }

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
          this.cdr.detectChanges();
        },
        error: (error) => {
          console.error(error);
          this.loading = false;
          this.error = 'Unable to load currency data. Please try again.';
          this.cdr.detectChanges();
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

