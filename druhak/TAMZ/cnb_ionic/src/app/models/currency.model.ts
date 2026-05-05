export interface CurrencyData {
  country_label: string;
  curr_label: string;
  unit: string;
  code: string;
  rate: string;
}

export interface APIResponse {
  date: string;
  order: string;
  data: CurrencyData[];
  labels: string[];
  lang: string;
  cached: boolean;
}

export type AppLanguage = 'en' | 'cs';

