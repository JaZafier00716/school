import Phaser, { Scene } from "phaser";
import { loadTopScores, saveScore } from "../utils/gameStateStorage";

export class GameOverScene extends Scene {
    constructor() {
        super("GameOverScene");
        this.finalScore = 0;
    }

    preload() {
        this.load.audio("main-game-over-sfx", ["sounds/game_over.mp3", "sounds/game_over"]);
    }

    init(data) {
        this.cameras.main.fadeIn(350, 0, 0, 0);
        this.finalScore = data?.score || 0;
    }

    create() {
        const topScores = saveScore(this.finalScore);

        this.add.rectangle(0, 0, this.scale.width, this.scale.height, 0x0a0a14).setOrigin(0, 0);

        const panel = this.add.rectangle(
            this.scale.width * 0.5,
            this.scale.height * 0.5,
            620,
            430,
            0x1f2937,
            0.96
        );
        panel.setStrokeStyle(3, 0xf87171, 0.85);

        this.add.text(this.scale.width * 0.5, 105, "Game Over", {
            fontSize: "52px",
            color: "#f9fafb",
            fontStyle: "bold"
        }).setOrigin(0.5);

        this.add.text(this.scale.width * 0.5, 170, `Score: ${this.finalScore}`, {
            fontSize: "30px",
            color: "#fde68a",
            fontStyle: "bold"
        }).setOrigin(0.5);

        const scoreList = (topScores.length > 0 ? topScores : loadTopScores())
            .map((score, index) => `${index + 1}. ${score}`)
            .join("\n");

        this.add.text(this.scale.width * 0.5, 220, "Top 5", {
            fontSize: "24px",
            color: "#fca5a5",
            fontStyle: "bold"
        }).setOrigin(0.5);

        this.add.text(this.scale.width * 0.5, 250, scoreList || "No scores yet", {
            fontSize: "22px",
            color: "#e5e7eb",
            align: "center"
        }).setOrigin(0.5, 0);

        this.createButton(this.scale.width * 0.5, 410, "Play Again", () => {
            this.scene.start("MainScene");
        });

        this.createButton(this.scale.width * 0.5, 470, "Back To Menu", () => {
            this.scene.start("MenuScene");
        });

        if (this.cache.audio.exists("main-game-over-sfx")) {
            this.sound.play("main-game-over-sfx", { volume: 0.7 });
        } else {
            console.warn("[GameOverScene] game over sound is unavailable on this device/browser.");
        }
    }

    createButton(x, y, label, onClick) {
        const button = this.add.rectangle(x, y, 270, 46, 0xb91c1c, 0.96)
            .setStrokeStyle(2, 0xfca5a5)
            .setInteractive({ useHandCursor: true });

        this.add.text(x, y, label, {
            fontSize: "21px",
            color: "#fef2f2",
            fontStyle: "bold"
        }).setOrigin(0.5);

        button.on("pointerover", () => button.setFillStyle(0xdc2626, 0.98));
        button.on("pointerout", () => button.setFillStyle(0xb91c1c, 0.96));
        button.on("pointerdown", onClick);
    }
}