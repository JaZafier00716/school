import { Component, Input } from '@angular/core';
import { APIResponse, CurrencyData } from '../../models/currency.model';

export interface ConversionResultLabels {
  title: string;
  emptyState: string;
  country: string;
  currency: string;
  code: string;
  exchangeRate: string;
  date: string;
  cached: string;
  yes: string;
  no: string;
}

@Component({
  selector: 'app-conversion-result',
  templateUrl: './conversion-result.component.html',
  styleUrls: ['./conversion-result.component.scss'],
  standalone: false,
})
export class ConversionResultComponent {
  @Input() selectedCurrency: CurrencyData | null = null;
  @Input() apiResponse: APIResponse | null = null;
  @Input() amountCZK = 0;
  @Input() convertedAmount = 0;
  @Input() labels: ConversionResultLabels = {
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
  };
}

