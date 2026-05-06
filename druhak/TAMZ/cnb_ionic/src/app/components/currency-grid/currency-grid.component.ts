import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CurrencyData } from '../../models/currency.model';

export interface CurrencyGridLabels {
  title: string;
  subtitle: string;
  empty: string;
}

@Component({
  selector: 'app-currency-grid',
  templateUrl: './currency-grid.component.html',
  styleUrls: ['./currency-grid.component.scss'],
  standalone: false,
})
export class CurrencyGridComponent {
  @Input() currencies: CurrencyData[] = [];
  @Input() selectedCurrencyCode = '';
  @Input() labels: CurrencyGridLabels = {
    title: 'All currencies',
    subtitle: 'Tap a tile to select the target currency.',
    empty: 'No currencies loaded yet.',
  };

  @Output() currencySelected = new EventEmitter<CurrencyData>();

  trackByCode(_: number, currency: CurrencyData): string {
    return currency.code;
  }

  selectCurrency(currency: CurrencyData): void {
    this.currencySelected.emit(currency);
  }
}

