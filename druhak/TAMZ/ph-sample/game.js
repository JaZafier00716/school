var config = {
    type: Phaser.AUTO,
    width: Math.min(window.innerWidth, window.outerWidth),
    height: Math.min(window.innerHeight, window.outerHeight),
    physics: {
        default: 'arcade',
        arcade: {
            gravity: { y: 0 },
            debug: false
        }
    },    
    scene: {
        preload: preload,
        create: create,
        update: update
    },
    debug: true
};

var game = new Phaser.Game(config);

var backgroundLayer;
var collisionLayer;
var itemsLayer;

var map;
var coinsCollected = 0;
var bestCollected = 0;
var text;
//var robot;
var player;
var bombs;
var gameOver = false;
var move_ctl = false;
var left,right,up,down;

var isCollision;

function preload ()
{
    
	//PM: Robot example only - will be eliminated in final game
    this.load.spritesheet('robot', 'assets/lego.png',
       { frameWidth: 37, frameHeight: 48 } ); 
        
    this.load.spritesheet('player', 'assets/warrior_spritesheet_calciumtrice.png',
        { frameWidth: 32, frameHeight: 32 } ); 

    this.load.spritesheet('items', 'assets/FinishedB.png', { frameWidth: 32, frameHeight: 32 } ); 
    
    this.load.image('tiles', 'assets/mountain_landscape.png');
    this.load.tilemapTiledJSON('json_map', 'assets/json_map.json');
    
}

function resize (width, height)
{
/*    if (width === undefined) { width = game.config.width; }
    if (height === undefined) { height = game.config.height; }
    //console.log('W: ' +  width + ', H: ' + height); 
    if (width < backgroundLayer.width || height < backgroundLayer.height) {
		map.scene.cameras.main.zoom = 0.5;
		map.scene.cameras.main.setPosition(-width/2, -height/2);
  } else {
		map.scene.cameras.main.zoom = 1;
		map.scene.cameras.main.setPosition(0,0);
	}
    //backgroundLayer.setSize(width, height);
    map.scene.cameras.resize(width/map.scene.cameras.main.zoom, height/map.scene.cameras.main.zoom);
	if (game.renderer.type === Phaser.WEBGL){	
		game.renderer.resize(width, height);
	}
    updateText();
*/
}		
function create ()
{
    isCollision = 0;
    map = this.make.tilemap({ key: 'json_map' });
    //F: 'map_tiles' - name of the tilesets in json_map.json
    //F: 'tiles' - name of the image in load.images()
    var tiles = map.addTilesetImage('map_tiles','tiles');

    backgroundLayer = map.createLayer('background', tiles, 0, 0);
    //collisionLayer = map.createLayer('collision', tiles, 0, 0)//.setVisible(false);
    //collisionLayer.setCollisionByExclusion([ -1 ]);
    //collisionLayer.setCollisionBetween(203, 255);    
    
    //items = this.physics.add.sprite(100, 150, 'items', 0);
    //items.setBounce(0.1);
    
    
    //PM: Robot example only 
    robot = this.physics.add.sprite(120, 300, 'robot');
    robot.setBounce(0.1);
    //---
	
    player = this.physics.add.sprite(100, 450, 'player');
    player.setBounce(0.1);
    
    
    //this.physics.add.collider(player, collisionLayer);
    this.physics.add.overlap(player, backgroundLayer);
    
    //F:set collision range 
    //backgroundLayer.setCollisionBetween(1, 25);    
       
    //F:Checks to see if the player overlaps with any of the items, 
    //f:if he does call the collisionHandler function
    //this.physics.add.overlap(player, items, collisionHandler);
    
    
    //this.cameras.main.startFollow(player);    
 
    text = this.add.text(game.canvas.width/2, 16, '', {
        fontSize: '3em',
        fontFamily: 'fantasy',
        align: 'center',
        boundsAlignH: "center", 
        boundsAlignV: "middle", 
        fill: '#ffffff'
    });
    text.setOrigin(0.5);
    text.setScrollFactor(0);    
    updateText();
    
    //PM: Robot example only - will be eliminated in final game
    this.anims.create({
        key: 'robot_run',
        frames: this.anims.generateFrameNumbers('robot', { start: 0, end: 15 }),
        frameRate: 20,
        repeat: -1
    });
	robot.anims.play('robot_run', true);
   

    this.anims.create({
        key: 'walk',
        frames: this.anims.generateFrameNumbers('player', { start: 11, end: 19}),
        frameRate: 10,
        repeat: -1
    }); 

    this.anims.create({
        key: 'idle',
        frames: this.anims.generateFrameNumbers('player', { start: 1, end: 9}),
        frameRate: 10,
        repeat: -1
    }); 
    
    cursors = this.input.keyboard.createCursorKeys();  

	this.input.on('pointerdown', function (pointer) { 
		move_ctl = true; 
		pointer_move(pointer); 
	});
	this.input.on('pointerup', function (pointer) { move_ctl = false; reset_move()});
	this.input.on('pointermove', pointer_move);
	window.addEventListener('resize', function (event) {
		resize(Math.min(window.innerWidth, window.outerWidth), Math.min(window.innerHeight, window.outerHeight));
	}, false);		
	resize(Math.min(window.innerWidth, window.outerWidth), Math.min(window.innerHeight, window.outerHeight));
}

function pointer_move(pointer) {
		var dx=dy=0;
		//var min_pointer=20; // virtual joystick
		var min_pointer = (player.body.width + player.body.height) / 4 ; // following pointer by player
		if (move_ctl) {
			reset_move();
/*			// virtual joystick
 			dx =  (pointer.x - pointer.downX); 
			dy = (pointer.y - pointer.downY);*/
			
			// following pointer by player
			dx = (pointer.x / map.scene.cameras.main.zoom - player.x);
			dy = (pointer.y / map.scene.cameras.main.zoom - player.y);
		    //console.log( 'Xp:'  + player.x + ', Xc:'  + pointer.x + ', Yp:' + player.y + ', Yc:' + pointer.y );
			
			if (Math.abs(dx) > min_pointer) {
				left = (dx < 0); 
				right = !left; 
			} else { 
				left = right = false;
			}
			if (Math.abs(dy) > min_pointer) {
				up = (dy < 0); 
				down = !up; 
			} else { 
				up = down = false;
			}
		}
		//console.log( 'L:'  + left + ', R:'  + right + ', U:' + up + ', D:' + down, ', dx: ' + dx + ',dy: ' + dy );
}

function reset_move() {
  up = down = left = right = false;
}

function update ()
{     
	let move_x = true
	// Needed for player following the pointer:
	if (move_ctl) { pointer_move(game.input.activePointer); }
	
    // Horizontal movement
    if (cursors.left.isDown || left)
    {
        player.body.setVelocityX(-150);
        player.angle = 90;
        player.anims.play('walk', true); 
    }
    else if (cursors.right.isDown || right)
    {
        player.body.setVelocityX(150);
        player.angle = 270;
        player.anims.play('walk', true); 
    }
    else
    {
        player.body.setVelocityX(0);
        move_x = false
    }

    // Vertical movement
    if (cursors.up.isDown || up)
    {
        player.body.setVelocityY(-150);
        player.angle = 180;
        player.anims.play('walk', true); 
    }
    else if (cursors.down.isDown || down)
    {
        player.body.setVelocityY(150);
        player.anims.play('walk', true); 
        player.angle = 0;
    }
    else
    {
        player.body.setVelocityY(0);
        if (! move_x) {
			player.anims.play('idle', true); 
		}
    }

}


function updateText ()
{
	text.setPosition(game.canvas.width/2 / map.scene.cameras.main.zoom, text.height);
    text.setText(
        'Coins collected: ' + coinsCollected //+ '    Best result: ' + bestCollected
    );
    text.setColor('white');
}

// If the player collides with items
function collisionHandler (player, item) {   
    
    let delta_x = item.width/2 + 1;
    let delta_y = item.height/2 + 1;
    coinsCollected += 1;
    //if (coinsCollected > bestCollected) { bestCollected = coinsCollected; }
    updateText();
    //Will destroy item completely: items.destroy();  
    item.disableBody(true, true);
      
    /*if (item.body.enable == false)
    {
        var h = map.heightInPixels-delta_y;
        var w = map.widthInPixels-delta_x;
        var itemX = Phaser.Math.Between(delta_x, w);
        var itemY = Phaser.Math.Between(delta_y, h);
        var itemID = Phaser.Math.Between(0, item.texture.frameTotal-2);
        item.setFrame(itemID);
        item.enableBody(true, itemX, itemY, true, true);
    }*/
       
}
