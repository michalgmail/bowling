import com.kenshoo.michal.infra.spring.ApplicationContext;
import com.kenshoo.michal.service.impl.Game;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.Resource;

import static org.hamcrest.core.Is.is;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ApplicationContext.class, loader = AnnotationConfigContextLoader.class)
public class GameTest {

    @Resource
    Game game;

    @After
    public void cleanGame() {
        game.initGame();
    }

    @Test
    public void testFrameWith2Hits() {
        game.roll(4);
        game.roll(5);

        Assert.assertThat(game.score(), is(9));
    }

    @Test
    public void testStrike() {

        // First Hit is Strike
        game.roll(10);

        Assert.assertThat(game.score(), is(10));

        // Second hit - two throws should be added to previous score
        game.roll(3);
        game.roll(2);

        Assert.assertThat(game.score(), is(20));
    }

    @Test
    public void testSpare() {
        game.roll(5);
        game.roll(5);

        Assert.assertThat(game.score(), is(10));

        // Second hit - two throws should be added to previous score
        game.roll(3);
        game.roll(2);

        Assert.assertThat(game.score(), is(18));
    }

    @Test
    public void testBonusInLastFrameSpare() {

        getToLastFrame();

        // Last Frame hit spare
        game.roll(5);
        game.roll(5);

        // Add to bonus
        game.roll(7);

        Assert.assertThat(game.score(), is(62));
    }

    @Test
    public void testBonusInLastFrameStrike() {

        getToLastFrame();

        // Last Frame hit spare
        game.roll(10);

        // Add to bonus
        game.roll(7);

        Assert.assertThat(game.score(), is(62));
    }

    @Test
    public void testGameOver() {

        getToLastFrame();

        // Last Frame hit spare
        game.roll(10);

        // Add to bonus
        game.roll(7);

        Assert.assertThat(game.score(), is(62));

        try {
            game.roll(4);
            // Should throw an exception
            Assert.assertTrue(false);
        } catch (RuntimeException e) {
            Assert.assertThat(e.getMessage(), is("Game Over"));
        }
    }

    private void getToLastFrame() {
        for (int i = 0; i < game.NUM_OF_FRAMES - 1; i++) {
            game.roll(3);
            game.roll(2);
        }

        Assert.assertThat(game.score(), is(45));
    }

    @Test
    public void testLegalRoll() {

        try {
            game.roll(11);
            // Should throw an exception
            Assert.assertTrue(false);
        } catch (RuntimeException e) {
            Assert.assertThat(e.getMessage(), is("Illegal roll"));
        }

        try {
            game.roll(-1);
            // Should throw an exception
            Assert.assertTrue(false);
        } catch (RuntimeException e) {
            Assert.assertThat(e.getMessage(), is("Illegal roll"));
        }
    }

    @Test
    public void testWith0Roll() {
        game.roll(3);
        game.roll(7);

        game.roll(0);
        game.roll(4);

        Assert.assertThat(game.score(), is(14));
    }

    @Test
    public void testPerfectGame() {
        for (int i = 0; i < game.NUM_OF_FRAMES; i++) {
            game.roll(10);
        }

        game.roll(10);
        game.roll(10);

        Assert.assertThat(game.score(), is(300));
    }

    @Test
    public void testDouble() {
        game.roll(10);
        game.roll(10);

        game.roll(2);
        game.roll(0);

        Assert.assertThat(game.score(), is(36));
    }

    @Test
    public void testDouble2() {
        game.roll(10);
        game.roll(10);

        game.roll(2);
        game.roll(3);

        Assert.assertThat(game.score(), is(42));
    }

    @Test
    public void testTurkey() {
        game.roll(10);
        game.roll(10);
        game.roll(10);

        game.roll(4);
        game.roll(0);

        Assert.assertThat(game.score(), is(72));
    }

    @Test
    public void testTurkey2() {
        game.roll(10);
        game.roll(10);
        game.roll(10);

        game.roll(4);
        game.roll(3);

        Assert.assertThat(game.score(), is(78));
    }
}
