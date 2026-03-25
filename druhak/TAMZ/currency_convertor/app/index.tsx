import React, { useEffect, useState, useCallback, useMemo } from "react";
import {
  View,
  Text,
  TextInput,
  ScrollView,
  TouchableOpacity,
  ActivityIndicator,
  Switch,
  StyleSheet,
  Modal,
  FlatList,
} from "react-native";

interface CurrencyData {
  country_label: string;
  curr_label: string;
  unit: string;
  code: string;
  rate: string;
}

interface APIResponse {
  date: string;
  order: string;
  data: CurrencyData[];
  labels: string[];
  lang: string;
  cached: boolean;
}

// Language translations
const translations = {
  en: {
    title: "Currency Converter",
    subtitle: "CZK Exchange Rates",
    languageLabel: "Language",
    languageEnglish: "English",
    languageCzech: "Czech",
    customDateLabel: "Use Custom Date",
    currencyLabel: "Select Currency",
    amountLabel: "Amount (CZK)",
    amountPlaceholder: "Enter amount in CZK",
    datePlaceholder: "YYYY-MM-DD",
    resultLabel: "Conversion Result",
    exchangeRateLabel: "Exchange Rate:",
    currencyListLabel: "All Available Currencies",
    dateInfoLabel: "Exchange rates as of:",
    countInfoLabel: "Total currencies:",
    loadingText: "Loading currencies...",
    selectLanguageModal: "Select Language",
    selectCurrencyModal: "Select Currency",
    searchCurrencyPlaceholder: "Search by country or code",
    failedLoadError: "Failed to load currencies:",
    closeButton: "Close",
  },
  cs: {
    title: "Měnový konvertor",
    subtitle: "Směnné kurzy ČNB",
    languageLabel: "Jazyk",
    languageEnglish: "Angličtina",
    languageCzech: "Čeština",
    customDateLabel: "Použít vlastní datum",
    currencyLabel: "Vyberte měnu",
    amountLabel: "Částka (CZK)",
    amountPlaceholder: "Zadejte částku v CZK",
    datePlaceholder: "RRRR-MM-DD",
    resultLabel: "Výsledek konverze",
    exchangeRateLabel: "Směnný kurz:",
    currencyListLabel: "Všechny dostupné měny",
    dateInfoLabel: "Směnné kurzy k:",
    countInfoLabel: "Počet měn:",
    loadingText: "Načítání měn...",
    selectLanguageModal: "Vyberte jazyk",
    selectCurrencyModal: "Vyberte měnu",
    searchCurrencyPlaceholder: "Hledat podle země nebo kódu",
    failedLoadError: "Nepodařilo se načíst měny:",
    closeButton: "Zavřít",
  },
};

export default function CurrencyConverter() {
  const [amount, setAmount] = useState("1");
  const [selectedCurrency, setSelectedCurrency] = useState("EUR");
  const [convertedAmount, setConvertedAmount] = useState("0");
  const [currencies, setCurrencies] = useState<CurrencyData[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedDate, setSelectedDate] = useState(
    new Date().toISOString().split("T")[0]
  );
  const [language, setLanguage] = useState("en");
  const [useCustomDate, setUseCustomDate] = useState(false);
  const [currencySearchQuery, setCurrencySearchQuery] = useState("");
  const [currencyModalVisible, setCurrencyModalVisible] = useState(false);
  const [languageModalVisible, setLanguageModalVisible] = useState(false);
  const [calendarModalVisible, setCalendarModalVisible] = useState(false);
  const [calendarMonth, setCalendarMonth] = useState(new Date());

  // Helper function to get days in month
  const getDaysInMonth = (date: Date) => {
    return new Date(date.getFullYear(), date.getMonth() + 1, 0).getDate();
  };

  // Helper function to get first day of month
  const getFirstDayOfMonth = (date: Date) => {
    return new Date(date.getFullYear(), date.getMonth(), 1).getDay();
  };

  // Handle calendar date selection
  const handleDateSelect = (day: number) => {
    const selected = new Date(
      calendarMonth.getFullYear(),
      calendarMonth.getMonth(),
      day
    );
    const dateStr = selected.toISOString().split("T")[0];
    setSelectedDate(dateStr);
    setCalendarModalVisible(false);
    // The useEffect will trigger the fetch because selectedDate changed
  };

  // Navigate to previous month
  const goToPreviousMonth = () => {
    setCalendarMonth(
      new Date(calendarMonth.getFullYear(), calendarMonth.getMonth() - 1)
    );
  };

  // Navigate to next month
  const goToNextMonth = () => {
    setCalendarMonth(
      new Date(calendarMonth.getFullYear(), calendarMonth.getMonth() + 1)
    );
  };

  // Render calendar days
  const renderCalendarDays = () => {
    const daysInMonth = getDaysInMonth(calendarMonth);
    const firstDay = getFirstDayOfMonth(calendarMonth);
    const days = [];

    // Empty cells for days before month starts
    for (let i = 0; i < firstDay; i++) {
      days.push(
        <View key={`empty-${i}`} style={styles.calendarDay} />
      );
    }

    // Days of month
    for (let day = 1; day <= daysInMonth; day++) {
      const dateStr = `${calendarMonth.getFullYear()}-${String(
        calendarMonth.getMonth() + 1
      ).padStart(2, "0")}-${String(day).padStart(2, "0")}`;
      const isSelected = dateStr === selectedDate;

      days.push(
        <TouchableOpacity
          key={day}
          style={[
            styles.calendarDay,
            isSelected && styles.calendarDaySelected,
          ]}
          onPress={() => handleDateSelect(day)}
        >
          <Text
            style={[
              styles.calendarDayText,
              isSelected && styles.calendarDayTextSelected,
            ]}
          >
            {day}
          </Text>
        </TouchableOpacity>
      );
    }

    return days;
  };

  const buildCnbUrl = useCallback(
    (date?: string, lang?: string, sse = false) => {
      const params: string[] = [];
      if (date) {
        params.push(`date=${date}`);
      }
      params.push(`lang=${lang || language}`);
      if (sse) {
        params.push("sse=y");
      }
      return `http://linedu.vsb.cz/~mor03/TAMZ/cnb_json.php?${params.join("&")}`;
    },
    [language]
  );

  const applyApiResponse = useCallback(
    (data: APIResponse) => {
      if (!data || !Array.isArray(data.data)) {
        throw new Error("Invalid API response format");
      }

      setCurrencies(data.data);

      if (data.data.length === 0) {
        return;
      }

      const hasSelectedCurrency = data.data.some(
        (item) => item.code === selectedCurrency
      );
      if (!hasSelectedCurrency) {
        setSelectedCurrency(data.data[0].code);
      }
    },
    [selectedCurrency]
  );

  // Fetch currency data from CNB API
  const fetchCurrencies = useCallback(async (date?: string, lang?: string) => {
    try {
      setLoading(true);
      setError(null);

      const url = buildCnbUrl(date, lang, false);

      const response = await fetch(url);
      
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      
      const data: APIResponse = await response.json();

      applyApiResponse(data);
      
      setError(null);
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : "Unknown error";
      const t = translations[language as keyof typeof translations] || translations.en;
      setError(`${t.failedLoadError} ${errorMessage}`);
      console.error("Error fetching currencies:", err);
    } finally {
      setLoading(false);
    }
  }, [buildCnbUrl, language, applyApiResponse]);

  const connectSse = useCallback(
    (date?: string, lang?: string) => {
      const EventSourceCtor = (globalThis as { EventSource?: any }).EventSource;
      if (!EventSourceCtor) {
        throw new Error("SSE is not supported on this platform");
      }

      const url = buildCnbUrl(date, lang, true);
      const eventSource = new EventSourceCtor(url);

      eventSource.onmessage = (event: { data: string }) => {
        try {
          const payload: APIResponse = JSON.parse(event.data);
          applyApiResponse(payload);
          setError(null);
          setLoading(false);
        } catch (parseError) {
          console.error("SSE parse error:", parseError);
        }
      };

      eventSource.onerror = () => {
        setLoading(false);
      };

      return eventSource as { close: () => void };
    },
    [buildCnbUrl, applyApiResponse]
  );

  // Fetch on mount and when date/language/source changes.
  // SSE is used automatically when custom date is OFF.
  useEffect(() => {
    const dateParam = useCustomDate ? selectedDate : undefined;
    const shouldUseSse = !useCustomDate;

    if (!shouldUseSse) {
      fetchCurrencies(dateParam, language);
      return;
    }

    setLoading(true);
    setError(null);
    let source: { close: () => void } | null = null;

    try {
      source = connectSse(dateParam, language);
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : "Unknown error";
      const errorPrefix =
        (translations[language as keyof typeof translations] || translations.en)
          .failedLoadError;
      setError(`${errorPrefix} ${errorMessage}`);
      fetchCurrencies(dateParam, language);
    }

    return () => {
      source?.close();
    };
  }, [
    useCustomDate,
    selectedDate,
    language,
    fetchCurrencies,
    connectSse,
  ]);

  // Calculate converted amount when amount or currency changes
  useEffect(() => {
    const current = currencies.find((c) => c.code === selectedCurrency);
    if (current && amount) {
      const numAmount = parseFloat(amount);
      if (!isNaN(numAmount)) {
        const rate = parseFloat(current.rate);
        const unit = parseFloat(current.unit);
        // CNB rate is "unit CUR = rate CZK"; convert CZK -> CUR accordingly.
        const result = (numAmount * unit) / rate;
        setConvertedAmount(result.toFixed(2));
      }
    }
  }, [amount, selectedCurrency, currencies]);

  const currentCurrency = currencies.find((c) => c.code === selectedCurrency);
  const t = translations[language as keyof typeof translations] || translations.en;
  const filteredCurrencies = useMemo(() => {
    const query = currencySearchQuery.trim().toLowerCase();
    if (!query) {
      return currencies;
    }

    return currencies.filter((currency) => {
      const country = currency.country_label.toLowerCase();
      const code = currency.code.toLowerCase();
      return country.includes(query) || code.includes(query);
    });
  }, [currencies, currencySearchQuery]);

  return (
    <ScrollView style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.title}>{t.title}</Text>
        <Text style={styles.subtitle}>{t.subtitle}</Text>
      </View>

      {error && <Text style={styles.error}>{error}</Text>}

      {loading ? (
        <View style={styles.loadingContainer}>
          <ActivityIndicator size="large" color="#0066CC" />
          <Text style={styles.loadingText}>{t.loadingText}</Text>
        </View>
      ) : (
        <>
          {/* Language Selection */}
          <View style={styles.section}>
            <Text style={styles.label}>{t.languageLabel}</Text>
            <TouchableOpacity
              style={styles.dropdownButton}
              onPress={() => setLanguageModalVisible(true)}
            >
              <Text style={styles.dropdownButtonText}>
                {language === "en" ? t.languageEnglish : t.languageCzech} ({language})
              </Text>
              <Text style={styles.dropdownArrow}>▼</Text>
            </TouchableOpacity>
            <Modal
              visible={languageModalVisible}
              transparent
              onRequestClose={() => setLanguageModalVisible(false)}
            >
              <TouchableOpacity
                style={styles.modalOverlay}
                onPress={() => setLanguageModalVisible(false)}
              >
                <View style={styles.modalContent}>
                  <Text style={styles.modalTitle}>{t.selectLanguageModal}</Text>
                  {["en", "cs"].map((lang) => (
                    <TouchableOpacity
                      key={lang}
                      style={styles.modalOption}
                      onPress={() => {
                        setLanguage(lang);
                        setLanguageModalVisible(false);
                      }}
                    >
                      <Text
                        style={[
                          styles.modalOptionText,
                          language === lang && styles.modalOptionTextActive,
                        ]}
                      >
                        {lang === "en" ? t.languageEnglish : t.languageCzech}
                      </Text>
                      {language === lang && (
                        <Text style={styles.checkmark}>✓</Text>
                      )}
                    </TouchableOpacity>
                  ))}
                </View>
              </TouchableOpacity>
            </Modal>
          </View>

          {/* Custom Date Selection */}
          <View style={styles.section}>
            <View style={styles.switchRow}>
              <Text style={styles.label}>{t.customDateLabel}</Text>
              <Switch
                value={useCustomDate}
                onValueChange={setUseCustomDate}
                trackColor={{ false: "#767577", true: "#81C784" }}
                thumbColor={useCustomDate ? "#0066CC" : "#f4f3f4"}
              />
            </View>
            {useCustomDate && (
              <>
                <TouchableOpacity
                  style={styles.dateButton}
                  onPress={() => setCalendarModalVisible(true)}
                >
                  <Text style={styles.dateButtonText}>{selectedDate}</Text>
                  <Text style={styles.calendarIcon}>📅</Text>
                </TouchableOpacity>
                
                <Modal
                  visible={calendarModalVisible}
                  transparent
                  onRequestClose={() => setCalendarModalVisible(false)}
                >
                  <TouchableOpacity
                    style={styles.modalOverlay}
                    onPress={() => setCalendarModalVisible(false)}
                  >
                    <View style={styles.calendarModal}>
                      {/* Calendar Header */}
                      <View style={styles.calendarHeader}>
                        <TouchableOpacity
                          style={styles.monthNavButton}
                          onPress={goToPreviousMonth}
                        >
                          <Text style={styles.monthNavText}>◀</Text>
                        </TouchableOpacity>
                        <Text style={styles.monthTitle}>
                          {calendarMonth.toLocaleDateString(
                            language === "en" ? "en-US" : "cs-CZ",
                            { month: "long", year: "numeric" }
                          )}
                        </Text>
                        <TouchableOpacity
                          style={styles.monthNavButton}
                          onPress={goToNextMonth}
                        >
                          <Text style={styles.monthNavText}>▶</Text>
                        </TouchableOpacity>
                      </View>

                      {/* Day labels */}
                      <View style={styles.calendarWeekDays}>
                        {["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"].map(
                          (day) => (
                            <Text key={day} style={styles.calendarWeekDay}>
                              {day}
                            </Text>
                          )
                        )}
                      </View>

                      {/* Calendar days grid */}
                      <View style={styles.calendarDaysGrid}>
                        {renderCalendarDays()}
                      </View>

                      {/* Close button */}
                      <TouchableOpacity
                        style={styles.calendarCloseButton}
                        onPress={() => setCalendarModalVisible(false)}
                      >
                        <Text style={styles.calendarCloseButtonText}>
                          {t.closeButton || "Close"}
                        </Text>
                      </TouchableOpacity>
                    </View>
                  </TouchableOpacity>
                </Modal>
              </>
            )}
          </View>

          {/* Currency Selection */}
          <View style={styles.section}>
            <Text style={styles.label}>{t.currencyLabel}</Text>
            <TouchableOpacity
              style={styles.dropdownButton}
              onPress={() => setCurrencyModalVisible(true)}
            >
              <Text style={styles.dropdownButtonText}>
                {currentCurrency
                  ? `${currentCurrency.country_label} (${selectedCurrency})`
                  : t.currencyLabel}
              </Text>
              <Text style={styles.dropdownArrow}>▼</Text>
            </TouchableOpacity>
            <Modal
              visible={currencyModalVisible}
              transparent
              onRequestClose={() => setCurrencyModalVisible(false)}
            >
              <TouchableOpacity
                style={styles.modalOverlay}
                onPress={() => setCurrencyModalVisible(false)}
              >
                <View style={styles.modalContent}>
                  <Text style={styles.modalTitle}>{t.selectCurrencyModal}</Text>
                  <FlatList
                    data={filteredCurrencies}
                    keyExtractor={(item) => item.code}
                    renderItem={({ item }) => (
                      <TouchableOpacity
                        style={styles.modalOption}
                        onPress={() => {
                          setSelectedCurrency(item.code);
                          setCurrencyModalVisible(false);
                        }}
                      >
                        <View style={styles.currencyOptionContent}>
                          <Text style={styles.currencyOptionCode}>
                            {item.code}
                          </Text>
                          <Text style={styles.currencyOptionCountry}>
                            {item.country_label}
                          </Text>
                        </View>
                        {selectedCurrency === item.code && (
                          <Text style={styles.checkmark}>✓</Text>
                        )}
                      </TouchableOpacity>
                    )}
                    scrollEnabled
                    style={styles.modalList}
                  />
                </View>
              </TouchableOpacity>
            </Modal>
          </View>

          {/* Conversion Result */}
          {currentCurrency && (
            <View style={styles.resultSection}>
              <View style={styles.resultCard}>
                <Text style={styles.resultLabel}>{t.resultLabel}</Text>
                <View style={styles.resultRow}>
                  <View style={styles.resultColumn}>
                    <TextInput
                      style={styles.resultValueInput}
                      value={amount}
                      onChangeText={setAmount}
                      placeholder="0"
                      keyboardType="decimal-pad"
                    />
                    <Text style={styles.resultCurrency}>CZK</Text>
                  </View>
                  <Text style={styles.resultEquals}>=</Text>
                  <View style={styles.resultColumn}>
                    <Text style={styles.resultValue}>{convertedAmount}</Text>
                    <Text style={styles.resultCurrency}>
                      {selectedCurrency}
                    </Text>
                  </View>
                </View>

                {/* Exchange Rate Info */}
                <View style={styles.rateInfo}>
                  <Text style={styles.rateLabel}>{t.exchangeRateLabel}</Text>
                  <Text style={styles.rateValue}>
                    1 {currentCurrency.unit === "1" ? selectedCurrency : `${currentCurrency.unit} ${selectedCurrency}`} = {currentCurrency.rate} CZK
                  </Text>
                  <Text style={styles.countryLabel}>
                    {currentCurrency.country_label} -{" "}
                    {currentCurrency.curr_label}
                  </Text>
                </View>
              </View>
            </View>
          )}

          {/* Currency List */}
          <View style={styles.section}>
            <Text style={styles.label}>{t.currencyListLabel}</Text>
            <TextInput
              style={[styles.input, styles.searchInput]}
              placeholder={t.searchCurrencyPlaceholder}
              value={currencySearchQuery}
              onChangeText={setCurrencySearchQuery}
              autoCapitalize="none"
            />
            <View style={styles.currencyList}>
              {filteredCurrencies.map((currency) => (
                <TouchableOpacity
                  key={currency.code}
                  style={[
                    styles.currencyItem,
                    selectedCurrency === currency.code &&
                      styles.currencyItemActive,
                  ]}
                  onPress={() => setSelectedCurrency(currency.code)}
                >
                  <Text
                    style={[
                      styles.currencyCode,
                      selectedCurrency === currency.code &&
                        styles.currencyCodeActive,
                    ]}
                  >
                    {currency.code}
                  </Text>
                  <Text style={styles.currencyName}>
                    {currency.country_label}
                  </Text>
                  <Text style={styles.currencyRate}>{currency.rate} CZK</Text>
                </TouchableOpacity>
              ))}
            </View>
          </View>

          {/* Data Info */}
          <View style={styles.dataInfo}>
            <Text style={styles.infoText}>
              {t.dateInfoLabel} {selectedDate}
            </Text>
            <Text style={styles.infoText}>
              {t.countInfoLabel} {currencies.length}
            </Text>
          </View>
        </>
      )}
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "#f5f5f5",
    paddingHorizontal: 16,
    paddingTop: 20,
    paddingBottom: 30,
  },
  header: {
    marginBottom: 30,
    alignItems: "center",
  },
  title: {
    fontSize: 28,
    fontWeight: "bold",
    color: "#333",
    marginBottom: 5,
  },
  subtitle: {
    fontSize: 16,
    color: "#666",
  },
  section: {
    marginBottom: 20,
    backgroundColor: "white",
    padding: 16,
    borderRadius: 8,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
  },
  label: {
    fontSize: 14,
    fontWeight: "600",
    color: "#333",
    marginBottom: 10,
  },
  dropdownButton: {
    borderWidth: 1,
    borderColor: "#ddd",
    borderRadius: 6,
    padding: 12,
    backgroundColor: "#fafafa",
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  dropdownButtonText: {
    fontSize: 16,
    color: "#333",
  },
  dropdownArrow: {
    fontSize: 12,
    color: "#999",
  },
  pickerContainer: {
    borderWidth: 1,
    borderColor: "#ddd",
    borderRadius: 6,
    overflow: "hidden",
    backgroundColor: "#fafafa",
  },
  picker: {
    height: 50,
    color: "#333",
  },
  input: {
    borderWidth: 1,
    borderColor: "#ddd",
    borderRadius: 6,
    padding: 12,
    fontSize: 16,
    backgroundColor: "#fafafa",
    color: "#333",
  },
  searchInput: {
    marginBottom: 12,
  },
  switchRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 10,
  },
  resultSection: {
    marginBottom: 20,
  },
  resultCard: {
    backgroundColor: "white",
    padding: 20,
    borderRadius: 8,
    shadowColor: "#000",
    shadowOffset: { width: 0, height: 2 },
    shadowOpacity: 0.1,
    shadowRadius: 4,
    elevation: 3,
    borderLeftWidth: 4,
    borderLeftColor: "#0066CC",
  },
  resultLabel: {
    fontSize: 14,
    fontWeight: "600",
    color: "#666",
    marginBottom: 16,
  },
  resultRow: {
    flexDirection: "row",
    justifyContent: "space-around",
    alignItems: "center",
    marginBottom: 20,
  },
  resultColumn: {
    alignItems: "center",
  },
  resultValue: {
    fontSize: 24,
    fontWeight: "bold",
    color: "#0066CC",
  },
  resultValueInput: {
    fontSize: 24,
    fontWeight: "bold",
    color: "#0066CC",
    minWidth: 90,
    textAlign: "center",
    borderBottomWidth: 1,
    borderBottomColor: "#cfe3ff",
    paddingVertical: 2,
  },
  resultCurrency: {
    fontSize: 12,
    color: "#666",
    marginTop: 4,
  },
  resultEquals: {
    fontSize: 24,
    fontWeight: "bold",
    color: "#999",
  },
  rateInfo: {
    borderTopWidth: 1,
    borderTopColor: "#eee",
    paddingTop: 16,
  },
  rateLabel: {
    fontSize: 12,
    color: "#999",
    marginBottom: 4,
  },
  rateValue: {
    fontSize: 16,
    fontWeight: "600",
    color: "#333",
    marginBottom: 8,
  },
  countryLabel: {
    fontSize: 12,
    color: "#999",
  },
  currencyList: {
    flexDirection: "row",
    flexWrap: "wrap",
    justifyContent: "space-between",
  },
  currencyItem: {
    width: "48.5%",
    backgroundColor: "#f9f9f9",
    padding: 12,
    marginBottom: 8,
    borderRadius: 6,
    borderWidth: 1,
    borderColor: "#e0e0e0",
    alignItems: "center",
  },
  currencyItemActive: {
    backgroundColor: "#e3f2fd",
    borderColor: "#0066CC",
  },
  currencyCode: {
    fontSize: 14,
    fontWeight: "bold",
    color: "#333",
    marginBottom: 4,
  },
  currencyCodeActive: {
    color: "#0066CC",
  },
  currencyName: {
    fontSize: 12,
    color: "#666",
    marginBottom: 6,
    textAlign: "center",
  },
  currencyRate: {
    fontSize: 11,
    color: "#999",
  },
  loadingContainer: {
    alignItems: "center",
    justifyContent: "center",
    paddingVertical: 40,
  },
  loadingText: {
    marginTop: 12,
    fontSize: 16,
    color: "#666",
  },
  error: {
    color: "#d32f2f",
    fontSize: 14,
    padding: 12,
    backgroundColor: "#ffebee",
    borderRadius: 6,
    marginBottom: 20,
  },
  dataInfo: {
    backgroundColor: "#f0f7ff",
    padding: 12,
    borderRadius: 6,
    borderLeftWidth: 3,
    borderLeftColor: "#0066CC",
    marginTop: 20,
    marginBottom: 10,
  },
  infoText: {
    fontSize: 12,
    color: "#0066CC",
    marginBottom: 4,
  },
  modalOverlay: {
    flex: 1,
    backgroundColor: "rgba(0, 0, 0, 0.5)",
    justifyContent: "center",
    alignItems: "center",
  },
  modalContent: {
    backgroundColor: "white",
    borderRadius: 12,
    maxHeight: "80%",
    width: "85%",
    paddingVertical: 16,
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: "bold",
    color: "#333",
    paddingHorizontal: 16,
    paddingBottom: 12,
    borderBottomWidth: 1,
    borderBottomColor: "#eee",
  },
  modalOption: {
    paddingVertical: 12,
    paddingHorizontal: 16,
    borderBottomWidth: 1,
    borderBottomColor: "#f0f0f0",
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  modalOptionText: {
    fontSize: 16,
    color: "#333",
  },
  modalOptionTextActive: {
    color: "#0066CC",
    fontWeight: "600",
  },
  checkmark: {
    fontSize: 18,
    color: "#0066CC",
    fontWeight: "bold",
  },
  modalList: {
    maxHeight: 400,
  },
  currencyOptionContent: {
    flex: 1,
  },
  currencyOptionCode: {
    fontSize: 16,
    fontWeight: "bold",
    color: "#0066CC",
    marginBottom: 2,
  },
  currencyOptionCountry: {
    fontSize: 14,
    color: "#666",
  },
  dateButton: {
    borderWidth: 1,
    borderColor: "#ddd",
    borderRadius: 6,
    padding: 12,
    backgroundColor: "#fafafa",
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
  },
  dateButtonText: {
    fontSize: 16,
    color: "#333",
    fontWeight: "600",
  },
  calendarIcon: {
    fontSize: 20,
  },
  calendarModal: {
    backgroundColor: "white",
    borderRadius: 12,
    padding: 20,
    width: "90%",
    maxWidth: 400,
  },
  calendarHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 20,
  },
  monthNavButton: {
    padding: 8,
  },
  monthNavText: {
    fontSize: 20,
    color: "#0066CC",
    fontWeight: "bold",
  },
  monthTitle: {
    fontSize: 18,
    fontWeight: "bold",
    color: "#333",
  },
  calendarWeekDays: {
    flexDirection: "row",
    justifyContent: "space-around",
    marginBottom: 10,
  },
  calendarWeekDay: {
    fontSize: 12,
    fontWeight: "600",
    color: "#999",
    width: "14.28%",
    textAlign: "center",
  },
  calendarDaysGrid: {
    flexDirection: "row",
    flexWrap: "wrap",
    marginBottom: 20,
  },
  calendarDay: {
    width: "14.28%",
    aspectRatio: 1,
    justifyContent: "center",
    alignItems: "center",
    borderRadius: 4,
    marginBottom: 4,
  },
  calendarDayText: {
    fontSize: 14,
    color: "#333",
    fontWeight: "500",
  },
  calendarDaySelected: {
    backgroundColor: "#0066CC",
  },
  calendarDayTextSelected: {
    color: "white",
    fontWeight: "bold",
  },
  calendarCloseButton: {
    backgroundColor: "#0066CC",
    padding: 12,
    borderRadius: 6,
    alignItems: "center",
  },
  calendarCloseButtonText: {
    color: "white",
    fontSize: 16,
    fontWeight: "600",
  },
});
