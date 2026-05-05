import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-date-selector',
  templateUrl: './date-selector.component.html',
  styleUrls: ['./date-selector.component.scss'],
})
export class DateSelectorComponent {
  @Input() customDateEnabled = false;
  @Input() selectedDate = '';
  @Input() minDate = '2000-01-01';
  @Input() maxDate = '';

  @Output() customDateEnabledChange = new EventEmitter<boolean>();
  @Output() selectedDateChange = new EventEmitter<string>();

  onToggleChange(event: CustomEvent): void {
    this.customDateEnabledChange.emit(Boolean(event.detail.checked));
  }

  onDateChange(event: CustomEvent): void {
    const nextValue = `${event.detail.value ?? ''}`;
    if (nextValue) {
      this.selectedDateChange.emit(nextValue.slice(0, 10));
    }
  }
}

