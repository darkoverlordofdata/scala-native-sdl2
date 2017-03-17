import scalanative.native._
import SDL._
import SDLExtra._
import SDL_image._
import SDL_image_extras._



class Entity (
    val id: Int,                    /* Unique sequential id */
    val name: String,               /* Display name */
    val active: Boolean,            /* In use */
    val actor: Actor,             /* Actor Id */
    val category: Category,       /* Actor Category */
    val position: Point2d,              /* Position on screen */
    val bounds: Rectangle,              /* Collision bounds */
    val sprite: Sprite,                 /* Sprite */
    val scale: Vector2d,                /* Display scale */
    // //                              /* Optional: */
    val tint: Option[Color],            /* Color to use as tint */
    val expires: Option[Double],        /* Countdown until expiration */
    val health: Option[Health],         /* Track health */
    val scaleTween: Option[ScaleTween], /* scale Tweening variables*/
    val velocity: Option[Vector2d]      /* Cartesian velocity*/
    
)

object Entities {
    var uniqueId = 0
    def getUniqueId():Int = {
        uniqueId+= 1
        uniqueId
    }
    def createSprite(renderer:Ptr[Renderer], path:CString, width:Int, height:Int):Sprite = {
        val surface:Ptr[Surface] = IMG_Load(path)
        if (surface == null) {
            println("unable to load surface")
        }
        val sprite = new Sprite(texture = SDL_CreateTextureFromSurface(renderer, surface), width = width, height = height)
        if (sprite.texture == null) {
            println("unable to load texture")
        }
        SDL_SetTextureBlendMode(sprite.texture, SDL_BLENDMODE_BLEND)
        return sprite

    }

    def createBackground(renderer:Ptr[Renderer]):Entity = {
        val img = c"/home/bruce/scala/shmupwarz/assets/images/BackdropBlackLittleSparkBlack.png"
        val width = 512
        val height = 512
        val sprite = createSprite(renderer, img, width, height)

        return new Entity(
            id = getUniqueId(),
            name = "Background",
            active = true,
            actor = ActorBackground,
            category = CategoryBackground,
            position = new Point2d(0, 0),
            bounds = new Rectangle(0, 0, width, height),
            sprite = sprite,
            scale = new Vector2d(0, 0),
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def createPlayer(renderer:Ptr[Renderer]):Entity = {
        val img = c"/home/bruce/scala/shmupwarz/assets/images/fighter.png"
        val width = 108
        val height = 172
        val sprite = createSprite(renderer, img, width, height)

        return new Entity(
            id = getUniqueId(),
            name = "Player",
            active = true,
            actor = ActorPlayer,
            category = CategoryPlayer,
            position = new Point2d(0, 0),
            bounds = new Rectangle(0, 0, width, height),
            sprite = sprite,
            scale = new Vector2d(0, 0),
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def createBullet(renderer:Ptr[Renderer]):Entity = {
        val img = c"/home/bruce/scala/shmupwarz/assets/images/bullet.png"
        val width = 5
        val height = 17
        val sprite = createSprite(renderer, img, width, height)

        return new Entity(
            id = getUniqueId(),
            name = "Bullet",
            active = false,
            actor = ActorBullet,
            category = CategoryBullet,
            position = new Point2d(0, 0),
            bounds = new Rectangle(0, 0, width, height),
            sprite = sprite,
            scale = new Vector2d(0, 0),
            tint = None,
            expires = Option(2.0),
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def createEnemy1(renderer:Ptr[Renderer]):Entity = {
        val img = c"/home/bruce/scala/shmupwarz/assets/images/enemy1.png"
        val width = 69
        val height = 91
        val sprite = createSprite(renderer, img, width, height)

        return new Entity(
            id = getUniqueId(),
            name = "Enemy1",
            active = false,
            actor = ActorEnemy1,
            category = CategoryEnemy,
            position = new Point2d(0, 0),
            bounds = new Rectangle(0, 0, width, height),
            sprite = sprite,
            scale = new Vector2d(0, 0),
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def createEnemy2(renderer:Ptr[Renderer]):Entity = {
        val img = c"/home/bruce/scala/shmupwarz/assets/images/enemy2.png"
        val width = 172
        val height = 172
        val sprite = createSprite(renderer, img, width, height)

        return new Entity(
            id = getUniqueId(),
            name = "Enemy2",
            active = false,
            actor = ActorEnemy2,
            category = CategoryEnemy,
            position = new Point2d(0, 0),
            bounds = new Rectangle(0, 0, width, height),
            sprite = sprite,
            scale = new Vector2d(0, 0),
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def createEnemy3(renderer:Ptr[Renderer]):Entity = {
        val img = c"/home/bruce/scala/shmupwarz/assets/images/enemy3.png"
        val width = 320
        val height = 320
        val sprite = createSprite(renderer, img, width, height)

        return new Entity(
            id = getUniqueId(),
            name = "Enemy3",
            active = false,
            actor = ActorEnemy3,
            category = CategoryEnemy,
            position = new Point2d(0, 0),
            bounds = new Rectangle(0, 0, width, height),
            sprite = sprite,
            scale = new Vector2d(0, 0),
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def createExplosion(renderer:Ptr[Renderer]):Entity = {
        val img = c"/home/bruce/scala/shmupwarz/assets/images/explosion.png"
        val width = 512
        val height = 512
        val sprite = createSprite(renderer, img, width, height)

        return new Entity(
            id = getUniqueId(),
            name = "Explosion",
            active = false,
            actor = ActorExplosion,
            category = CategoryExplosion,
            position = new Point2d(0, 0),
            bounds = new Rectangle(0, 0, width, height),
            sprite = sprite,
            scale = new Vector2d(0, 0),
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def createBang(renderer:Ptr[Renderer]):Entity = {
        val img = c"/home/bruce/scala/shmupwarz/assets/images/explosion.png"
        val width = 512
        val height = 512
        val sprite = createSprite(renderer, img, width, height)

        return new Entity(
            id = getUniqueId(),
            name = "Explosion",
            active = false,
            actor = ActorBang,
            category = CategoryExplosion,
            position = new Point2d(0, 0),
            bounds = new Rectangle(0, 0, width, height),
            sprite = sprite,
            scale = new Vector2d(0, 0),
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def createParticle(renderer:Ptr[Renderer]):Entity = {
        val img = c"/home/bruce/scala/shmupwarz/assets/images/star.png"
        val width = 32
        val height = 32
        val sprite = createSprite(renderer, img, width, height)

        return new Entity(
            id = getUniqueId(),
            name = "Explosion",
            active = false,
            actor = ActorBang,
            category = CategoryExplosion,
            position = new Point2d(0, 0),
            bounds = new Rectangle(0, 0, width, height),
            sprite = sprite,
            scale = new Vector2d(0, 0),
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }
}