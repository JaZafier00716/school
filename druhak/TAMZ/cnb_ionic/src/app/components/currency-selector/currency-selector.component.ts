import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CurrencyData } from '../../models/currency.model';

@Component({
  selector: 'app-currency-selector',
  templateUrl: './currency-selector.component.html',
  styleUrls: ['./currency-selector.component.scss'],
})
export class CurrencySelectorComponent {
  @Input() currencies: CurrencyData[] = [];
  @Input() selectedCurrency: CurrencyData | null = null;
  @Input() selectedCurrencyCode = '';
  @Input() loading = false;

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

