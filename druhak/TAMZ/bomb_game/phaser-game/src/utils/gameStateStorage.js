const TOP_SCORES_KEY = "bomb-game-top-scores-v1";
const MUTE_KEY = "bomb-game-muted-v1";
const MAX_TOP_SCORES = 5;

function safeLocalStorageGet(key) {
    try {
        return window.localStorage.getItem(key);
    } catch (_error) {
        return null;
    }
}

function safeLocalStorageSet(key, value) {
    try {
        window.localStorage.setItem(key, value);
    } catch (_error) {
        // Ignore storage failures in private mode or restricted environments.
    }
}

export function loadTopScores() {
    const raw = safeLocalStorageGet(TOP_SCORES_KEY);
    if (!raw) {
        return [];
    }

    try {
        const parsed = JSON.parse(raw);
        if (!Array.isArray(parsed)) {
            return [];
        }

        return parsed
            .map((value) => Number(value) || 0)
            .filter((value) => value >= 0)
            .sort((a, b) => b - a)
            .slice(0, MAX_TOP_SCORES);
    } catch (_error) {
        return [];
    }
}

export function saveScore(score) {
    const numericScore = Math.max(0, Number(score) || 0);
    const topScores = loadTopScores();
    topScores.push(numericScore);

    const normalized = topScores
        .sort((a, b) => b - a)
        .slice(0, MAX_TOP_SCORES);

    safeLocalStorageSet(TOP_SCORES_KEY, JSON.stringify(normalized));
    return normalized;
}

export function loadMuted() {
    return safeLocalStorageGet(MUTE_KEY) === "true";
}

export function saveMuted(isMuted) {
    safeLocalStorageSet(MUTE_KEY, isMuted ? "true" : "false");
}

