import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AppLanguage } from '../../models/currency.model';

@Component({
  selector: 'app-language-selector',
  templateUrl: './language-selector.component.html',
  styleUrls: ['./language-selector.component.scss'],
})
export class LanguageSelectorComponent {
  @Input() language: AppLanguage = 'en';
  @Input() disabled = false;

  @Output() languageChange = new EventEmitter<AppLanguage>();

  onSegmentChange(event: CustomEvent): void {
    const nextLanguage = event.detail.value as AppLanguage;
    if (nextLanguage) {
      this.languageChange.emit(nextLanguage);
    }
  }
}

