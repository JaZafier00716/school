import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-error-banner',
  templateUrl: './error-banner.component.html',
  styleUrls: ['./error-banner.component.scss'],
  standalone: false,
})
export class ErrorBannerComponent {
  @Input() message = 'Something went wrong.';
  @Input() title = 'Could not load exchange rates';
  @Input() retryLabel = 'Retry';

  @Output() retry = new EventEmitter<void>();
}

