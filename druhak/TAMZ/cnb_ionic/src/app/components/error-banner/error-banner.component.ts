import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'app-error-banner',
  templateUrl: './error-banner.component.html',
  styleUrls: ['./error-banner.component.scss'],
})
export class ErrorBannerComponent {
  @Input() message = 'Something went wrong.';
  @Input() title = 'Could not load exchange rates';

  @Output() retry = new EventEmitter<void>();
}

