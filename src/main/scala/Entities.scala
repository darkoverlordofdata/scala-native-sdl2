import scalanative.native._
import SDL._
import SDLExtra._
import SDL_image._
import SDL_image_extras._
/**
 * Entity database record
 */
case class Entity (
    val id: Int,                        /* Unique sequential id */
    val name: String,                   /* Display name */
    val active: Boolean,                /* In use */
    val actor: Actor,                   /* Actor Id */
    val category: Category,             /* Category */
    val position: Point2d,              /* Position on screen */
    val bounds: Rectangle,              /* Collision bounds */
    val sprite: Sprite,                 /* Sprite */
    //                                  /* Optional: */
    val scale: Option[Vector2d],        /* Display scale */
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
    
    def createSprite(renderer:Renderer, path:CString, width:Int, height:Int):Sprite = {
        val surface:Surface = IMG_Load(path)
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

    def createBackground(renderer:Renderer):Entity = {
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
            scale = None,
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def createPlayer(renderer:Renderer):Entity = {
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
            scale = None,
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def createBullet(renderer:Renderer):Entity = {
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
            bounds = new Rectangle(0, 0, (width*2).toInt, height),
            sprite = sprite,
            scale = None,
            tint = None,
            expires = Option(2.0),
            health = None,
            scaleTween = None,
            velocity = None
        )
    }
    def bullet(e:Entity, x:Double, y:Double):Entity = {
        e.copy(active = true,
            expires = Some(1), 
            health = Some(new Health(2, 2)),
            position = new Point2d(x, y),
            tint = Some(new Color(0xd2.toUByte, 0xfa.toUByte, 0x00.toUByte, 0xffa.toUByte)),
            velocity = Some(new Vector2d(0.0, -800.0)))
    }

    def createEnemy1(renderer:Renderer):Entity = {
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
            scale = None,
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def enemy1(e:Entity, width:Int, rand:java.util.Random):Entity = {
        e.copy(active = true,
            position = new Point2d(rand.nextInt(width-35).toDouble, 92.0/2.0),
            velocity = Some(new Vector2d(0.0, 40.0)),
            health = Some(new Health(10, 10)))
    }

    def createEnemy2(renderer:Renderer):Entity = {
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
            scale = None,
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def enemy2(e:Entity, width:Int, rand:java.util.Random):Entity = {
        e.copy(active = true,
            position = new Point2d(rand.nextInt(width-85).toDouble, 172.0/2.0),
            velocity = Some(new Vector2d(0.0, 30.0)),
            health = Some(new Health(20, 20)))
    }

    def createEnemy3(renderer:Renderer):Entity = {
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
            scale = None,
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def enemy3(e:Entity, width:Int, rand:java.util.Random):Entity = {
        e.copy(active = true,
            position = new Point2d(rand.nextInt(width-160).toDouble, 320.0/2.0),
            velocity = Some(new Vector2d(0.0, 20.0)),
            health = Some(new Health(60, 60)))
    }

    def createExplosion(renderer:Renderer):Entity = {
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
            scale = None,
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def explosion(e:Entity, x:Double, y:Double):Entity = {
        e.copy(active = true,
            position = new Point2d(x, y),
            scaleTween = Some(new ScaleTween(0.5/100, 0.5, -3, false, true)),
            tint = Some(new Color(0xd2.toUByte, 0xfa.toUByte, 0xd2.toUByte, 0xfa.toUByte)),
            scale = Some(new Vector2d(0.5, 0.5)),
            expires = Some(0.2))
    }

    def createBang(renderer:Renderer):Entity = {
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
            scale = None,
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def bang(e:Entity, x:Double, y:Double):Entity = {
        e.copy(active = true,
            position = new Point2d(x, y),
            scaleTween = Some(new ScaleTween(0.2/100, 0.2, -3, false, true)),
            tint = Some(new Color(0xd2.toUByte, 0xfa.toUByte, 0xd2.toUByte, 0xfa.toUByte)),
            scale = Some(new Vector2d(0.2, 0.2)),
            expires = Some(0.2))
    }

    def createParticle(renderer:Renderer):Entity = {
        val img = c"/home/bruce/scala/shmupwarz/assets/images/star.png"
        val width = 32
        val height = 32
        val sprite = createSprite(renderer, img, width, height)

        return new Entity(
            id = getUniqueId(),
            name = "Particle",
            active = false,
            actor = ActorParticle,
            category = CategoryExplosion,
            position = new Point2d(0, 0),
            bounds = new Rectangle(0, 0, width, height),
            sprite = sprite,
            scale = None,
            tint = None,
            expires = None,
            health = None,
            scaleTween = None,
            velocity = None
        )
    }

    def particle(e:Entity, x:Double, y:Double, rand:java.util.Random):Entity = {
        val Tau = 6.28318
        val radians = rand.nextDouble() * Tau
        val magnitude = rand.nextInt(200)
        val velocityX = magnitude * scala.math.cos(radians)
        val velocityY = magnitude * scala.math.sin(radians)
        val scale = rand.nextInt(10).toDouble / 10.0
        e.copy(active = true,
            position = new Point2d(x, y),
            scale = Some(new Vector2d(scale, scale)),
            velocity = Some(new Vector2d(velocityX, velocityY)),
            tint = Some(new Color(0xfa.toUByte, 0xfa.toUByte, 0xd2.toUByte, 0xff.toUByte)),
            expires = Some(0.5))
    }
}