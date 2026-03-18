import { decode as decodeBase64 } from 'base-64';
import { useMemo, useState } from 'react';
import {
  ActivityIndicator,
  Pressable,
  ScrollView,
  StyleSheet,
  TextInput,
  View,
} from 'react-native';

import { ThemedText } from '@/components/themed-text';
import { ThemedView } from '@/components/themed-view';

const DEFAULT_URL = 'https://homel.vsb.cz/~mor03/TAMZ/TAMZ22.php';

const atob = globalThis.atob ?? decodeBase64;

type CallState = {
  requestUrl: string;
  status: number;
  body: string;
};

export default function HomeScreen() {
  const fixedTextColor = '#111827';
  const [username, setUsername] = useState('');
  const [scriptUrl, setScriptUrl] = useState(DEFAULT_URL);
  const [encodedToken, setEncodedToken] = useState('');
  const [decodedToken, setDecodedToken] = useState('');
  const [authorizedResponse, setAuthorizedResponse] = useState<CallState | null>(null);
  const [errorMessage, setErrorMessage] = useState('');
  const [loading, setLoading] = useState(false);

  const hasToken = encodedToken.length > 0;

  const canSubmit = useMemo(
    () => username.trim().length > 0 && scriptUrl.trim().length > 0 && !loading,
    [loading, scriptUrl, username]
  );

  const requestToken = async () => {
    const trimmedUser = username.trim();
    const trimmedUrl = scriptUrl.trim();

    if (!trimmedUser) {
      setErrorMessage('Username is required.');
      return;
    }

    let parsedUrl: URL;
    try {
      parsedUrl = new URL(trimmedUrl);
    } catch {
      setErrorMessage('Script URL is not valid.');
      return;
    }

    setLoading(true);
    setErrorMessage('');
    setEncodedToken('');
    setDecodedToken('');
    setAuthorizedResponse(null);

    try {
      const timestamp = Date.now().toString();

      const tokenRequestUrl = new URL(parsedUrl.toString());
      tokenRequestUrl.searchParams.set('user', trimmedUser);
      tokenRequestUrl.searchParams.set('timestamp', timestamp);

      const tokenResponse = await fetch(tokenRequestUrl.toString(), {
        method: 'GET',
        headers: { Accept: 'text/plain' },
      });

      const tokenBody = (await tokenResponse.text()).trim();
      if (!tokenResponse.ok) {
        throw new Error(`Token request failed (${tokenResponse.status}): ${tokenBody}`);
      }

      const decoded = atob(tokenBody);
      setEncodedToken(tokenBody);
      setDecodedToken(decoded);
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage('Unexpected error while processing request.');
      }
    } finally {
      setLoading(false);
    }
  };

  const testApiKey = async () => {
    const trimmedUrl = scriptUrl.trim();

    if (!encodedToken) {
      setErrorMessage('Fetch token first.');
      return;
    }

    let parsedUrl: URL;
    try {
      parsedUrl = new URL(trimmedUrl);
    } catch {
      setErrorMessage('Script URL is not valid.');
      return;
    }

    setLoading(true);
    setErrorMessage('');
    setAuthorizedResponse(null);

    try {
      const authRequestUrl = new URL(parsedUrl.toString());
      authRequestUrl.searchParams.set('timestamp', Date.now().toString());

      const authResponse = await fetch(authRequestUrl.toString(), {
        method: 'GET',
        headers: {
          Accept: 'text/plain',
          Authorization: `Bearer ${encodedToken}`,
        },
      });

      const authBody = (await authResponse.text()).trim();
      setAuthorizedResponse({
        requestUrl: authRequestUrl.toString(),
        status: authResponse.status,
        body: authBody,
      });

      if (!authResponse.ok) {
        throw new Error(`Authorized request failed (${authResponse.status}): ${authBody}`);
      }
    } catch (error) {
      if (error instanceof Error) {
        setErrorMessage(error.message);
      } else {
        setErrorMessage('Unexpected error while processing request.');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <ScrollView contentContainerStyle={styles.screen} keyboardShouldPersistTaps="handled">
      <ThemedView style={styles.card} lightColor="#f8fafc" darkColor="#f8fafc">
        <ThemedText type="title" lightColor={fixedTextColor} darkColor={fixedTextColor}>
          Token Fetch Task
        </ThemedText>
        <ThemedText lightColor={fixedTextColor} darkColor={fixedTextColor}>
          Enter your username and script URL, then run both required requests.
        </ThemedText>

        <ThemedText type="defaultSemiBold" lightColor={fixedTextColor} darkColor={fixedTextColor}>
          Username
        </ThemedText>
        <TextInput
          value={username}
          onChangeText={(value) => {
            setUsername(value);
            setEncodedToken('');
            setDecodedToken('');
            setAuthorizedResponse(null);
            setErrorMessage('');
          }}
          autoCapitalize="none"
          autoCorrect={false}
          style={styles.input}
          placeholder="e.g. xnovak99"
        />

        <ThemedText type="defaultSemiBold" lightColor={fixedTextColor} darkColor={fixedTextColor}>
          Script URL
        </ThemedText>
        <TextInput
          value={scriptUrl}
          onChangeText={(value) => {
            setScriptUrl(value);
            setEncodedToken('');
            setDecodedToken('');
            setAuthorizedResponse(null);
            setErrorMessage('');
          }}
          autoCapitalize="none"
          autoCorrect={false}
          style={styles.input}
        />

        <Pressable
          style={[styles.button, !canSubmit && styles.buttonDisabled]}
          onPress={hasToken ? testApiKey : requestToken}
          disabled={!canSubmit}>
          {loading ? (
            <ActivityIndicator color="#ffffff" />
          ) : (
            <ThemedText style={styles.buttonText}>{hasToken ? 'Test API Key' : 'Send Request'}</ThemedText>
          )}
        </Pressable>

        <ThemedText type="defaultSemiBold" lightColor={fixedTextColor} darkColor={fixedTextColor}>
          Decoded token (read-only)
        </ThemedText>
        <TextInput value={decodedToken} editable={false} selectTextOnFocus={false} style={[styles.input, styles.readOnly]} />


        {authorizedResponse ? (
          <View style={styles.section}>
            <ThemedText type="defaultSemiBold" lightColor={fixedTextColor} darkColor={fixedTextColor}>
              Authorized request result
            </ThemedText>
            <ThemedText lightColor={fixedTextColor} darkColor={fixedTextColor}>
              Status: {authorizedResponse.status}
            </ThemedText>
            <ThemedText lightColor={fixedTextColor} darkColor={fixedTextColor}>
              URL: {authorizedResponse.requestUrl}
            </ThemedText>
            <ThemedText lightColor={fixedTextColor} darkColor={fixedTextColor}>
              {authorizedResponse.body || '(empty body)'}
            </ThemedText>
          </View>
        ) : null}

        {errorMessage ? (
          <View style={styles.errorBox}>
            <ThemedText style={styles.errorText}>{errorMessage}</ThemedText>
          </View>
        ) : null}
      </ThemedView>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  screen: {
    padding: 16,
  },
  card: {
    borderRadius: 12,
    gap: 10,
    padding: 16,
  },
  input: {
    backgroundColor: '#ffffff',
    borderColor: '#9ca3af',
    borderRadius: 8,
    borderWidth: 1,
    color: '#111827',
    paddingHorizontal: 12,
    paddingVertical: 10,
  },
  readOnly: {
    backgroundColor: '#f3f4f6',
  },
  button: {
    alignItems: 'center',
    backgroundColor: '#2563eb',
    borderRadius: 8,
    paddingVertical: 12,
  },
  buttonDisabled: {
    backgroundColor: '#93c5fd',
  },
  buttonText: {
    color: '#ffffff',
  },
  section: {
    borderColor: '#d1d5db',
    borderRadius: 8,
    borderWidth: 1,
    gap: 6,
    padding: 10,
  },
  errorBox: {
    backgroundColor: '#fee2e2',
    borderColor: '#fca5a5',
    borderRadius: 8,
    borderWidth: 1,
    padding: 10,
  },
  errorText: {
    color: '#991b1b',
  },
});
