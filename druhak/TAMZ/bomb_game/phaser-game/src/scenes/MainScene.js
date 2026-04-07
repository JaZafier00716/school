import Phaser, { Scene } from "phaser";

export class MainScene extends Scene {
    constructor() {
        super("MainScene");

        this.player = null;
        this.robot = null;
        this.cursors = null;

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
        this.coinTiers = [];

        this.mapScale = 0.5;
        this.mapPixelWidth = 0;
        this.mapPixelHeight = 0;
        this.collisionLayer = null;
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
    }

    create() {
        const width = this.scale.width;
        const height = this.scale.height;

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
        this.player.setScale(2);
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

        this.cursors = this.input.keyboard.createCursorKeys();

        this.coins = this.physics.add.group();
        this.spawnCoin();

        this.randomMines = this.physics.add.group();
        this.homingMines = this.physics.add.group();

        this.spawnRandomMine();
        this.time.delayedCall(5000, () => this.spawnRandomMine());
        this.time.delayedCall(10000, () => this.spawnHomingMine());

        this.physics.add.overlap(this.player, this.coins, this.handleCoinCollected, null, this);
        this.physics.add.overlap(this.robot, this.coins, this.handleCoinCollected, null, this);
        this.physics.add.overlap(this.player, this.randomMines, this.handleRandomMineHit, null, this);
        this.physics.add.overlap(this.player, this.homingMines, this.handleHomingMineHit, null, this);

        // Collision layer is rendered for visuals only (no physics colliders attached).

        // Keep the player centered by following them with the main camera.
        this.cameras.main.startFollow(this.player, true, 0.1, 0.1);
        this.cameras.main.centerOn(this.player.x, this.player.y);

        this.scoreText = this.add.text(16, 16, "Score: 0", {
            fontSize: "24px",
            color: "#ffffff"
        }).setScrollFactor(0);
    }

    update() {
        if (!this.player || !this.robot) {
            return;
        }

        this.player.setVelocity(0, 0);

        if (this.cursors.left.isDown) {
            this.player.setVelocityX(-this.playerSpeed);
            this.player.flipX = true;
        } else if (this.cursors.right.isDown) {
            this.player.setVelocityX(this.playerSpeed);
            this.player.flipX = false;
        }

        if (this.cursors.up.isDown) {
            this.player.setVelocityY(-this.playerSpeed);
        } else if (this.cursors.down.isDown) {
            this.player.setVelocityY(this.playerSpeed);
        }

        if (this.cursors.left.isDown || this.cursors.right.isDown || this.cursors.up.isDown || this.cursors.down.isDown) {
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
            frameRate: 18,
            repeat: 0
        });
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
        const point = this.randomPoint(24);
        const tier = this.pickCoinTier();
        const coin = this.coins.create(point.x, point.y, tier.key, tier.frame);

        coin.setData("coinValue", tier.value);
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

    spawnRandomMine() {
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

    explodeMine(mine) {
        if (!mine || !mine.active) {
            return;
        }

        const mineType = mine.getData("mineType") || "random";
        const respawnDelay = mine.getData("respawnDelay") || this.randomMineRespawnDelay;
        console.log(`[MainScene] ${mineType} bomb exploded at (${Math.round(mine.x)}, ${Math.round(mine.y)})`);

        const explosion = this.add.sprite(mine.x, mine.y, this.explosionTextureKey, 0);
        this.fitSprite(explosion, 56);

        mine.disableBody(true, true);
        mine.destroy();

        if (this.anims.exists("main-mine-explode-anim")) {
            explosion.play("main-mine-explode-anim");
            explosion.once(Phaser.Animations.Events.ANIMATION_COMPLETE, () => {
                if (explosion.scene) {
                    explosion.destroy();
                }
            });
        } else {
            this.time.delayedCall(260, () => {
                if (explosion.scene) {
                    explosion.destroy();
                }
            });
        }

        this.time.delayedCall(respawnDelay, () => {
            if (!this.scene.isActive()) {
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
        this.score = 0;
        this.scoreText.setText("Score: 0");
        this.explodeMine(mine);
    }

    handleRandomMineHit(_player, mine) {
        this.score = 0;
        this.scoreText.setText("Score: 0");
        this.explodeMine(mine);
    }

    handleCoinCollected(_collector, coin) {
        if (!coin.active) {
            return;
        }

        const value = coin.getData("coinValue") || 1;
        coin.disableBody(true, true);
        this.score += value;
        this.scoreText.setText(`Score: ${this.score}`);
        console.log(`[MainScene] coin picked up for +${value}, score=${this.score}`);

        this.time.delayedCall(3000, () => {
            if (!coin.scene) {
                return;
            }

            const point = this.randomPoint(24);
            const tier = this.pickCoinTier();
            coin.setData("coinValue", tier.value);
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
