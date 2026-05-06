import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CurrencyData } from '../../models/currency.model';

export interface CurrencySelectorLabels {
  title: string;
  summaryEmpty: string;
  summaryHint: string;
  helperText: string;
  modalTitle: string;
  close: string;
  searchPlaceholder: string;
  emptyResults: string;
  searchButton: string;
}

@Component({
  selector: 'app-currency-selector',
  templateUrl: './currency-selector.component.html',
  styleUrls: ['./currency-selector.component.scss'],
  standalone: false,
})
export class CurrencySelectorComponent {
  @Input() currencies: CurrencyData[] = [];
  @Input() selectedCurrency: CurrencyData | null = null;
  @Input() selectedCurrencyCode = '';
  @Input() loading = false;
  @Input() labels: CurrencySelectorLabels = {
    title: 'Choose currency',
    summaryEmpty: 'No currency selected',
    summaryHint: 'Open the search to pick a currency from the CNB list.',
    helperText: 'Search by country, currency label, code, unit, or rate.',
    modalTitle: 'Select currency',
    close: 'Close',
    searchPlaceholder: 'Search currencies',
    emptyResults: 'No currencies matched your search.',
    searchButton: 'Search',
  };

  @Output() currencyChange = new EventEmitter<CurrencyData>();

  isModalOpen = false;
  searchTerm = '';

  get filteredCurrencies(): CurrencyData[] {
    const term = this.searchTerm.trim().toLowerCase();
    if (!term) {
      return this.currencies;
    }

    return this.currencies.filter((currency) => {
      return [currency.country_label, currency.curr_label, currency.code, currency.rate, currency.unit]
        .join(' ')
        .toLowerCase()
        .includes(term);
    });
  }

  openModal(): void {
    if (!this.loading && this.currencies.length) {
      this.searchTerm = '';
      this.isModalOpen = true;
    }
  }

  closeModal(): void {
    this.isModalOpen = false;
  }

  selectCurrency(currency: CurrencyData): void {
    this.currencyChange.emit(currency);
    this.isModalOpen = false;
  }

  trackByCode(_: number, currency: CurrencyData): string {
    return currency.code;
  }
}

