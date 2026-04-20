import Phaser, { Scene } from "phaser";

export class MainScene extends Scene {
    constructor() {
        super("MainScene");

        this.player = null;
        this.robot = null;
        this.cursors = null;
        this.moveLeft = false;
        this.moveRight = false;
        this.moveUp = false;
        this.moveDown = false;
        this.keyboardHandlers = null;

        this.coins = null;
        this.randomMines = null;
        this.homingMines = null;

        this.score = 0;
        this.scoreText = null;

        this.playerSpeed = 200;
        this.robotSpeed = 145;
        this.homingMineSpeed = 165;

        this.randomMineRespawnDelay = 3000;
        this.homingMineRespawnDelay = 4000;
        this.explosionTextureKey = "main-mine-explode";
        this.defaultExplosionDurationMs = 900;
        this.mineRampDurationMs = 60000;
        this.mineRampCheckIntervalMs = 5000;
        this.baseRandomMineTarget = 2;
        this.baseHomingMineTarget = 1;
        this.mineKillRadius = 72;
        this.isGameOver = false;
        this.gameStartTime = 0;
        this.minePopulationTimer = null;
        this.coinPileSpawnTimer = null;
        this.coinTiers = [];
        this.coinPileDelayStartMs = 5000;
        this.coinPileDelayMinMs = 3000;
        this.coinPileDelayDecay = 0.9;
        this.coinPileCurrentDelayMs = this.coinPileDelayStartMs;
        this.robotCoinTravelTimesMs = [];
        this.robotCoinTravelWindowSize = 10;
        this.gameOverTransitionPending = false;
        this.pendingGameOverTimer = null;
        this.pendingGameOverSound = null;
        this.pendingGameOverSoundCompleteHandler = null;

        this.mapScale = 0.5;
        this.mapPixelWidth = 0;
        this.mapPixelHeight = 0;
        this.collisionLayer = null;
        this.backgroundMusic = null;
    }

    preload() {
        // Requested asset source: project-root assets/
        this.load.image("main-player-source", "assets/warrior_spritesheet_calciumtrice.png");
        this.load.spritesheet("main-robot", "assets/lego.png", {
            frameWidth: 37,
            frameHeight: 48
        });
        this.load.spritesheet("main-coin-source", "assets/FinishedB.png", {
            frameWidth: 32,
            frameHeight: 32
        });
        this.load.image("main-random-mine", "assets/bomb.png");
        this.load.image("main-homing-mine", "assets/bomb.png");
        this.load.spritesheet("main-mine-explode", "assets/BombExplosion.png", {
            frameWidth: 32,
            frameHeight: 32
        });

        // Tilemap background (map.json request mapped to existing json_map.json).
        this.load.tilemapTiledJSON("main-map", "assets/json_map.json");
        this.load.image("map_tiles", "assets/mountain_landscape.png");

        // Sounds are served from /public/sounds via Vite static public path.
        this.load.audio("main-player-coin-pick-sfx", "sounds/item_picking_player.mp3");
        this.load.audio("main-robot-coin-pick-sfx", "sounds/coin_picking.mp3");
        this.load.audio("main-explosion-sfx", "sounds/explosion.mp3");
        this.load.audio("main-background-music", "sounds/background.mp3");
    }

    create() {
        const width = this.scale.width;
        const height = this.scale.height;

        this.isGameOver = false;
        this.gameOverTransitionPending = false;
        this.pendingGameOverTimer = null;
        this.pendingGameOverSound = null;
        this.pendingGameOverSoundCompleteHandler = null;
        this.coinPileSpawnTimer = null;
        this.coinPileCurrentDelayMs = this.coinPileDelayStartMs;
        this.robotCoinTravelTimesMs = [];
        this.score = 0;
        this.moveLeft = false;
        this.moveRight = false;
        this.moveUp = false;
        this.moveDown = false;
        this.keyboardHandlers = null;

        this.cameras.main.setBackgroundColor("#101622");

        this.createMapBackground();

        const worldWidth = this.mapPixelWidth || width;
        const worldHeight = this.mapPixelHeight || height;

        this.physics.world.setBounds(0, 0, worldWidth, worldHeight);
        this.cameras.main.setBounds(0, 0, worldWidth, worldHeight);

        this.buildDerivedTextures();
        this.ensureFallbackTextures();
        this.setupAnimations();

        this.player = this.physics.add.sprite(worldWidth * 0.25, worldHeight * 0.5, "main-player-run-0", 0);
        this.player.setCollideWorldBounds(true);
        this.player.setBounce(1);
        this.player.setScale(1.5);
        this.player.body.setSize(this.player.frame.width * 0.6, this.player.frame.height * 0.85, true);

        this.robot = this.physics.add.sprite(worldWidth * 0.75, worldHeight * 0.5, "main-robot", 0);
        this.robot.setCollideWorldBounds(true);
        this.robot.setBounce(1);
        this.fitSprite(this.robot, 56);
        if (this.anims.exists("main-robot-walk")) {
            this.robot.play("main-robot-walk");
        }

        // Robot and player physically deflect each other.
        this.physics.add.collider(this.player, this.robot);

        this.input.keyboard.enabled = true;
        this.input.keyboard.resetKeys();
        this.input.keyboard.addCapture([
            Phaser.Input.Keyboard.KeyCodes.LEFT,
            Phaser.Input.Keyboard.KeyCodes.RIGHT,
            Phaser.Input.Keyboard.KeyCodes.UP,
            Phaser.Input.Keyboard.KeyCodes.DOWN
        ]);
        this.bindKeyboardControls();

        this.time.delayedCall(0, () => {
            this.input.keyboard?.resetKeys?.();
        });

        this.coins = this.physics.add.group();
        this.spawnCoin();
        this.scheduleNextCoinPileSpawn();

        this.randomMines = this.physics.add.group();
        this.homingMines = this.physics.add.group();

        this.spawnRandomMine();
        this.time.delayedCall(5000, () => this.spawnRandomMine());
        this.time.delayedCall(10000, () => this.spawnHomingMine());

        this.physics.add.overlap(this.player, this.coins, this.handleCoinCollected, null, this);
        this.physics.add.overlap(this.robot, this.coins, this.handleCoinCollected, null, this);
        this.physics.add.overlap(this.player, this.randomMines, this.handleRandomMineHit, null, this);
        this.physics.add.overlap(this.player, this.homingMines, this.handleHomingMineHit, null, this);
        this.physics.add.collider(this.robot, this.randomMines);
        this.physics.add.collider(this.robot, this.homingMines);

        this.gameStartTime = this.time.now;
        this.minePopulationTimer = this.time.addEvent({
            delay: this.mineRampCheckIntervalMs,
            loop: true,
            callback: this.rebalanceMinePopulation,
            callbackScope: this
        });

        // Collision layer is rendered for visuals only (no physics colliders attached).

        // Keep the player centered by following them with the main camera.
        this.cameras.main.startFollow(this.player, true, 0.1, 0.1);
        this.cameras.main.centerOn(this.player.x, this.player.y);

        if (this.cache.audio.exists("main-background-music")) {
            this.backgroundMusic = this.sound.add("main-background-music", {
                loop: true,
                volume: 0.12
            });
            this.backgroundMusic.play();
        }

        const cleanupSceneResources = () => {
            this.cancelPendingGameOverTransition();
            this.input.keyboard?.resetKeys?.();
            this.unbindKeyboardControls();
            this.input.keyboard.enabled = true;

            if (this.backgroundMusic) {
                this.backgroundMusic.stop();
                this.backgroundMusic.destroy();
                this.backgroundMusic = null;
            }

            if (this.minePopulationTimer) {
                this.minePopulationTimer.remove(false);
                this.minePopulationTimer = null;
            }

            if (this.coinPileSpawnTimer) {
                this.coinPileSpawnTimer.remove(false);
                this.coinPileSpawnTimer = null;
            }
        };

        this.events.once(Phaser.Scenes.Events.SHUTDOWN, cleanupSceneResources);
        this.events.once(Phaser.Scenes.Events.DESTROY, cleanupSceneResources);

        this.scoreText = this.add.text(16, 16, "Score: 0", {
            fontSize: "24px",
            color: "#ffffff"
        }).setScrollFactor(0);
    }

    update() {
        if (this.isGameOver || this.gameOverTransitionPending || !this.player || !this.robot) {
            return;
        }

        this.player.setVelocity(0, 0);

        if (this.moveLeft) {
            this.player.setVelocityX(-this.playerSpeed);
            this.player.flipX = true;
        } else if (this.moveRight) {
            this.player.setVelocityX(this.playerSpeed);
            this.player.flipX = false;
        }

        if (this.moveUp) {
            this.player.setVelocityY(-this.playerSpeed);
        } else if (this.moveDown) {
            this.player.setVelocityY(this.playerSpeed);
        }

        if (this.moveLeft || this.moveRight || this.moveUp || this.moveDown) {
            this.player.anims.play("main-player-run", true);
        } else {
            this.player.anims.stop();
            this.player.setTexture("main-player-run-0");
        }

        const targetCoin = this.getClosestActiveCoin(this.robot.x, this.robot.y);
        if (targetCoin) {
            this.physics.moveToObject(this.robot, targetCoin, this.robotSpeed);
        } else {
            this.robot.setVelocity(0, 0);
        }

        this.homingMines.getChildren().forEach((mine) => {
            if (!mine.active) {
                return;
            }
            this.physics.moveToObject(mine, this.player, this.homingMineSpeed);
        });
    }

    bindKeyboardControls() {
        this.unbindKeyboardControls();

        const down = (key) => {
            if (key === "LEFT") this.moveLeft = true;
            if (key === "RIGHT") this.moveRight = true;
            if (key === "UP") this.moveUp = true;
            if (key === "DOWN") this.moveDown = true;
        };

        const up = (key) => {
            if (key === "LEFT") this.moveLeft = false;
            if (key === "RIGHT") this.moveRight = false;
            if (key === "UP") this.moveUp = false;
            if (key === "DOWN") this.moveDown = false;
        };

        const leftDown = () => down("LEFT");
        const leftUp = () => up("LEFT");
        const rightDown = () => down("RIGHT");
        const rightUp = () => up("RIGHT");
        const upDown = () => down("UP");
        const upUp = () => up("UP");
        const downDown = () => down("DOWN");
        const downUp = () => up("DOWN");

        this.keyboardHandlers = {
            leftDown,
            leftUp,
            rightDown,
            rightUp,
            upDown,
            upUp,
            downDown,
            downUp
        };

        this.input.keyboard.on("keydown-LEFT", leftDown);
        this.input.keyboard.on("keyup-LEFT", leftUp);
        this.input.keyboard.on("keydown-RIGHT", rightDown);
        this.input.keyboard.on("keyup-RIGHT", rightUp);
        this.input.keyboard.on("keydown-UP", upDown);
        this.input.keyboard.on("keyup-UP", upUp);
        this.input.keyboard.on("keydown-DOWN", downDown);
        this.input.keyboard.on("keyup-DOWN", downUp);

        this.events.once(Phaser.Scenes.Events.SHUTDOWN, () => {
            this.moveLeft = false;
            this.moveRight = false;
            this.moveUp = false;
            this.moveDown = false;
            this.unbindKeyboardControls();
        });
    }

    unbindKeyboardControls() {
        if (!this.keyboardHandlers) {
            return;
        }

        this.input.keyboard.off("keydown-LEFT", this.keyboardHandlers.leftDown);
        this.input.keyboard.off("keyup-LEFT", this.keyboardHandlers.leftUp);
        this.input.keyboard.off("keydown-RIGHT", this.keyboardHandlers.rightDown);
        this.input.keyboard.off("keyup-RIGHT", this.keyboardHandlers.rightUp);
        this.input.keyboard.off("keydown-UP", this.keyboardHandlers.upDown);
        this.input.keyboard.off("keyup-UP", this.keyboardHandlers.upUp);
        this.input.keyboard.off("keydown-DOWN", this.keyboardHandlers.downDown);
        this.input.keyboard.off("keyup-DOWN", this.keyboardHandlers.downUp);
        this.keyboardHandlers = null;
    }

    buildDerivedTextures() {
        if (this.textures.exists("main-player-source")) {
            for (let frame = 0; frame < 10; frame += 1) {
                this.createTextureFromSheetCell(`main-player-run-${frame}`, "main-player-source", 10, 10, frame, 2, 1);
            }
        }

        this.coinTiers = [
            { key: "main-coin-source", value: 1, frame: 4 },
            { key: "main-coin-source", value: 2, frame: 5 },
            { key: "main-coin-source", value: 3, frame: 6 },
            { key: "main-coin-source", value: 4, frame: 7 }
        ];
    }

    ensureFallbackTextures() {
        if (!this.textures.exists("main-player-run-0")) {
            this.createRectTexture("main-player-run-0", 32, 48, 0x3b82f6);
        }

        if (this.coinTiers.length === 0) {
            this.coinTiers = [
                { key: "main-coin-tier-0", value: 1 },
                { key: "main-coin-tier-1", value: 2 },
                { key: "main-coin-tier-2", value: 3 },
                { key: "main-coin-tier-3", value: 4 }
            ];
        }

        this.createRectTexture("main-coin-tier-0", 32, 32, 0xfacc15);
        this.createRectTexture("main-coin-tier-1", 32, 32, 0xfbbf24);
        this.createRectTexture("main-coin-tier-2", 32, 32, 0xf59e0b);
        this.createRectTexture("main-coin-tier-3", 32, 32, 0xea580c);

        this.createCircleTexture("main-random-mine", 12, 0xef4444);
        this.createCircleTexture("main-homing-mine", 12, 0xa855f7);
        this.createCircleTexture("main-mine-explode", 32, 0xf97316);
    }

    createTextureFromSheetCell(targetKey, sourceKey, columns, rows, columnIndex, rowIndex, inset = 0) {
        if (this.textures.exists(targetKey)) {
            return;
        }

        const texture = this.textures.get(sourceKey);
        const sourceImage = texture?.getSourceImage?.() || texture?.source?.[0]?.image;
        if (!sourceImage) {
            return;
        }

        const cellWidth = Math.round(sourceImage.width / columns);
        const cellHeight = Math.round(sourceImage.height / rows);
        const sx = columnIndex * cellWidth + inset;
        const sy = rowIndex * cellHeight + inset;
        const sw = Math.max(1, cellWidth - inset * 2);
        const sh = Math.max(1, cellHeight - inset * 2);

        const canvasTexture = this.textures.createCanvas(targetKey, sw, sh);
        const ctx = canvasTexture.getContext();
        ctx.clearRect(0, 0, sw, sh);
        ctx.drawImage(sourceImage, sx, sy, sw, sh, 0, 0, sw, sh);
        canvasTexture.refresh();
    }

    createRectTexture(key, width, height, color) {
        if (this.textures.exists(key)) {
            return;
        }

        const graphics = this.make.graphics({ x: 0, y: 0, add: false });
        graphics.fillStyle(color, 1);
        graphics.fillRect(0, 0, width, height);
        graphics.generateTexture(key, width, height);
        graphics.destroy();
    }

    createCircleTexture(key, radius, color) {
        if (this.textures.exists(key)) {
            return;
        }

        const size = radius * 2;
        const graphics = this.make.graphics({ x: 0, y: 0, add: false });
        graphics.fillStyle(color, 1);
        graphics.fillCircle(radius, radius, radius);
        graphics.generateTexture(key, size, size);
        graphics.destroy();
    }

    setupAnimations() {
        if (!this.anims.exists("main-player-run")) {
            const frameList = [];
            for (let frame = 0; frame < 10; frame += 1) {
                const key = `main-player-run-${frame}`;
                if (this.textures.exists(key)) {
                    frameList.push({ key });
                }
            }

            if (frameList.length > 1) {
                this.anims.create({
                    key: "main-player-run",
                    frames: frameList,
                    frameRate: 12,
                    repeat: -1
                });
            }
        }

        if (!this.anims.exists("main-robot-walk")) {
            const frameCount = Math.max(0, this.textures.get("main-robot").frameTotal - 1);
            if (frameCount >= 2) {
                this.anims.create({
                    key: "main-robot-walk",
                    frames: this.anims.generateFrameNumbers("main-robot", {
                        start: 0,
                        end: frameCount - 1
                    }),
                    frameRate: 10,
                    repeat: -1
                });
            }
        }

        if (this.anims.exists("main-mine-explode-anim")) {
            return;
        }

        const explodeFrameCount = Math.max(0, this.textures.get(this.explosionTextureKey).frameTotal - 1);
        if (explodeFrameCount < 2) {
            return;
        }

        this.anims.create({
            key: "main-mine-explode-anim",
            frames: this.anims.generateFrameNumbers(this.explosionTextureKey, {
                start: 0,
                end: explodeFrameCount - 1
            }),
            frameRate: 10,
            repeat: 0
        });
    }

    getExplosionDurationMs() {
        try {
            if (!this.cache.audio.exists("main-explosion-sfx")) {
                return this.defaultExplosionDurationMs;
            }

            const probe = this.sound.add("main-explosion-sfx");
            const seconds = probe?.totalDuration || probe?.duration || 0;
            probe?.destroy();

            if (Number.isFinite(seconds) && seconds > 0) {
                return Math.round(seconds * 1000);
            }
        } catch (_error) {
            // Fall through to default duration if audio metadata is unavailable.
        }

        return this.defaultExplosionDurationMs;
    }

    fitSprite(sprite, maxSize) {
        if (sprite.width <= maxSize && sprite.height <= maxSize) {
            return;
        }

        const scale = Math.min(maxSize / sprite.width, maxSize / sprite.height);
        sprite.setScale(scale);
        if (sprite.body) {
            sprite.body.setSize(sprite.width * 0.7, sprite.height * 0.7, true);
        }
    }

    randomPoint(padding) {
        return {
            x: Phaser.Math.Between(padding, this.physics.world.bounds.width - padding),
            y: Phaser.Math.Between(padding, this.physics.world.bounds.height - padding)
        };
    }

    spawnCoin() {
        if (this.gameOverTransitionPending || this.isGameOver) {
            return null;
        }

        const point = this.randomPoint(24);
        const tier = this.pickCoinTier();
        const coin = this.coins.create(point.x, point.y, tier.key, tier.frame);

        coin.setData("coinValue", tier.value);
        coin.setData("spawnedAtMs", this.time.now);
        this.fitSprite(coin, 28);
        return coin;
    }

    getClosestActiveCoin(x, y) {
        const activeCoins = this.coins.getChildren().filter((coin) => coin.active);

        if (activeCoins.length === 0) {
            return null;
        }

        let closestCoin = activeCoins[0];
        let minDistanceSq = Phaser.Math.Distance.BetweenPointsSquared({ x, y }, closestCoin);

        for (let i = 1; i < activeCoins.length; i += 1) {
            const coin = activeCoins[i];
            const distanceSq = Phaser.Math.Distance.BetweenPointsSquared({ x, y }, coin);
            if (distanceSq < minDistanceSq) {
                minDistanceSq = distanceSq;
                closestCoin = coin;
            }
        }

        return closestCoin;
    }

    pickCoinTier() {
        const index = Phaser.Math.Between(0, this.coinTiers.length - 1);
        return this.coinTiers[index];
    }

    getRobotMeanCoinTravelTimeMs() {
        if (this.robotCoinTravelTimesMs.length === 0) {
            return null;
        }

        const total = this.robotCoinTravelTimesMs.reduce((sum, value) => sum + value, 0);
        return total / this.robotCoinTravelTimesMs.length;
    }

    recordRobotCoinTravelTime(coin) {
        const spawnedAtMs = coin?.getData("spawnedAtMs");
        if (!Number.isFinite(spawnedAtMs)) {
            return;
        }

        const elapsedMs = this.time.now - spawnedAtMs;
        if (!Number.isFinite(elapsedMs) || elapsedMs <= 0) {
            return;
        }

        this.robotCoinTravelTimesMs.push(elapsedMs);
        if (this.robotCoinTravelTimesMs.length > this.robotCoinTravelWindowSize) {
            this.robotCoinTravelTimesMs.shift();
        }
    }

    getAdaptiveCoinPileDelayMs() {
        const meanRobotTravelMs = this.getRobotMeanCoinTravelTimeMs();
        const dynamicFloorMs = Number.isFinite(meanRobotTravelMs)
            ? Math.max(this.coinPileDelayMinMs, meanRobotTravelMs)
            : this.coinPileDelayMinMs;
        const targetFloorMs = Math.min(this.coinPileDelayStartMs, dynamicFloorMs);

        return Math.max(targetFloorMs, this.coinPileCurrentDelayMs * this.coinPileDelayDecay);
    }

    scheduleNextCoinPileSpawn() {
        if (!this.scene.isActive() || this.gameOverTransitionPending || this.isGameOver) {
            return;
        }

        if (this.coinPileSpawnTimer) {
            this.coinPileSpawnTimer.remove(false);
            this.coinPileSpawnTimer = null;
        }

        this.coinPileSpawnTimer = this.time.delayedCall(this.coinPileCurrentDelayMs, () => {
            if (!this.scene.isActive() || this.gameOverTransitionPending || this.isGameOver) {
                return;
            }

            this.spawnCoin();
            this.coinPileCurrentDelayMs = this.getAdaptiveCoinPileDelayMs();
            this.scheduleNextCoinPileSpawn();
        });
    }

    spawnRandomMine() {
        if (this.gameOverTransitionPending || this.isGameOver) {
            return null;
        }

        const point = this.randomPoint(40);
        const mine = this.randomMines.create(point.x, point.y, "main-random-mine");

        mine.setData("mineType", "random");
        mine.setData("respawnDelay", this.randomMineRespawnDelay);
        this.fitSprite(mine, 32);
        mine.setBounce(1);
        mine.setCollideWorldBounds(true);
        mine.setVelocity(Phaser.Math.Between(-170, 170), Phaser.Math.Between(-170, 170));

        return mine;
    }

    spawnHomingMine() {
        if (this.gameOverTransitionPending || this.isGameOver) {
            return null;
        }

        const point = this.randomPoint(40);
        const mine = this.homingMines.create(point.x, point.y, "main-homing-mine");

        mine.setData("mineType", "homing");
        mine.setData("respawnDelay", this.homingMineRespawnDelay);
        this.fitSprite(mine, 34);
        mine.setBounce(1);
        mine.setCollideWorldBounds(true);

        // Homing mine explodes exactly 10 seconds after spawning.
        this.time.delayedCall(10000, () => this.explodeMine(mine));

        return mine;
    }

    getMineRampFactor() {
        if (!this.gameStartTime) {
            return 0;
        }

        const elapsed = this.time.now - this.gameStartTime;
        return Phaser.Math.Clamp(elapsed / this.mineRampDurationMs, 0, 1);
    }

    getActiveGroupCount(group) {
        return group.getChildren().filter((child) => child.active).length;
    }

    rebalanceMinePopulation() {
        if (!this.scene.isActive() || this.gameOverTransitionPending || this.isGameOver) {
            return;
        }

        const rampFactor = this.getMineRampFactor();
        const targetRandom = Math.max(this.baseRandomMineTarget, Math.floor(this.baseRandomMineTarget * (1 + rampFactor)));
        const targetHoming = Math.max(this.baseHomingMineTarget, Math.floor(this.baseHomingMineTarget * (1 + rampFactor)));

        const currentRandom = this.getActiveGroupCount(this.randomMines);
        const currentHoming = this.getActiveGroupCount(this.homingMines);

        for (let i = currentRandom; i < targetRandom; i += 1) {
            this.spawnRandomMine();
        }

        for (let i = currentHoming; i < targetHoming; i += 1) {
            this.spawnHomingMine();
        }
    }

    explodeMine(mine) {
        if (!mine || !mine.active || this.gameOverTransitionPending || this.isGameOver) {
            return;
        }

        const mineType = mine.getData("mineType") || "random";
        const respawnDelay = mine.getData("respawnDelay") || this.randomMineRespawnDelay;
        const explosionDurationMs = this.getExplosionDurationMs();
        console.log(`[MainScene] ${mineType} bomb exploded at (${Math.round(mine.x)}, ${Math.round(mine.y)})`);

        const explosionSound = this.cache.audio.exists("main-explosion-sfx")
            ? this.sound.add("main-explosion-sfx", { volume: 0.5 })
            : null;
        if (explosionSound) {
            explosionSound.play();
        }

        const explosion = this.add.sprite(mine.x, mine.y, this.explosionTextureKey, 0);
        this.fitSprite(explosion, 56);

        if (this.player?.active) {
            const distanceToPlayer = Phaser.Math.Distance.Between(mine.x, mine.y, this.player.x, this.player.y);
            if (distanceToPlayer <= this.mineKillRadius) {
                this.triggerGameOver(explosionSound);
            }
        }

        mine.disableBody(true, true);
        mine.destroy();

        if (this.anims.exists("main-mine-explode-anim")) {
            explosion.play({ key: "main-mine-explode-anim", duration: explosionDurationMs });
            explosion.once(Phaser.Animations.Events.ANIMATION_COMPLETE, () => {
                if (explosion.scene) {
                    explosion.destroy();
                }
            });
        } else {
            this.time.delayedCall(explosionDurationMs, () => {
                if (explosion.scene) {
                    explosion.destroy();
                }
            });
        }

        this.time.delayedCall(respawnDelay, () => {
            if (!this.scene.isActive() || this.gameOverTransitionPending || this.isGameOver) {
                return;
            }

            if (mineType === "homing") {
                this.spawnHomingMine();
            } else {
                this.spawnRandomMine();
            }
        });
    }

    handleHomingMineHit(_player, mine) {
        if (this.gameOverTransitionPending || this.isGameOver) {
            return;
        }

        this.explodeMine(mine);
    }

    handleRandomMineHit(_player, mine) {
        if (this.gameOverTransitionPending || this.isGameOver) {
            return;
        }

        this.explodeMine(mine);
    }

    triggerGameOver(explosionSound = null) {
        if (this.isGameOver || this.gameOverTransitionPending) {
            return;
        }

        this.gameOverTransitionPending = true;
        this.freezePlayerInput();

        const startGameOverScene = () => {
            if (this.isGameOver) {
                return;
            }

            this.isGameOver = true;
            this.cancelPendingGameOverTransition();
            this.scene.start("GameOverScene", { score: this.score });
        };

        const fallbackDelay = this.getExplosionDurationMs();
        this.pendingGameOverTimer = this.time.delayedCall(fallbackDelay + 50, () => {
            startGameOverScene();
        });

        if (explosionSound) {
            this.pendingGameOverSound = explosionSound;
            this.pendingGameOverSoundCompleteHandler = () => {
                startGameOverScene();
            };
            this.pendingGameOverSound.once(Phaser.Sound.Events.COMPLETE, this.pendingGameOverSoundCompleteHandler);
        }
    }

    freezePlayerInput() {
        this.moveLeft = false;
        this.moveRight = false;
        this.moveUp = false;
        this.moveDown = false;

        this.input.keyboard?.resetKeys?.();
        this.unbindKeyboardControls();
        this.input.keyboard.enabled = false;

        this.player?.anims?.stop?.();
        this.robot?.anims?.stop?.();

        this.physics.world.pause();

        if (this.player?.body) {
            this.player.setVelocity(0, 0);
        }

        if (this.robot?.body) {
            this.robot.setVelocity(0, 0);
        }

        if (this.randomMines) {
            this.randomMines.getChildren().forEach((mine) => {
                mine.setVelocity(0, 0);
            });
        }

        if (this.homingMines) {
            this.homingMines.getChildren().forEach((mine) => {
                mine.setVelocity(0, 0);
            });
        }
    }

    cancelPendingGameOverTransition() {
        if (this.pendingGameOverTimer) {
            this.pendingGameOverTimer.remove(false);
            this.pendingGameOverTimer = null;
        }

        if (this.pendingGameOverSound) {
            if (this.pendingGameOverSoundCompleteHandler) {
                this.pendingGameOverSound.off(Phaser.Sound.Events.COMPLETE, this.pendingGameOverSoundCompleteHandler);
            }
            this.pendingGameOverSound.stop();
            this.pendingGameOverSound.destroy();
            this.pendingGameOverSound = null;
            this.pendingGameOverSoundCompleteHandler = null;
        }
    }

    handleCoinCollected(collector, coin) {
        if (!coin.active || this.gameOverTransitionPending || this.isGameOver) {
            return;
        }

        const value = coin.getData("coinValue") || 1;
        coin.disableBody(true, true);
        const isPlayerCollector = collector === this.player;

        if (isPlayerCollector) {
            if (this.cache.audio.exists("main-player-coin-pick-sfx")) {
                this.sound.play("main-player-coin-pick-sfx", { volume: 0.5 });
            }
            this.score += value;
            this.scoreText.setText(`Score: ${this.score}`);
            console.log(`[MainScene] player picked up coin for +${value}, score=${this.score}`);
        } else {
            this.recordRobotCoinTravelTime(coin);
            if (this.cache.audio.exists("main-robot-coin-pick-sfx")) {
                this.sound.play("main-robot-coin-pick-sfx", { volume: 0.5 });
            }
            console.log(`[MainScene] robot picked up coin for +${value}, score unchanged=${this.score}`);
        }

        this.time.delayedCall(3000, () => {
            if (!coin.scene || this.gameOverTransitionPending || this.isGameOver) {
                return;
            }

            const point = this.randomPoint(24);
            const tier = this.pickCoinTier();
            coin.setData("coinValue", tier.value);
            coin.setData("spawnedAtMs", this.time.now);
            coin.enableBody(true, point.x, point.y, true, true);
            coin.setTexture(tier.key, tier.frame);
        });
    }

    createMapBackground() {
        if (!this.cache.tilemap.exists("main-map") || !this.textures.exists("map_tiles")) {
            return;
        }

        const map = this.make.tilemap({ key: "main-map" });
        const tileset = map.addTilesetImage("map_tiles", "map_tiles");
        if (!tileset) {
            return;
        }

        const backgroundLayer = map.createLayer("background", tileset, 0, 0);
        const collisionLayer = map.createLayer("collision", tileset, 0, 0);
        if (!backgroundLayer) {
            return;
        }

        // Preserve aspect ratio while covering viewport width/height.
        const scaleX = this.scale.width / map.widthInPixels;
        const scaleY = this.scale.height / map.heightInPixels;
        this.mapScale = Math.max(scaleX, scaleY);

        backgroundLayer.setScale(this.mapScale);
        backgroundLayer.setDepth(-100);

        if (collisionLayer) {
            collisionLayer.setScale(this.mapScale);
            collisionLayer.setVisible(true);
             this.collisionLayer = collisionLayer;
        }

        this.mapPixelWidth = map.widthInPixels * this.mapScale;
        this.mapPixelHeight = map.heightInPixels * this.mapScale;
    }
}
