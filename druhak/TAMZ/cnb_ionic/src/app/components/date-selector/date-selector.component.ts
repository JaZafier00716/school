import { Component, EventEmitter, Input, Output } from '@angular/core';

export interface DateSelectorLabels {
  title: string;
  customLabel: string;
  description: string;
  liveMode: string;
  pickDate: string;
}

@Component({
  selector: 'app-date-selector',
  templateUrl: './date-selector.component.html',
  styleUrls: ['./date-selector.component.scss'],
  standalone: false,
})
export class DateSelectorComponent {
  @Input() customDateEnabled = false;
  @Input() selectedDate = '';
  @Input() minDate = '2000-01-01';
  @Input() maxDate = '';
  @Input() labels: DateSelectorLabels = {
    title: 'Date mode',
    customLabel: 'Use custom date',
    description: 'Disable this to stream live CNB rates via SSE.',
    liveMode: 'Live SSE mode',
    pickDate: 'Pick a date',
  };

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

  onDateButtonClick(): void {
    if (!this.selectedDate) {
      this.selectedDate = this.minDate;
    }
  }
}

