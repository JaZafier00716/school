import { Component, Input } from '@angular/core';
import { APIResponse, CurrencyData } from '../../models/currency.model';

@Component({
  selector: 'app-conversion-result',
  templateUrl: './conversion-result.component.html',
  styleUrls: ['./conversion-result.component.scss'],
})
export class ConversionResultComponent {
  @Input() selectedCurrency: CurrencyData | null = null;
  @Input() apiResponse: APIResponse | null = null;
  @Input() amountCZK = 0;
  @Input() convertedAmount = 0;
}

