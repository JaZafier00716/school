import { HttpClientModule } from '@angular/common/http';
import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { IonicModule } from '@ionic/angular';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CurrencyGridComponent } from './components/currency-grid/currency-grid.component';
import { CurrencySelectorComponent } from './components/currency-selector/currency-selector.component';
import { DateSelectorComponent } from './components/date-selector/date-selector.component';
import { ErrorBannerComponent } from './components/error-banner/error-banner.component';
import { LanguageSelectorComponent } from './components/language-selector/language-selector.component';
import { LoadingStateComponent } from './components/loading-state/loading-state.component';
import { ConversionResultComponent } from './components/conversion-result/conversion-result.component';
import { HomePage } from './pages/home/home.page';

@NgModule({
  declarations: [
    AppComponent,
    HomePage,
    LanguageSelectorComponent,
    DateSelectorComponent,
    CurrencySelectorComponent,
    ConversionResultComponent,
    CurrencyGridComponent,
    LoadingStateComponent,
    ErrorBannerComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    IonicModule.forRoot({ mode: 'md' }),
    AppRoutingModule,
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class AppModule {}

