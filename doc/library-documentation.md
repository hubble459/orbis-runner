## Rendering library

To allow students to get games up-and-running quickly, and to provide solutions for some aspects that are especially unintuitive when starting out with Android game programming, a small library and some example uses have been provided. They can be used as a base for your work. You're very much encouraged to make changes to the library to fit your needs! Or at least, make sure you have a basic understanding of what is happening.  

This document describes how to use this library.


### About this document

This document is written in Markdown using PlantUML for diagrams. To view and edit it from within Android Studio, install the 'Markdown Navigator' plugin.

### Features and scope

The provided library and examples demonstrate how to:

- Draw scaled, rotated and semi-transparent images.
- Animate fluently at 60 frames per second.
- Maintain game speed when the hardware is not capable of achieving the full frame rate. 
- Render on any screen resolution, without much effort.
- Sensibly organize your game logic into independent Entity instances.
- Handle touch events.

If you're creating a game that does not use fluent canvas animation, you're probably better of *not* starting out with this library. This may be the case when your game:
- only changes state as an immediate response to user touches, or
- doesn't use the canvas but only (or mostly) uses native Android Views like `TextView`, `ImageView` and `CheckBox`.


### Overview

The state of a game is represented by a `GameModel` object, that contains a list of objects extending `Entity`. Each `Entity` represents a visible entity or actor in the game. The `GameModel` is meant to be subclassed, to specify specifics for your game.

A `GameView` is an Android custom view. It collaborates with `GameModel` (and its `Entity`s) to render the graphical representation of the game state. `GameView`s offer methods for easily drawing scaled, rotated and semi-transparent bitmaps, and make it easy to work in a resolution independent way.


### Class diagram

This UML class diagram shows the use of the base library with a simple example game. Many details have been intentionally left out.

```plantuml

skinparam class {
    BackgroundColor<< lib >> white
}

class MyGame {
    + score: int
    + getWidth()
    + getHeight()
    + start()
}

class MainActivity {
}

class Hero {
    + draw(GameView)
    + tick()
    + handleTouch(Touch)
}

class FlyingBullet {
    + draw(GameView)
    + tick()
}

class Entity<<lib>> {
    + draw(GameView)
    + tick()
    + handleTouch(Touch)
    + getLayer(): int
}

class GameModel<<lib>> {
    - actualWidth
    - actualHeight
    + getWidth()
    + getHeight()
    + start()
    + addEntityEntity)
    + removeEntity(Entity)
    + getEntities(X.class): List<X>
}

class GameView<<lib>> {
    + setGame(GameModel)
    + setPaused(boolean)

    + drawBitmap(...)
    + getCanvas(): Canvas
}


GameModel o-> "*" Entity

GameView --> GameModel

MyGame -|> GameModel
MyGame *-- Hero
note on link
    Hero references back to MyGame
    to increase the score and to
    spawn FlyingBullet entities.
end note


Hero -> "*" FlyingBullet

Entity <|-- Hero
Entity <|-- FlyingBullet

MainActivity --> MyGame
MainActivity -> GameView


class android.View {
    + onDraw()
}
class android.Activity {
    + onCreate()
    + onResume()
    + onPause()
}
android.Activity <|-- MainActivity 
android.View <|-- GameView

```


### Class reference

#### Entity

Although Model View Control (MVC) is often a good idea, it's use is not really convenient for games. In this library, the central concept is the `Entity`, which you can think of as a thing that is visible in the game, or changing the state of the game. Examples: the hero, a cloud, a bullet, an enemy cruiser, and the planet shattering megaboss. But HUD elements (like your health, inventory or buttons) and non-visible elements (like a victory checker, a monster spawner or a collision checker) may also be game objects.

You can create a new kind of game object by extending from `Entity`. `Entity`s need to be added to the `GameModel` by calling `GameMode.addEntity(entity)`. Similarly, entities can be removed using `GameModel.removeEntity(entity)`.

There are four methods on `Entity` that you may want to override:
- `draw(GameView)`. Called each time the screen is refreshed (usually up to 60 times per second). If the entity wants to display something, it will need to do that here onto the given GameView (discussed later).
- `tick()`. Called on every game object 180 times per second (configurable by overriding `GameLoop.ticksPerSecond()`). This method is to update the game state one step. So a bullet may want to move forward and check if it is hitting something. The reason the tick rate is (by default) higher than the maximum frame rate, is that this makes things like collision detection more accurate. Also, when the full frame rate cannot be met, smaller tick steps make for a more fluent experience.
- `getLayer()`. Returns the layer this entity is on. The draw and tick methods are usually called in the order the entities were created. In case this is not desirable, you can put game objects on a different layer. Let's say you want to create a new cloud, but that cloud should be drawn underneath (thus before) the hero, you can give clouds a lower layer number. The default layer number is 0, so the clouds could be put on layer -1.
- `handleTouch(Game.Touch, MotionEvent)`. Used to act on touch events such as `ACTION_UP`, `ACTION_DOWN` or `ACTION_MOVE`. The `Touch` object provides coordinates (translated to the virtual screen), movement, time, etc.


#### GameModel

The `GameModel` class is meant to be subclassed by your own class that contains specifics about your game.

A `GameModel` object represents the state of a single game, mostly by way of a list of `Entity`s.

`GameModel`s are `serializable`, meaning the entire game state can easily be converted to and from a `String`. This can be used in the Android Activity to, for instance, resume a game after it has been moved to the background and stopped by the operating system. 

Methods on `Game` you may want to call:

- addEntity(Entity) will add an `Entity` to the game.
- removeEntity(Entity) will remove an `Entity` from the game.
- `getEntities(Class)` returns a list of `Entity`s that are of type `Class`. For instance:
  ```java
  List<Car> cars = Game.getObjects(Car.class);
  ````

Methods you may want to override:

- `getWidth()`, returning the width of the virtual screen you want to use. You could use a constant value for this, which makes reasoning about scale in your `draw()` methods really easy. However, if the aspect ratio of your `getWidth()` and `getHeight()` doesn't match that of the actual View, black bars will appear. To prevent this, you can base the virtual resolution on the actual resolution, available as protected variables `actualWidth` and `actualHeight`. These might change during the game though, for instance on orientation change.
- `getHeight()`, like `getWidth()`.
- `start()`, called once just before your game is first drawn. At this point, the width and height of the View are known, meaning you could meaningfully call `getWidth()` and `getHeight()` even if these are dependend upon the actual resolution. This makes the `start()` method a suitable point at which to instantiate your initial `Entity`s.
- `ticksPerSecond()`, which returns the number of `tick()` call that are performed per second. If your game doesn't use ticks (for instance when it only changes immediately based on touch events), you can set this to 0. It defaults to 180.


#### GameView

This Android custom View uses the canvas to display graphics. This is done by associating a `GameModel` with it, using `setGame(..)`. From that point on, the model will receive `tick()`s (by default 180 times per second) and will be redrawn up to 60 times per second.

Drawing happens by asking each `Entity`, in order, to `draw(GameView)` itself. The `Entity` can:
- Call `GameView.getCanvas()` to get access to the Android Canvas object, on which the usual methods like `drawRect(..)` are available.
- Use the helper method `GameView.drawBitmap(...)`, to easily copy a `Bitmap` image to a specific position on the canvas, while optionally rotating and scaling it, and adding translucency. A `Bitmap` can be created from an Android resource using the `GameView.getBitmapFromResource(int resourceId)` helper method. In case you only want to draw a part of the Bitmap (for instance when using sprite sheets), you can create a new Bitmap containing only the intended part using `Bitmap.createBitmap(..)`.

In both cases, the `GameView` will arrange for the `Canvas` to be in such a state that it is `GameModel.getWidth()` wide and `GameModel.getHeight()` high.

