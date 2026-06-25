# Blue Boy Adventure

A 2D top-down RPG game developed in Java using Swing, featuring exploration, combat, NPC interactions, and quests.

## Project Overview

**Blue Boy Adventure** is a feature-rich 2D RPG game where the player takes on the role of a young adventurer exploring a fantasy world filled with dangers, NPCs, and mysteries. The game includes a complete quest system, combat mechanics, inventory management, and environmental interactions.

## Features

- **2D Top-Down Gameplay**: Classic tile-based world exploration
- **Combat System**: Real-time combat with multiple weapons (sword, axe, pickaxe) and projectiles (fireball)
- **Diverse Enemies**: 5+ unique monster types including Slimes, Orcs, Bats, and the powerful Skeleton Lord boss
- **NPC Interactions**: Dynamic NPCs including a merchant, old man guide, and interactive objects
- **Quest System**: Complete quest framework with progression tracking
- **Lore & Story**: Built-in lore system with notes and cipher scrolls for world-building
- **Equipment System**: Equippable items including weapons, shields, armor, and accessories
- **Inventory Management**: Full inventory system with item pickups and management
- **Dynamic Environment**: Interactive tiles (destructible walls, trees, metal plates), weather system, and lighting
- **Audio**: Full soundtrack and sound effects for immersive gameplay
- **Save/Load System**: Complete game state persistence
- **Cutscenes & Transitions**: Scene transitions and cutscene management
- **Tutorial System**: Built-in tutorial for new players
- **Particle Effects**: Visual effects for combat and special events

## Project Structure

```
My2DGame/
├── src/
│   ├── main/                    # Core game logic
│   │   ├── Main.java            # Entry point
│   │   ├── GamePanel.java       # Main game loop and rendering
│   │   ├── UI.java              # User interface elements
│   │   ├── Config.java          # Configuration management
│   │   ├── CollisionChecker.java# Collision detection
│   │   ├── EventHandler.java    # Event management
│   │   ├── EntityGenerator.java # Entity spawning
│   │   ├── CameraShake.java     # Camera effects
│   │   ├── Sound.java           # Audio management
│   │   ├── CutsceneManager.java # Cutscene handling
│   │   ├── TransitionManager.java# Scene transitions
│   │   ├── TutorialManager.java # Tutorial system
│   │   ├── LoreManager.java     # Story/lore system
│   │   └── WeatherManager.java  # Weather effects
│   │
│   ├── entity/                  # Dynamic game entities
│   │   ├── NPC_Merchant.java    # Merchant NPC
│   │   ├── NPC_OldMan.java      # Guide NPC
│   │   ├── NPC_BigRock.java     # Interactive object
│   │   ├── PlayerDummy.java     # Player entity
│   │   └── Particle.java        # Particle effects
│   │
│   ├── monster/                 # Enemy types
│   │   ├── MON_Bat.java
│   │   ├── MON_GreenSlime.java
│   │   ├── MON_RedSlime.java
│   │   ├── MON_Orc.java
│   │   └── MON_SkeletonLord.java# Boss enemy
│   │
│   ├── object/                  # Collectible items and equipment
│   │   ├── OBJ_Sword_Normal.java
│   │   ├── OBJ_Axe.java
│   │   ├── OBJ_Shield_Wood.java
│   │   ├── OBJ_Shield_Blue.java
│   │   ├── OBJ_Heart.java       # Health drop
│   │   ├── OBJ_ManaCrystal.java # Mana drop
│   │   ├── OBJ_Potion_Red.java
│   │   ├── OBJ_Coin_Bronze.java
│   │   ├── OBJ_Chest.java
│   │   ├── OBJ_Door.java
│   │   ├── OBJ_Key.java
│   │   ├── OBJ_Lantern.java
│   │   ├── OBJ_Boots.java
│   │   ├── OBJ_Pickaxe.java
│   │   ├── OBJ_Tent.java
│   │   ├── OBJ_Fireball.java
│   │   ├── OBJ_CipherScroll.java
│   │   └── More objects...
│   │
│   ├── tile/                    # Tilemap system
│   │   ├── Tile.java            # Base tile class
│   │   ├── TileManager.java     # Tile rendering and management
│   │   └── Map.java             # Map data and layout
│   │
│   ├── tile_interactive/        # Interactive environmental tiles
│   │   ├── InteractiveTile.java # Base class
│   │   ├── IT_DestructibleWall.java
│   │   ├── IT_DryTree.java
│   │   ├── IT_MetalPlate.java
│   │   └── IT_Trunk.java
│   │
│   ├── environment/             # Environmental systems
│   │   ├── EnvironmentManager.java# Overall env management
│   │   └── Lighting.java        # Lighting system
│   │
│   ├── data/                    # Data persistence
│   │   ├── DataStorage.java     # Game data container
│   │   ├── Progress.java        # Progress tracking
│   │   └── SaveLoad.java        # Save/load functionality
│   │
│   └── ai/                      # Pathfinding and AI
│       ├── PathFinder.java      # A* pathfinding algorithm
│       └── Node.java            # Pathfinding node
│
├── res/                         # Game resources
│   ├── tiles/                   # Tileset graphics (PNG)
│   ├── player/                  # Player sprites and animations
│   ├── monster/                 # Enemy sprites and animations
│   ├── npc/                     # NPC sprites and animations
│   ├── objects/                 # Item and object graphics
│   ├── projectile/              # Projectile sprites
│   ├── sound/                   # Audio files (WAV)
│   │   ├── BlueBoyAdventure.wav # Main theme
│   │   ├── Dungeon.wav
│   │   ├── FinalBattle.wav
│   │   ├── Merchant.wav
│   │   └── Multiple SFX files
│   ├── font/                    # Custom fonts (TTF, OTF)
│   └── maps/                    # Map data files (TXT)
│       ├── world01.txt
│       ├── dungeon01.txt
│       ├── dungeon02.txt
│       └── Multiple map layouts
│
├── .idea/                       # IntelliJ IDEA project configuration
├── config.txt                   # Game configuration file
├── save.dat                     # Player save data
├── My2DGame.iml                # IntelliJ project file
└── .gitignore                  # Git ignore rules
```

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- IntelliJ IDEA Community Edition (recommended)

### Running the Game

1. **Via IntelliJ IDEA:**
   - Open the project in IntelliJ IDEA
   - Run [`src/main/Main.java`](src/main/Main.java)

2. **Via Command Line:**
   ```bash
   javac -d bin src/main/Main.java
   java -cp bin main.Main
   ```

### Game Configuration
Edit `config.txt` to modify game settings:
- Fullscreen toggle
- Graphics settings
- Audio preferences

## Game Systems

### Combat System
- Attack with equipped weapon
- Defend/guard mechanic
- Different weapon types with varying damage/speed
- Special projectile attacks (Fireball)
- Enemy AI with pathfinding and combat behavior

### Inventory & Equipment
- Equip weapons, shields, and armor
- Collect consumable items
- Manage inventory capacity
- Trade with NPCs via merchant

### Quest System
- Dynamic quest tracking via [`src/main/Quest.java`](src/main/Quest.java)
- Quest objectives and completion tracking
- Reward system
- Progress persistence

### Save/Load System
- Automatic and manual save functionality
- Complete game state persistence
- Player progress, inventory, and position tracking
- Located in [`src/data/SaveLoad.java`](src/data/SaveLoad.java)

### Pathfinding & AI
- A* algorithm for enemy navigation
- Intelligent NPC movement
- Player movement validation
- Implemented in [`src/ai/PathFinder.java`](src/ai/PathFinder.java)

### Audio System
- Dynamic music based on game state
- Sound effects for actions and events
- Audio management in [`src/main/Sound.java`](src/main/Sound.java)

## Key Classes & Their Roles

| Class | Purpose |
|-------|---------|
| [`GamePanel.java`](src/main/GamePanel.java) | Main game loop, rendering, and state management |
| [`CollisionChecker.java`](src/main/CollisionChecker.java) | Collision detection and response |
| [`UI.java`](src/main/UI.java) | Health bars, inventory display, menus |
| [`EventHandler.java`](src/main/EventHandler.java) | Event triggers and NPC interactions |
| [`TileManager.java`](src/tile/TileManager.java) | Tile rendering and map management |
| [`PathFinder.java`](src/ai/PathFinder.java) | AI pathfinding for NPCs and enemies |
| [`Sound.java`](src/main/Sound.java) | Audio playback and management |

## Map System

Maps are stored as text files in `res/maps/`:
- Each tile is represented by a numeric ID
- Multiple map layouts available (world, dungeons, indoor areas)
- Referenced in [`src/tile/Map.java`](src/tile/Map.java)

## Controls (Default)

- **Arrow Keys / WASD**: Move character
- **Z**: Attack/Interact
- **X**: Guard/Defend
- **Space**: Use consumable item
- **Esc**: Pause/Menu

## Game States

- **Overworld**: Main exploration mode
- **Dungeon**: Underground or special areas
- **Combat**: Active enemy engagement
- **Dialog**: NPC conversation
- **Cutscene**: Story sequences
- **Pause Menu**: Game paused

## Development Notes

### Architecture
- Object-oriented design with inheritance hierarchy
- Entity-Component-like pattern for game objects
- State management through [`GamePanel.java`](src/main/GamePanel.java)
- Event-driven NPC and quest interactions

### Sprites & Graphics
- Pixel art style with multiple animation frames
- Each entity has directional sprites (up, down, left, right)
- Attack and special action variants
- Dynamic lighting and weather overlays

### Audio
- Multiple music tracks for different areas
- Comprehensive sound effects library
- Audio clips managed via [`Sound.java`](src/main/Sound.java)

## Extending the Game

### Adding New Monsters
1. Create a new class extending base enemy class
2. Add sprite assets to `res/monster/`
3. Configure AI behavior and stats
4. Register in entity spawning system

### Adding New Items
1. Create object class in `src/object/`
2. Add graphics to `res/objects/`
3. Define item properties and effects
4. Register in inventory system

### Creating New Maps
1. Create tilemap file in `res/maps/`
2. Use numeric tile IDs matching your tileset
3. Reference in [`Map.java`](src/tile/Map.java)
4. Add event triggers via [`EventHandler.java`](src/main/EventHandler.java)

## Performance Considerations

- Tile rendering optimized with viewport culling
- Collision detection uses spatial partitioning
- Pathfinding uses A* algorithm for efficiency
- Sprite animation frame caching

## Future Enhancements

Potential areas for expansion:
- Multiplayer support
- More complex quest chains
- Magic system expansion
- Additional equipment tiers
- Procedural dungeon generation
- Improved visual effects

## License

[Specify your license here]

## Credits

Developed by: KozP
Game Title: Blue Boy Adventure

---

For more information on specific systems, refer to the source code documentation in the respective Java files.
