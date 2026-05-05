import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CurrencyData } from '../../models/currency.model';

@Component({
  selector: 'app-currency-grid',
  templateUrl: './currency-grid.component.html',
  styleUrls: ['./currency-grid.component.scss'],
})
export class CurrencyGridComponent {
  @Input() currencies: CurrencyData[] = [];
  @Input() selectedCurrencyCode = '';

  @Output() currencySelected = new EventEmitter<CurrencyData>();

  trackByCode(_: number, currency: CurrencyData): string {
    return currency.code;
  }

  selectCurrency(currency: CurrencyData): void {
    this.currencySelected.emit(currency);
  }
}

