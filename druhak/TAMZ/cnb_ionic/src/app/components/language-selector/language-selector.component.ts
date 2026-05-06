import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AppLanguage } from '../../models/currency.model';

export interface LanguageSelectorLabels {
  title: string;
  english: string;
  czech: string;
}

@Component({
  selector: 'app-language-selector',
  templateUrl: './language-selector.component.html',
  styleUrls: ['./language-selector.component.scss'],
  standalone: false,
})
export class LanguageSelectorComponent {
  @Input() language: AppLanguage = 'en';
  @Input() disabled = false;
  @Input() labels: LanguageSelectorLabels = {
    title: 'Language',
    english: 'English',
    czech: 'Čeština',
  };

  @Output() languageChange = new EventEmitter<AppLanguage>();

  onSegmentChange(event: CustomEvent): void {
    const nextLanguage = event.detail.value as AppLanguage;
    if (nextLanguage) {
      this.languageChange.emit(nextLanguage);
    }
  }
}

