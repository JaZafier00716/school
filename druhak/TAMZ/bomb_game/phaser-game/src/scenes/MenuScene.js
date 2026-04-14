import Phaser, { Scene } from "phaser";
import { loadMuted, loadTopScores, saveMuted } from "../utils/gameStateStorage";

export class MenuScene extends Scene {
    constructor() {
        super("MenuScene");
        this.menuMusic = null;
        this.muteButtonLabel = null;
    }

    preload() {
        this.load.audio("main-background-music", "sounds/background.mp3");
    }

    create() {
        this.cameras.main.fadeIn(350, 0, 0, 0);

        this.sound.mute = loadMuted();

        this.add.rectangle(0, 0, this.scale.width, this.scale.height, 0x0b1020).setOrigin(0, 0);

        const panel = this.add.rectangle(
            this.scale.width * 0.5,
            this.scale.height * 0.5,
            620,
            430,
            0x111827,
            0.95
        );
        panel.setStrokeStyle(3, 0x60a5fa, 0.75);

        this.add.text(this.scale.width * 0.5, 105, "Bomb Survival", {
            fontSize: "44px",
            color: "#f8fafc",
            fontStyle: "bold"
        }).setOrigin(0.5);

        this.createButton(this.scale.width * 0.5, 210, "Start Game", async () => {
            await this.ensureAudioReadyFromGesture();
            this.stopMenuMusic();
            this.scene.start("MainScene");
        });

        this.muteButtonLabel = this.createButton(
            this.scale.width * 0.5,
            275,
            this.sound.mute ? "Unmute" : "Mute",
            () => {
                this.sound.mute = !this.sound.mute;
                saveMuted(this.sound.mute);
                this.muteButtonLabel.setText(this.sound.mute ? "Unmute" : "Mute");
            }
        );

        this.add.text(this.scale.width * 0.5, 335, "Top 5 Scores", {
            fontSize: "24px",
            color: "#bfdbfe",
            fontStyle: "bold"
        }).setOrigin(0.5);

        const scores = loadTopScores();
        const scoreText = scores.length > 0
            ? scores.map((score, index) => `${index + 1}. ${score}`).join("\n")
            : "No scores yet";

        this.add.text(this.scale.width * 0.5, 350, scoreText, {
            fontSize: "22px",
            color: "#e5e7eb",
            align: "center"
        }).setOrigin(0.5, 0);

        if (this.cache.audio.exists("main-background-music")) {
            this.menuMusic = this.sound.add("main-background-music", {
                loop: true,
                volume: 0.12
            });
            this.menuMusic.play();
        }

        this.events.once(Phaser.Scenes.Events.SHUTDOWN, () => this.stopMenuMusic());
        this.events.once(Phaser.Scenes.Events.DESTROY, () => this.stopMenuMusic());
    }

    createButton(x, y, label, onClick) {
        const button = this.add.rectangle(x, y, 250, 48, 0x1d4ed8, 0.95)
            .setStrokeStyle(2, 0x93c5fd)
            .setInteractive({ useHandCursor: true });

        const buttonLabel = this.add.text(x, y, label, {
            fontSize: "22px",
            color: "#f8fafc",
            fontStyle: "bold"
        }).setOrigin(0.5);

        button.on("pointerover", () => button.setFillStyle(0x2563eb, 0.98));
        button.on("pointerout", () => button.setFillStyle(0x1d4ed8, 0.95));
        button.on("pointerdown", onClick);

        return buttonLabel;
    }

    async ensureAudioReadyFromGesture() {
        const contextState = this.sound.context?.state;
        const soundOk = !this.sound.locked && (!contextState || contextState === "running");
        if (soundOk) {
            return;
        }

        if (this.sound.context?.resume) {
            try {
                await this.sound.context.resume();
            } catch (_error) {
                // Keep going; Phaser unlock can still succeed on some browsers.
            }
        }

        if (this.sound.locked && this.sound.unlock) {
            this.sound.unlock();
        }
    }

    stopMenuMusic() {
        if (!this.menuMusic) {
            return;
        }

        this.menuMusic.stop();
        this.menuMusic.destroy();
        this.menuMusic = null;
    }
}