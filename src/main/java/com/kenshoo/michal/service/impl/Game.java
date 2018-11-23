package com.kenshoo.michal.service.impl;

import com.kenshoo.michal.model.Frame;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Service("game")
public class Game {

    private List<Frame> board;
    public final int NUM_OF_FRAMES = 10;
    private boolean bonusFrame1 = false;
    private boolean bonusFrame2 = false;

    @PostConstruct
    public void initGame() {
        board = new ArrayList<Frame>();
        bonusFrame1 = false;
        bonusFrame2 = false;
    }

    public void roll(int pins) {

        assertLegalRoll(pins);

        Frame currentFrame = getCurrentFrame();

        if (board.size() == NUM_OF_FRAMES && currentFrame.isFrameClosed() && !inBonusHit(currentFrame, pins)) {
            throw new RuntimeException("Game Over");
        }

        if (!bonusFrame1) {
            if (currentFrame.getFirstHit() == null) {
                currentFrame.setFirstHit(pins);
            } else {
                currentFrame.setSecondHit(pins);
            }

            addBonusToPrevFrame(currentFrame, pins);
        } else {
            addBonusToLastFrame(currentFrame, pins);
        }


    }

    private void assertLegalRoll(int pins) {
        if (pins < 0 || pins > Frame.STRIKE_COUNT) {
            throw new RuntimeException("Illegal roll");
        }
    }

    private void addBonusToLastFrame(Frame currentFrame, int pins) {
        currentFrame.addToBonus(pins);
    }

    private boolean inBonusHit(Frame prevFrame, int pins) {

        if ((prevFrame.isStrike() || prevFrame.isSpare()) && !bonusFrame1) {

            if ((pins == Frame.STRIKE_COUNT) && (!bonusFrame2)) {
                bonusFrame1 = false;
                bonusFrame2 = true;
            } else {
                bonusFrame1 = true;
            }
            return true;
        }
        return false;
    }

    private void addBonusToPrevFrame(Frame currentFrame, int bonus) {
        Frame prevFrame = getPrevFrame();
        Frame doubleFrame = getDoubleFrame();

        if (prevFrame != null && prevFrame.isStrike()) {
            prevFrame.addToBonus(bonus);

            if ((doubleFrame != null) && (currentFrame.isFirstHit())) {
                doubleFrame.addToBonus(bonus);
            }

        } else if (prevFrame != null && prevFrame.isSpare() && currentFrame.isFirstHit()) {
            prevFrame.addToBonus(bonus);
        }
    }

    private Frame getDoubleFrame() {
        Frame frame = null;

        if ((board.size() > 2) && board.get(board.size() - 2).isStrike() && board.get(board.size() - 3).isStrike()) {
            frame = board.get(board.size() - 3);
        }
        return frame;
    }

    private Frame getCurrentFrame() {
        Frame frame;

        if (board.size() == NUM_OF_FRAMES) {
            return board.get(NUM_OF_FRAMES - 1);
        }

        if (board.size() > 0 && !board.get(board.size() - 1).isFrameClosed()) {
            frame = board.get(board.size() - 1);
        } else {
            frame = new Frame();
            board.add(frame);
        }
        return frame;
    }

    private Frame getPrevFrame() {
        Frame frame = null;

        if (board.size() > 1) {
            frame = board.get(board.size() - 2);
        }
        return frame;
    }

    public int score() {
        return board.stream().mapToInt(Frame::getScore).sum();
    }
}
